// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.game.multiplayer.common.ui;

import com.barrybecker4.common.concurrency.Worker;
import com.barrybecker4.game.common.player.PlayerAction;
import com.barrybecker4.game.multiplayer.common.MultiGameController;
import com.barrybecker4.game.multiplayer.common.online.SurrogateMultiPlayer;

/**
 * Searches for the next computer move in a separate thread.
 *
 * @author Barry Becker
 */
class SurrogateMoveWorker {

    private MultiGameViewer viewer_;
    private MultiGameController controller_;

    /** this is true while the surrogate player thinks about its next move. */
    private boolean processing_ = false;


    /**
     * Construct the search worker.
     */
    public SurrogateMoveWorker(MultiGameViewer viewer) {
        this.viewer_ = viewer;
        controller_ = (MultiGameController) viewer.getController();
    }

    /**
     * Request the next move from the player that the surrogate represents.
     * Launches a separate thread to do the search for the next move so the UI is not blocked.
     * @return true if the game is over
     * @throws AssertionError if something bad happened while waiting.
     */
     public boolean requestSurrogateMove(final SurrogateMultiPlayer player) throws AssertionError {

         /** Worker represents a separate thread for getting the next move. */
         Worker worker = new Worker() {

             private PlayerAction action;

             @Override
             public Object construct() {
                 processing_ = true;
                 action = player.getAction(controller_);  // blocks
                 return action;
             }

              @Override
              public void finished() {

                  viewer_.applyAction(action, player.getPlayer());
                  viewer_.refresh();
                  controller_.advanceToNextPlayer();

                  processing_ = false;
              }
         };

         worker.start();
         return false;
     }

    /**
     *  @return true if the viewer is currently processing (i.e. searching)
     */
    public boolean isProcessing() {
        return processing_;
    }
}