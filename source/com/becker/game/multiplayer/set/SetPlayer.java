package com.becker.game.multiplayer.set;

import com.becker.game.common.*;
import com.becker.game.multiplayer.common.*;

import java.awt.*;
import java.text.*;

/**
 * Represents a Player in a Set game
 *
 * @author Barry Becker
 */
public class SetPlayer extends MultiGamePlayer
{

    private int numSetsFound_ = 0;

    public static final int DEFAULT_CASH = 100;


    protected SetPlayer(String name, Color color, boolean isHuman)
    {
        super(name, color, isHuman);
    }

    /**
     * Factory method for creating set players of the appropriate type.
     * @param name
     * @param color
     * @param isHuman
     * @return
     */
    public static SetPlayer createSetPlayer(String name, Color color, boolean isHuman)
    {
       if (isHuman)
           return new SetPlayer(name, color, true);
        else
           return SetRobotPlayer.getSequencedRobotPlayer(name, color);
    }

    /**
     *
     * @param i index of player
     * @return  the default name for player i
     */
    public String getDefaultName(int i)
    {
        Object[] args = {Integer.toString(i)};
        String dname = MessageFormat.format(GameContext.getLabel("SET_DEFAULT_NAME"), args );
        return dname;
    }


    public int getNumSetsFound() {
        return numSetsFound_;
    }

    public void setNumSetsFound(int numSetsFound) {
        this.numSetsFound_ = numSetsFound;
        if (numSetsFound_< 0) {
            numSetsFound_ = 0;
        }
    }

    public void incrementNumSetsFound() {
        numSetsFound_++;
    }

    public void decrementNumSetsFound() {
        // don;t go lower than 0.
        if (numSetsFound_> 0) {
            numSetsFound_--;
        }
    }

    /**
     * @param player
     * @return  true if the players name, isHuman and color attributes are the same as ours
     */
    public boolean equals(Object player) {
        SetPlayer p = (SetPlayer) player;
        return (p.getName().equals(getName()) && (p.isHuman() == isHuman()) && p.getColor().equals(getColor()));
    }

    /**
     * Players with the same name hash to the smae colation. Rare.
     * @return
     */
    public int hashCode() {
        return getName().hashCode();
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer( super.toString() );
        sb.append("Num Sets Found: "+numSetsFound_);
        return sb.toString();
    }


}



