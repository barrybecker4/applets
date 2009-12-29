package com.becker.game.multiplayer.poker.player;

import com.becker.game.common.*;
import com.becker.game.multiplayer.common.*;
import com.becker.game.multiplayer.poker.*;

import java.awt.*;
import java.text.*;

/**
 * Represents a Player in a poker game
 *
 * @author Barry Becker
 */
public abstract class PokerPlayer extends MultiGamePlayer
{
    private static final long serialVersionUID = 1;

    // this player's home planet. (like earth is for humans)
    private PokerHand hand_;
    private PokerPlayerMarker piece_;

    // in dollars
    private int cash_;
    private boolean hasFolded_;
    // this becomes true when the player has no money left or not enough to ante up.
    private boolean outOfGame_;
    // the maount that this player has contributed to the pot
    private int contribution_;

    public static final int DEFAULT_CASH = 100;
    
    protected PokerAction action_;


    protected PokerPlayer(String name, int money, Color color, boolean isHuman)
    {
        super(name, color, isHuman);
        cash_ = money;
        contribution_ = 0;
        hasFolded_ = false;
        outOfGame_ = false;
        piece_ = new PokerPlayerMarker(this);
    }

    /**
     * Factory method for creating poker players of the appropriate type.
     * @param name
     * @param color
     * @param isHuman
     * @return
     */
    public static PokerPlayer createPokerPlayer(String name, int money, Color color, boolean isHuman)
    {
       if (isHuman)
           return new PokerHumanPlayer(name, money, color);
        else
           return PokerRobotPlayer.getSequencedRobotPlayer(name, money, color);
    }
    
    /**
     *
     * @param i index of player
     * @return  the default name for player i
     */
    public String getDefaultName(int i)
    {
        Object[] args = {Integer.toString(i)};
        return MessageFormat.format(GameContext.getLabel("POKER_DEFAULT_NAME"), args );
    }

    public PokerHand getHand()
    {
        return hand_;
    }

    public void setHand( PokerHand hand )
    {
        this.hand_ = hand;
    }

    public int getCash()
    {
        return cash_;
    }

    public void setFold(boolean folded) {
        hasFolded_ = folded;
    }

    public boolean hasFolded() {
        return hasFolded_;
    }

    public boolean isOutOfGame() {
        return outOfGame_ || (cash_ <= 0);
    }

    @Override
    public PokerPlayerMarker getPiece() {
        return piece_;
    }

    public void setPiece(PokerPlayerMarker piece) {
        piece_ = piece;
    }


    /**
     * have this player contribute some amount to the pot.
     * of course the amount must be less than they have altogether.
     * If it is greater, then he/she is out of the game.
     * @param amount
     */
    public void contributeToPot(PokerController controller, int amount) {

        if (cash_ < amount)  {
            // the player is out of the game if he is asked to contribute more than he has.
            outOfGame_ = true;
            hasFolded_ = true; // anyone out of the game is also considered folded
            return;
        }
        if (amount > 0) {
            cash_ -= amount;
            contribution_ += amount;
            controller.addToPot(amount);
        }
    }

    public int getCallAmount(PokerController pc) {
        int currentMaxContrib = pc.getCurrentMaxContribution();
        return (currentMaxContrib - getContribution());
    }


    /**
     * start the player over fresh for a new round
     */
    public void resetPlayerForNewRound()  {
        hasFolded_ = false;
        contribution_ = 0;
        outOfGame_ = false;
    }

    /**
     * @return  the amount this player has currently put in the pot this round.
     */
    public int getContribution() {
        return contribution_;
    }

    /**
     * the pot goes to this player
     */
    public void claimPot(PokerController controller)  {
        cash_ += controller.getPotValue();
        controller.setPotValue(0);
    }
    
    @Override
    protected String additionalInfo() {
        StringBuffer sb = new StringBuffer();
        if (getHand() != null) {            
            sb.append(" Hand: "+getHand());
        }
        sb.append(" Money: "+cash_);
        return sb.toString();
    }

}



