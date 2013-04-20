// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.game.twoplayer.common;

import com.barrybecker4.game.twoplayer.common.search.strategy.SearchProgress;

/**
 * Requests the next move to be found.
 *
 * @author Barry Becker
 */
public class ComputerMoveRequester implements SearchProgress {

    private TwoPlayerController controller_;

    /**
     * Constructor.
     */
    public ComputerMoveRequester(TwoPlayerController controller) {
        controller_ = controller;
    }

    /**
     * make the computer move and show it on the screen.
     * Since this can take a very long time we will show the user a progress bar
     * to give feedback.
     *
     * @param isPlayer1 if the computer player now moving is player 1.
     * @return true if done. Always returns false unless auto optimizing
     */
    public boolean requestComputerMove( boolean isPlayer1 ) {

        // this will spawn the worker thread and return immediately (unless autoOptimize on)
        return controller_.requestComputerMove(isPlayer1);
    }

    public long getNumMovesConsidered() {
        return controller_.getSearchStrategy().getNumMovesConsidered();
    }

    public int getPercentDone() {
        return (controller_.getSearchStrategy() != null) ? controller_.getSearchStrategy().getPercentDone() : 0;
    }

    public void pause() {
        controller_.pause();
    }

    public boolean isPaused() {
        return controller_.isPaused();
    }

    public void continueProcessing() {
        controller_.getSearchStrategy().continueProcessing();
    }
}