package com.becker.game.twoplayer.common.search;

import com.becker.game.twoplayer.common.TwoPlayerMove;
import com.becker.optimization.parameter.ParameterArray;

/**
 * Interface for all SearchStrategies for 2 player games with perfect information.
 *
 * @author Barry Becker
 */
public interface SearchStrategy {

    /** anything greater than this is considered a won game. */
    public static final int WINNING_VALUE = 1024;


    /**
     * The search algorithm.
     * This method is the crux of all 2 player zero sum games with perfect information.
     * Derived classes work by narrowing a bound on the value of the optimal move.
     *
     * @param lastMove the most recent move made by one of the players.
     * @param weights coefficient for the evaluation polunomial that indirectly determines the best move.
     * @param depth how deep in this local game tree that we are to search.
     * @param quiescentDepth how far to go to reach quiescence if we detect an urgent move.
     * @param alpha same as p2best but for the other player. (alpha)
     * @param beta the maximum of the value that it inherits from above and the best move found at this level (beta).
     * @param parent for constructing a ui tree. If null no game tree is constructed.
     * @return the chosen move (ie the best move) (may be null if no next move).
     */
    TwoPlayerMove search( TwoPlayerMove lastMove, ParameterArray weights,
                                          int depth, int quiescentDepth,
                                          int alpha, int beta, SearchTreeNode parent );


    /**
     * @return the number of moves considered in the search so far
     */
    int getNumMovesConsidered();

    /**
     * Approximate percent completed for the search.
     * Approximate because pruning can cause the search to speed up considerably toward the end.
     *
     * @return the approximate percentage of total search time that has been completed.
     */
   int getPercentDone();


    // these methods give an external thread debugging controls over the search.

    /**
     * Cause search to become paused.
     */
    void pause();

    /**
     * @return true if search is paused.
     */
    boolean isPaused();

    /**
     * Continue processing if the search was paused.
     */
    void continueProcessing();
}
