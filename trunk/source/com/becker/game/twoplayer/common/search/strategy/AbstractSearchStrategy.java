package com.becker.game.twoplayer.common.search.strategy;

import com.becker.game.common.GameContext;
import com.becker.game.common.MoveList;
import com.becker.game.twoplayer.common.TwoPlayerMove;
import com.becker.game.twoplayer.common.search.options.SearchOptions;
import com.becker.game.twoplayer.common.search.SearchWindow;
import com.becker.game.twoplayer.common.search.Searchable;
import com.becker.game.twoplayer.common.search.tree.IGameTreeViewable;
import com.becker.game.twoplayer.common.search.tree.SearchTreeNode;
import com.becker.optimization.parameter.ParameterArray;

import java.util.List;

/**
 *  This is an abstract base class for a search strategy.
 *  It's sublcasses define the key search algorithms for 2 player zero sum games with perfect information.
 *
 *  @author Barry Becker
 */
public abstract class AbstractSearchStrategy implements SearchStrategy
{

    /** the interface implemented by the generic game controller that provides standard methods. */
    protected Searchable searchable_ = null;

    /** keep track of the number of moves searched so far. Long because there could be quite a few. */
    protected long movesConsidered_ = 0;

    /** approximate percent of search that is complete at given moment. */
    protected int percentDone_ = 0;

    /** weights coefficients for the evaluation polynomial that indirectly determines the best move.   */
    protected ParameterArray weights_;

    /** True when search is paused. */
    private volatile boolean paused_ = false;

    /** The optional ui component that will be updated to reflect the current search tree.  */
    private IGameTreeViewable gameTree_;

    /**
     * Number of moves to consider at the top ply.
     * we use this number to determine how far into the search that we are.
     */
    int numTopLevelMoves_;


    /**
     * Construct the strategy.
     * do not call directly. Use createSearchStrategy factory method instead.
     * @param searchable the game controller that has options and can make/undo moves.
     * @param weights coefficients for the evaluation polynomial that indirectly determines the best move.
     */
    AbstractSearchStrategy( Searchable searchable, ParameterArray weights ) {
        searchable_ = searchable;
        weights_ = weights;
    }

    public SearchOptions getOptions() {
        return searchable_.getSearchOptions();
    }


    /**
     * add a move to the visual game tree (if parent not null).
     * @return the node added to the tree.
     */
    protected SearchTreeNode addNodeToTree( SearchTreeNode parent, TwoPlayerMove theMove,
                                         SearchWindow window, int i )
    {
        SearchTreeNode child = null;
        if (gameTree_ != null) {
            child = new SearchTreeNode( theMove );
            child.setWindow(window);
            gameTree_.addNode(parent, child, i);
        }
        return child;
    }

    /**
     * Show the node in the game tree (if one is used. It is used if parent not null).
     *
     * @param list of pruned nodes
     * @param parent the tree node entry above the current position.
     * @param i th child.
     * @param val the worth of the node/move
     * @param window the lastPruned value of the window tells the alpha or beta threshold compared to.
     *   type either PRUNE_ALPHA or PRUNE_BETA - pruned by comparison with Alpha or Beta.
     */
    protected void showPrunedNodesInTree( MoveList list, SearchTreeNode parent,
                                          int i, int val, SearchWindow window) {
        if (gameTree_ != null) {
           gameTree_.addPrunedNodes(list, parent, i, val, window);
        }
    }

    /**
     * @return true if the move list is empty.
     */
    protected static boolean emptyMoveList( MoveList list, TwoPlayerMove lastMove ) {
        if ( !list.isEmpty() ) return false;

        //If there are no next moves, the game is over and the last player to move won
        if ( lastMove.isPlayer1() )
            lastMove.setInheritedValue(WINNING_VALUE);
        else
            lastMove.setInheritedValue(-WINNING_VALUE);

        return true;
    }

    public final long getNumMovesConsidered() {
        return movesConsidered_;
    }

    public final int getPercentDone() {
        return percentDone_;
    }

    /**
     * Set an optional ui component that will update when the search tree is modified.
     * @param listener game tree listener
     */
    public void setGameTreeEventListener(IGameTreeViewable listener) {
        gameTree_ = listener;
    }

    /**
     * Update the percentage done serching variable for the progress bar
     * if we are at the top level (otherwise this is a no-op).
     */
    protected abstract void updatePercentDone(int depth, List remainingNextMoves);

    /**
     * Get the next move and increment the number of moves considered.
     * @return next move in sorted generated next move list.
     */
    protected TwoPlayerMove getNextMove(MoveList list) {
        movesConsidered_ ++;
        return (TwoPlayerMove)list.remove(0);
    }

    /**
     * For minimax this is always true, but it depends on the player for the nega type searches.
     * @return true if we should evaluate the board from the point of view of player one.
     */
    protected boolean fromPlayer1sPerspective(TwoPlayerMove lastMove) {
        return true;
    }


    // these methods give an external thread debugging controls over the search

    public void pause()
    {
        paused_ = true;
    }


    public final boolean isPaused()
    {
        return paused_;
    }


    public void continueProcessing()
    {
        paused_ = false;
    }

    /**
     * pause if we are paused. Continue when not paused anymore.
     * The pause value is changed by the TwoPlayerBoardViewer
     * @return false right away if not paused. Returns true only if
     *  a long pause has been interrupted.
     */
    boolean pauseInterrupted() {
        try {
            while (paused_) {
                Thread.sleep(100);
            }
            return false;
        } catch (InterruptedException e) {
            //e.printStackTrace();
            GameContext.log(2, "interrupted" );
            e.printStackTrace();
            return true;
        }
    }
}