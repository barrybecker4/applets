/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.multiplayer.set.ui;


import com.becker.game.common.player.Player;
import com.becker.game.common.ui.viewer.GameBoardRenderer;
import com.becker.game.common.ui.viewer.ViewerMouseListener;
import com.becker.game.multiplayer.common.MultiGameController;
import com.becker.game.multiplayer.common.online.SurrogateMultiPlayer;
import com.becker.game.multiplayer.common.ui.MultiGameViewer;
import com.becker.game.multiplayer.set.Card;
import com.becker.game.multiplayer.set.SetController;
import com.becker.game.multiplayer.set.SetPlayer;

import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *  Shows the current cards in the Set Game in a canvas.
 *
 * @author Barry Becker
 */
public final class SetGameViewer extends MultiGameViewer {

    /**
     * Constructor
     */
    SetGameViewer()
    {
        NumberFormat formatter_=new DecimalFormat();
        formatter_.setGroupingUsed(true);
        formatter_.setMaximumFractionDigits(0);
    }


    @Override
    public void startNewGame() {
        controller_.reset();
    }

    @Override
    protected ViewerMouseListener createViewerMouseListener() {
        return new SetViewerMouseListener(this);
    }

    /**
     * @return the game specific controller for this viewer.
     */
    @Override
    protected MultiGameController createController() {
        return new SetController();
    }

    @Override
    protected GameBoardRenderer getBoardRenderer() {
        return SetGameRenderer.getRenderer();
    }


    /**
     * make the computer move and show it on the screen.
     *
     * @param player computer player to move
     * @return done return true if the game is over after moving
     */
    @Override
    public boolean doComputerMove(Player player)
    {
        assert false : " no computer player for set yet. coming soon!";
        return false;
    }

    /**
     * make the computer move and show it on the screen.
     *
     * @param player computer player to move
     * @return done return true if the game is over after moving
     */
    @Override
    public boolean doSurrogateMove(SurrogateMultiPlayer player)
    {
        assert false : " no online pla for set yet. coming soon!";
        return false;
    }

    @Override
    public String getGameOverMessage() {
        SetPlayer winner = ((SetController) controller_).determineWinner();
        return "the game is over. The winner is " + winner.getName() + " with " + winner.getNumSetsFound() + "sets";
    }



    public List<Card> getSelectedCards() {

        List<Card> selected = new ArrayList<Card>();
        SetController c = (SetController)controller_;

        for (int i = 0; i<c.getNumCardsShowing(); i++ ) {
            Card card = c.getDeck().get(i);
            if (card.isSelected()) {
                selected.add(card);
            }
        }
        return selected;
    }


    /**
     * @return the tooltip for the panel given a mouse event
     */
    @Override
    public String getToolTipText( MouseEvent e )
    {
        return null;
    }
}

