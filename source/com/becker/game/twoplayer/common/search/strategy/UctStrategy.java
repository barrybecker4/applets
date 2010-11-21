package com.becker.game.twoplayer.common.search.strategy;

import com.becker.game.common.Move;
import com.becker.game.common.MoveList;
import com.becker.game.twoplayer.common.TwoPlayerMove;
import com.becker.game.twoplayer.common.search.options.SearchOptions;
import com.becker.game.twoplayer.common.search.Searchable;
import com.becker.game.twoplayer.common.search.tree.SearchTreeNode;
import com.becker.optimization.parameter.ParameterArray;

import java.util.LinkedList;

/**
 *  Implementation of Upper Confidedence Tree (UCT) search strategy.
 *  This method uses a monte carlo (stochastic) method and is fundamentally different than minimax and its derivatives.
 *  It's sublcasses define the key search algorithms for 2 player zero sum games with perfect information.
 *
 *  @author Barry Becker
 */
public class UctStrategy extends AbstractSearchStrategy {

    private double exploreExploitRatio;

    /**
     * Constructor - do not call directly.
     * @param searchable the game controller that has options and can make/undo moves.
     * @param weights coefficients for the evaluation polynomial that indirectly determines the best move.
     */
    UctStrategy( Searchable searchable, ParameterArray weights ) {
        super(searchable, weights);
        exploreExploitRatio = getOptions().getMonteCarloSearchOptions().getExploreExploitRatio();
    }

    @Override
    public SearchOptions getOptions() {
        return searchable_.getSearchOptions();
    }

    /**
     * {@inheritDoc}
     */
    public TwoPlayerMove search(TwoPlayerMove lastMove, SearchTreeNode parent) {

        int numSimulations = 0;
        int maxSimulations = getOptions().getMonteCarloSearchOptions().getMaxSimulations();
        boolean interrupted = false;

        UctNode root = new UctNode(lastMove, 0);

        while (numSimulations < maxSimulations && !interrupted) {
            playSimulation(root, parent);
            numSimulations++;
            percentDone_ = (100 * numSimulations) / maxSimulations;
        }
        return root.bestNode.move;
    }

    public boolean playSimulation(UctNode lastMoveNode, SearchTreeNode parent) {

        boolean player1Wins = false;
        if (lastMoveNode.numVisits == 0) {
            player1Wins = playRandomGame(lastMoveNode.move);
        }
        else {
            if (lastMoveNode.children == null) {
                createChildren(lastMoveNode);
            }
            UctNode nextNode = uctSelect(lastMoveNode);

            // may be null if there are no move valid moves.
            // this may be happening a little more than expected.

            if (nextNode != null && parent != null) {
                SearchTreeNode child = addNodeToTree(parent, nextNode);
                
                searchable_.makeInternalMove(nextNode.move);
                player1Wins = playSimulation(nextNode, child);
                searchable_.undoInternalMove(nextNode.move);
            }
        }

        lastMoveNode.numVisits++;
        lastMoveNode.updateWin(player1Wins);

        lastMoveNode.setBestNode();
        return player1Wins;
    }

    /**
     * Selects the best child of node.
     * @return the best child of node. May be null if there are no next moves.
     */
    private UctNode uctSelect(UctNode node) {
        double bestUct = -1.0;
        UctNode selected = null;

        for (UctNode child : node.children) {
            double uctValue = child.calculateUctValue(exploreExploitRatio, node.numVisits);
            if (uctValue > bestUct) {
                bestUct = uctValue;
                selected = child;
            }
        }
        return selected;
    }

    /**
     * Add the children to the node.
     * @param node parent node to add children to.
     */
    private void createChildren(UctNode node) {
        node.children = new LinkedList<UctNode>();
        MoveList moves = searchable_.generateMoves(node.move, weights_, true);
        int i=0;
        for (Move m : moves) {
             node.children.add(new UctNode((TwoPlayerMove) m, i++));
        }
    }

    /**
     * Plays a semi-random game from the current node position.
     * Its semi random in the sense that we try to avoid obviously bad moves.
     * @return whether or not player1 won.
     */
    private boolean playRandomGame(TwoPlayerMove move) {

        return playRandomMove(move, searchable_); // getSearchableCopy()); not doing deep enough copy currently.

    }

    private Searchable getSearchableCopy() {
        Searchable s = null;
        try {
           s = searchable_.copy();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return s;
    }

    /**
     * Plays a semi-random game from the current node position.
     * Its semi random in the sense that we try to avoid obviously bad moves.
     * @return whether or not player1 won.
     */
    private boolean playRandomMove(TwoPlayerMove move, Searchable searchable) {

        if (searchable.done(move, false)) {
            return move.getValue() > 0;
        }
        MoveList moves = searchable.generateMoves(move, weights_, true);
        TwoPlayerMove randomMove = (TwoPlayerMove) moves.getFirstMove(); //moves.getRandomMove();

        searchable.makeInternalMove(randomMove);
        boolean result = playRandomMove(randomMove, searchable);
        searchable.undoInternalMove(randomMove);     // really do not want to do this for perf reasons.
        return result;
    }

    /**
     * add a move to the visual game tree (if parent not null).
     * If the new node is already in the tree, do not add it, but maybe update values.
     * @return the node added to the tree.
     */
    protected SearchTreeNode addNodeToTree(SearchTreeNode parent, UctNode node ) {
        SearchTreeNode alreadyChild = parent.hasChild(node.move);
        if (alreadyChild != null)  {
            alreadyChild.attributes = node.getAttributes();
            return alreadyChild;
        }
        return addNodeToTree(parent, node.move, node.getAttributes());
    }
}