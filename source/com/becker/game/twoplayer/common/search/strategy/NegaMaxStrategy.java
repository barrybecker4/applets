package com.becker.game.twoplayer.common.search.strategy;

import com.becker.game.common.GameContext;
import com.becker.game.twoplayer.common.search.tree.SearchTreeNode;
import com.becker.game.twoplayer.common.search.tree.PruneType;
import com.becker.game.twoplayer.common.search.*;
import com.becker.game.twoplayer.common.TwoPlayerMove;
import com.becker.optimization.parameter.ParameterArray;

import java.util.Iterator;
import java.util.List;

/**
 *  This strategy class defines the NegaMax search algorithm.
 *  Negamax is very much like minimax, but it avoids having separate
 *  sections of code for minimizing and maximizing search.
 *
 *  @author Barry Becker
 */
public class NegaMaxStrategy extends AbstractSearchStrategy
{

    /**
     * Construct NegaMax the strategy given a controller interface.
     * @inheritDoc
     */
    public NegaMaxStrategy( Searchable controller, ParameterArray weights )
    {
        super( controller , weights);
    }

    /**
     * @inheritDoc
     */
    @Override
    public TwoPlayerMove search( TwoPlayerMove lastMove, 
                                                     int alpha, int beta, SearchTreeNode parent ) {
        // need to negate alpha and beta on initial call.
        return searchInternal( lastMove, lookAhead_, -alpha, -beta, parent );
    }

    /**
     * @inheritDoc
     */
    protected TwoPlayerMove findBestMove(TwoPlayerMove lastMove, 
                                       int depth, List<? extends TwoPlayerMove> list,
                                       int alpha, int beta, SearchTreeNode parent) {
        int i = 0;
        int bestInheritedValue = -SearchStrategy.INFINITY;
        TwoPlayerMove selectedMove;
        TwoPlayerMove bestMove = (TwoPlayerMove) (list.get( 0 ));

        while ( !list.isEmpty() ) {
            if (pauseInterrupted())
                return lastMove;

            TwoPlayerMove theMove = (TwoPlayerMove) (list.remove(0));
            updatePercentDone(depth, list);

            searchable_.makeInternalMove( theMove );
            SearchTreeNode child = addNodeToTree(parent, theMove, alpha, beta, i++ );

            // recursive call
            selectedMove = searchInternal( theMove, depth-1, -beta, -alpha, child );

            searchable_.undoInternalMove( theMove );

            if (selectedMove == null) {
                // if this happens it means there isn't any possible move beyond theMove.
                continue;
            }

            int selectedValue = - selectedMove.getInheritedValue();
            theMove.setInheritedValue( selectedValue );

            if ( alphaBeta_ ) {
                if ( selectedValue > alpha ) {
                    alpha = selectedValue;
                    bestMove = theMove;
                }
                if ( alpha >= beta ) {
                    showPrunedNodesInTree( list, parent, i, selectedValue, beta, PruneType.BETA);
                    break;
                }
            } else if ( selectedValue > bestInheritedValue ) {
                bestMove = theMove;
                bestInheritedValue = selectedValue;
            }
        }

        bestMove.setSelected(true);
        lastMove.setInheritedValue(bestMove.getInheritedValue());
        return bestMove;
    }

    /**
     * This continues the search in situations where the board position is not stable.
     * For example, perhaps we are in the middle of a piece exchange.
     */
    protected TwoPlayerMove quiescentSearch( TwoPlayerMove lastMove,
                                          int depth, int oldAlpha, int beta, SearchTreeNode parent )
    {
        int alpha = oldAlpha;
        int val = lastMove.getValue();
        lastMove.setInheritedValue(val);
        if ( depth >= maxQuiescentDepth_) {
            return lastMove;
        }
        if (searchable_.inJeopardy( lastMove, weights_, lastMove.isPlayer1() ) ) {
            // then search  a little deeper
            return searchInternal( lastMove, depth+1, alpha, beta, parent );
        }

        if ( alphaBeta_ ) {
            if ( val >= beta )
                return lastMove; // prune
            if ( val > alpha )
                alpha = val;
        }

        // generate those moves that are critically urgent
        // if you generate too many, then you run the risk of an explosion in the search tree
        // these moves should be sorted from most to least urgent
        List list = searchable_.generateUrgentMoves( lastMove, weights_, fromPlayer1sPerspective(lastMove) );

        if ( list == null || list.isEmpty() )
            return lastMove; // nothing to check

        int bestVal = - SearchStrategy.INFINITY;
        TwoPlayerMove bestMove = null;
        movesConsidered_ += list.size();
        GameContext.log( 2, "********* urgent moves = " + list );
        Iterator it = list.iterator();
        int i = 0;

        while ( it.hasNext() ) {
            TwoPlayerMove theMove = (TwoPlayerMove) it.next();
            assert theMove!=null;

            searchable_.makeInternalMove( theMove );
            SearchTreeNode child = addNodeToTree(parent, theMove, alpha, beta, i++ );

            TwoPlayerMove selectedMove = quiescentSearch( theMove, depth+1, -beta, -alpha, child );
            assert selectedMove!=null;

            val = -selectedMove.getInheritedValue();
            theMove.setInheritedValue(val);

            searchable_.undoInternalMove( theMove );
            if ( val > bestVal ) {
                bestMove = theMove;
                bestVal = val;
            }
            if ( alphaBeta_ ) {
                if ( val >= beta ) {
                    //return bestMove;
                    break;
                }
                if ( val > alpha ) {
                    alpha = val;
                    bestMove = theMove;
                }
            }
        }
        if (bestMove == null) {
            System.out.println("returning last move as bestmove");
            bestMove = lastMove;   // avoid returning null
        } else {
            bestMove.setSelected(true);
            lastMove.setInheritedValue(bestMove.getInheritedValue()); /// negate?
        }
        return bestMove;
    }

    protected boolean fromPlayer1sPerspective(TwoPlayerMove lastMove) {
        return lastMove.isPlayer1();
    }
}