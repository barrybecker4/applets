/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.twoplayer.common.search.strategy;

import com.barrybecker4.game.twoplayer.common.TwoPlayerMove;
import com.barrybecker4.game.twoplayer.common.search.options.SearchOptions;
import com.barrybecker4.game.twoplayer.common.search.tree.IGameTreeViewable;
import com.barrybecker4.game.twoplayer.common.search.tree.SearchTreeNode;

/**
 * Interface for all SearchStrategies for 2 player games with perfect information.
 *
 * @author Barry Becker
 */
public interface SearchStrategy extends SearchProgress {

    /** anything greater than this is considered a won game. */
    int WINNING_VALUE = 4096;

    /** For our purposes, this is effectively infinity. */
    int INFINITY = 10000000;


    /**
     * The search algorithm.
     * This method is the crux of all 2 player zero sum games with perfect information.
     * Derived classes work by narrowing a bound on the value of the optimal move.
     *
     * @param lastMove the most recent move made by one of the players.
     * @param parent for constructing a ui tree. If null, no game tree is constructed.
     * @return the chosen move (ie the best move) (may be null if no next move).
     */
    TwoPlayerMove search( TwoPlayerMove lastMove, SearchTreeNode parent );

    /**
     * @return  parameters for defining the search.
     */
    SearchOptions getOptions();

    /**
     * An optional game tree event listener. There can be at most one.
     * @param listener event listener
     */
    void setGameTreeEventListener(IGameTreeViewable listener);
}
