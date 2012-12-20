/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.multiplayer.poker.ui;

import com.barrybecker4.game.common.Move;
import com.barrybecker4.game.common.player.Player;
import com.barrybecker4.game.common.player.PlayerAction;
import com.barrybecker4.game.common.player.PlayerList;
import com.barrybecker4.game.common.ui.viewer.GameBoardRenderer;
import com.barrybecker4.game.multiplayer.common.online.SurrogateMultiPlayer;
import com.barrybecker4.game.multiplayer.common.ui.MultiGameViewer;
import com.barrybecker4.game.multiplayer.poker.PokerAction;
import com.barrybecker4.game.multiplayer.poker.PokerController;
import com.barrybecker4.game.multiplayer.poker.PokerRound;
import com.barrybecker4.game.multiplayer.poker.player.PokerPlayer;
import com.barrybecker4.game.multiplayer.poker.player.PokerRobotPlayer;

import javax.swing.*;
import java.awt.*;

/**
 *  Takes a PokerController as input and displays the
 *  current state of the Poker Game. The PokerController contains a PokerTable object
 *  which describes this state.
 *
 *  @author Barry Becker
 */
public class PokerGameViewer extends MultiGameViewer {

    /**
     * Construct the application
     */
    public PokerGameViewer() {}

    @Override
    protected PokerController createController() {
        return new PokerController();
    }

    @Override
    protected GameBoardRenderer getBoardRenderer() {
        return PokerGameRenderer.getRenderer();
    }

    /**
     * @return   the message to display at the completion of the game.
     */
    @Override
    protected String getGameOverMessage() {
        StringBuilder buf = new StringBuilder("Game Over\n");

        // find the player with the most money. That's the winner.
        PlayerList players = controller_.getPlayers();
        int max = -1;
        PokerPlayer winner = null;
        for (final Player p : players) {
            PokerPlayer pp = (PokerPlayer) p;
            if (pp.getCash() > max) {
                max = pp.getCash();
                winner = pp;
            }
        }
        assert winner != null;
        buf.append(winner.getName()).append(" won the game with $").append(winner.getCash()).append('.');
        return buf.toString();
    }


    /**
     * make the computer move and show it on the screen.
     *
     * @param player computer player to move
     * @return done return true if the game is over after moving
     */
    @Override
    public boolean doComputerMove(Player player) {
        assert(!player.isHuman());
        PokerRobotPlayer robot = (PokerRobotPlayer)player;
        PokerController pc = (PokerController) controller_;

        String msg = applyAction(robot.getAction(pc), robot);

        JOptionPane.showMessageDialog(parent_, msg, robot.getName(), JOptionPane.INFORMATION_MESSAGE);
        refresh();
        pc.advanceToNextPlayer();

        return false;
    }

    /**
     * @param action to take
     * @param player to apply it to
     * @return message to show if on client.
     */
    @Override
    protected String applyAction(PlayerAction action,  Player player) {

        PokerPlayer p = (PokerPlayer) player;
        PokerAction act = (PokerAction) action;
        PokerController pc = (PokerController) controller_;

        String msg = null;
        int callAmount = pc.getCurrentMaxContribution() - p.getContribution();

        switch (act.getActionName()) {
            case FOLD :
                p.setFold(true);
                msg = p.getName() + " folded.";
                break;
            case CALL :
                // GameContext.log(0,"PGV: robot call amount = currentMaxContrib - robot.getContrib) = "
                //                   + pc.getCurrentMaxContribution()+" - "+robot.getContribution());
                if (callAmount <= p.getCash())  {
                    p.contributeToPot(pc, callAmount);
                    msg = p.getName() + " has called by adding "+ callAmount + " to the pot.";
                } else {
                    p.setFold(true);
                    msg = p.getName() + " folded.";
                }
                break;
            case RAISE :
                p.contributeToPot(pc, callAmount);
                int raise = act.getRaiseAmount();
                p.contributeToPot(pc, raise);
                msg = p.getName() + " has met the "+callAmount + ", and rasied the pot by " + raise;
                break;
        }
        return msg;
    }

    /**
     *
     * @param lastMove the move to show (but now record)
     */
    @Override
    public PokerRound createMove(Move lastMove) {
        return new PokerRound();
    }

    /**
     * show who won the round and dispurse the pot
     */
    public void showRoundOver(PokerPlayer winner, int winnings) {

        PlayerList players = controller_.getPlayers();
        for (final Player p : players) {
            PokerPlayer player = (PokerPlayer) p;
            player.getHand().setFaceUp(true);
        }
        refresh();

        RoundOverDialog roundOverDlg = new RoundOverDialog(null, winner, winnings);

        Point p = this.getParent().getLocationOnScreen();

        // offset the dlg so the board is visible as a reference
        roundOverDlg.setLocation((int)(p.getX()+ 0.9*getParent().getWidth()),
                                 (int)(p.getY()+getParent().getHeight()/3.0));

        roundOverDlg.setVisible(true);
    }
}
