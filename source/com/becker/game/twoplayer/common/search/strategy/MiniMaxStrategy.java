package com.becker.game.twoplayer.common.search.strategy;

import com.becker.game.twoplayer.common.search.tree.SearchTreeNode;
import com.becker.game.twoplayer.common.search.tree.PruneType;
import com.becker.game.twoplayer.common.search.*;
import com.becker.game.twoplayer.common.TwoPlayerMove;
import com.becker.optimization.parameter.ParameterArray;

import java.util.Iterator;
import java.util.List;

/**
 * This strategy class defines the MiniMax search algorithm.
 * This is the simplest search strategy to which the other variants are compared.
 *
 *  @author Barry Becker
 */
public final class MiniMaxStrategy extends AbstractSearchStrategy
{

    /**
     * Constructor for the strategy.
     * @param controller the game controller that has options and can make/undo moves.
     */
    public MiniMaxStrategy( Searchable controller )
    {
        super( controller );
    }

    /**
     * @inheritDoc
     */
    public TwoPlayerMove search( TwoPlayerMove lastMove, ParameterArray weights,
                                       int depth, int quiescentDepth,
                                       int oldAlpha, int oldBeta, SearchTreeNode parent )
    {
        List list;   // list of moves to consider
        TwoPlayerMove selectedMove;  // the currently selected move
        int alpha = oldAlpha;
        int beta = oldBeta;

        // if player 1, then search for a high score, else seach for a low score.
        boolean player1 = lastMove.isPlayer1();

        if ( depth == 0 || searchable_.done( lastMove, false ) ) {
            if ( quiescence_ && depth == 0 )
                return quiescentSearch( lastMove, weights, quiescentDepth, alpha, beta, parent );
            else {
                lastMove.setInheritedValue(lastMove.getValue());
                return lastMove;
            }
        }

        // generate a list of all candidate next moves, and pick the best one
        list = searchable_.generateMoves( lastMove, weights, true );
        movesConsidered_ += list.size();
        if (depth == searchable_.getLookAhead())
            numTopLevelMoves_ = list.size();

        if ( emptyMoveList( list, lastMove ) ) {
            // if there are no possible next moves, return null (we hit the end of the game).
            return null;
        }

        int i = 0;
        int selectedValue = -SearchStrategy.INFINITY;
        int bestInheritedValue = -SearchStrategy.INFINITY;
        if ( player1 ) bestInheritedValue = SearchStrategy.INFINITY;

        TwoPlayerMove bestMove = (TwoPlayerMove) (list.get( 0 ));
        while ( !list.isEmpty() ) {
            checkPause();
            if (interrupted_)
                return lastMove;

            TwoPlayerMove theMove = (TwoPlayerMove) (list.remove(0));
            updatePercentDone(depth, list);

            searchable_.makeInternalMove( theMove );
            SearchTreeNode child = addNodeToTree(parent, theMove, alpha, beta, i++);

            // recursive call
            selectedMove = search( theMove, weights, depth-1, quiescentDepth, alpha, beta, child );

            searchable_.undoInternalMove( theMove );

            if (selectedMove == null) {
                // if this happens it means there isn't any possible move beyond theMove.
                continue;
            }

            selectedValue = selectedMove.getInheritedValue();
            if ( player1 ) {
                if ( selectedValue < bestInheritedValue ) {
                    bestMove = theMove;
                    bestInheritedValue = bestMove.getInheritedValue();
                }
            }
            else if ( selectedValue > bestInheritedValue ) {
                bestMove = theMove;
                bestInheritedValue = bestMove.getInheritedValue();
            }

            //********* alpha beta pruning ********
            if ( alphaBeta_ ) {
                if ( player1 && (selectedValue < alpha) ) {
                    if ( selectedValue < beta ) {
                        showPrunedNodesInTree( list, parent, i, selectedValue, beta, PruneType.BETA);
                        break; // pruned
                    }
                    else
                        alpha = selectedValue;
                }
                if ( !player1 && (selectedValue > beta) ) {
                    if ( selectedValue > alpha ) {
                        showPrunedNodesInTree( list, parent, i, selectedValue, alpha, PruneType.ALPHA);
                        break; // pruned
                    }
                    else
                        beta = selectedValue;
                }
            }
            //********* end alpha beta pruning *****
        }

        bestMove.setSelected(true);
        lastMove.setInheritedValue(bestMove.getInheritedValue());
        return bestMove;
    }


    /**
     * This continues the search in situations where the board position is not stable.
     * For example, perhaps we are in the middle of a piece exchange
     */
    private TwoPlayerMove quiescentSearch( TwoPlayerMove lastMove, ParameterArray weights,
                                          int depth, int oldAlpha, int oldBeta, SearchTreeNode parent )
    {
        int alpha = oldAlpha;
        int beta = oldBeta;
        lastMove.setInheritedValue(lastMove.getValue());
        if ( depth >= MAX_QUIESCENT_DEPTH) {
            return lastMove;
        }
        if ( searchable_.inJeopardy( lastMove, weights, true )) {
            // then search  a little deeper
            return search( lastMove, weights, 1, depth+1, alpha, beta, parent );
        }

        boolean player1 = lastMove.isPlayer1();
        if ( player1 ) {
            if ( lastMove.getValue() >= beta )
                return lastMove; // prune
            if ( lastMove.getValue() > alpha )
                alpha = lastMove.getValue();
        }
        else {
            if ( lastMove.getValue() >= alpha )
                return lastMove; // prune
            if ( lastMove.getValue() > beta )
                beta = lastMove.getValue();
        }

        // generate those moves that are critically urgent
        // if you generate too many, then you run the risk of an explosion in the search tree
        // these moves should be sorted from most to least urgent
        List list = searchable_.generateUrgentMoves( lastMove, weights, true );

        if ( list == null || list.isEmpty() ) {
            return lastMove; // nothing to check
        }

        double bestInheritedValue = -SearchStrategy.INFINITY;
        if ( player1 ) bestInheritedValue = SearchStrategy.INFINITY;
        TwoPlayerMove bestMove = (TwoPlayerMove) list.get(0);
        movesConsidered_ += list.size();
        Iterator it = list.iterator();
        int i = 0;

        while ( it.hasNext() ) {
            TwoPlayerMove theMove = (TwoPlayerMove) it.next();
            searchable_.makeInternalMove( theMove );
            SearchTreeNode child = addNodeToTree(parent,  theMove, alpha, beta, i++ );

            TwoPlayerMove selectedMove = quiescentSearch( theMove, weights, depth+1, alpha, beta, child );
            assert selectedMove!=null;

            int selectedValue = selectedMove.getInheritedValue();
            if ( player1 ) {
                if ( selectedValue < bestInheritedValue ) {
                    bestMove = theMove;
                    bestInheritedValue = bestMove.getInheritedValue();
                }
            }
            else if ( selectedValue > bestInheritedValue ) {
                bestMove = theMove;
                bestInheritedValue = bestMove.getInheritedValue();
            }

            searchable_.undoInternalMove( theMove );
            if ( player1 ) {
                if ( bestMove.getInheritedValue() >= beta )
                    return bestMove;  // prune
                if ( bestMove.getInheritedValue() > alpha )
                    alpha = bestMove.getInheritedValue();
            }
            else {
                if ( bestMove.getInheritedValue() >= alpha )
                    return bestMove;  // prune
                if ( bestMove.getInheritedValue() > beta )
                    beta = bestMove.getInheritedValue();
            }
        }
        return bestMove;
    }

}