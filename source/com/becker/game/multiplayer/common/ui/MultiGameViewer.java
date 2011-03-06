package com.becker.game.multiplayer.common.ui;

import com.becker.common.Location;
import com.becker.game.common.GameContext;
import com.becker.game.common.Move;
import com.becker.game.common.board.BoardPosition;
import com.becker.game.common.player.Player;
import com.becker.game.common.player.PlayerAction;
import com.becker.game.common.ui.panel.GameChangedEvent;
import com.becker.game.common.ui.viewer.GameBoardViewer;
import com.becker.game.multiplayer.common.MultiGameController;
import com.becker.game.multiplayer.common.online.SurrogateMultiPlayer;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 *  Takes a TrivialController as input and displays the
 *  current state of the Game. The TrivalController contains a TrivialTable object
 *  which describes this state.
 *
 *  @author Barry Becker
 */
public abstract class MultiGameViewer extends GameBoardViewer
{

    private boolean winnerDialogShown_;

    // Construct the application
    protected MultiGameViewer() {}

    @Override
    protected abstract MultiGameController createController();

    /**
     * start over with a new game using the current options.
     */
    @Override
    public void startNewGame()
    {
        reset();
        winnerDialogShown_ = false;
        this.sendGameChangedEvent(null);  // get the info panel to refresh with 1st players name

        if (controller_.getPlayers().getFirstPlayer().isSurrogate()) {
            doSurrogateMove((SurrogateMultiPlayer) controller_.getCurrentPlayer());
        }
        else if (!controller_.getPlayers().getFirstPlayer().isHuman()) {
            controller_.computerMovesFirst();
        }
    }

     /**
      * display a dialog at the end of the game showing who won and other relevant
      * game specific information.
      */
    @Override
    public void showWinnerDialog()
    {
        String message = getGameOverMessage();
        JOptionPane.showMessageDialog( this, message, GameContext.getLabel("GAME_OVER"),
                   JOptionPane.INFORMATION_MESSAGE );
    }


    /**
     * @return   the message to display at the completion of the game.
     */
    @Override
    protected abstract String getGameOverMessage();


    /**
     * make the computer move and show it on the screen.
     *
     * @param player computer player to move
     * @return done return true if the game is over after moving
     */
    public abstract boolean doComputerMove(Player player);

    /**
     * make the computer move and show it on the screen.
     *
     * @param player computer player to move
     * @return done return true if the game is over after moving
     */
    public abstract boolean doSurrogateMove(SurrogateMultiPlayer player);

    /**
     * Do nothting by default.
     * @param action to take
     * @param player to apply it to
     * @return message to show if on client.
     */
    protected String applyAction(PlayerAction action,  Player player) {
        return null;
    }

    /**
     * Implements the GameChangedListener interface.
     * Called when the game has changed in some way
     * @param evt
     */
    @Override
    public void gameChanged(GameChangedEvent evt)
    {
        if (controller_.isDone() && !winnerDialogShown_)  {
            winnerDialogShown_ = true;
            showWinnerDialog();
        }
        else if (!winnerDialogShown_) {
             super.gameChanged(evt);
        }
    }


    /**
     * Many multiplayer games don't use this.
     * @param lastMove the move to show (but now record)
     */
    public Move createMove(Move lastMove)
    {
        // unused for now
        return null;
    }

    /**
     * show who won the round and dispurse the pot.
     * Don't show anything by default.
     */
    public void showRoundOver() {};


    public void highlightPlayer(Player player, boolean highlighted)
    {
        // player.setHighlighted(highlighted);
        this.refresh();
    }

    /**
     * @return the tooltip for the panel given a mouse event
     */
    @Override
    public String getToolTipText( MouseEvent e )
    {
        Location loc = getBoardRenderer().createLocation(e);
        StringBuilder sb = new StringBuilder( "<html><font=-3>" );

        if (controller_.getBoard() != null) {
            BoardPosition space = controller_.getBoard().getPosition( loc );
            if ( space != null && space.isOccupied() && GameContext.getDebugMode() >= 0 ) {
                sb.append("<br>");
                sb.append( loc );
            }
            sb.append( "</font></html>" );
        }
        return sb.toString();
    }

}
