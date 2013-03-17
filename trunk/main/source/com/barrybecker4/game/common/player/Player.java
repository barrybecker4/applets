/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.common.player;

import com.barrybecker4.game.common.online.server.IServerConnection;
import com.barrybecker4.game.common.online.OnlinePlayer;

import java.awt.*;
import java.io.Serializable;

/**
 * Represents a player in a game (either human or computer).
 *
 * @author Barry Becker
 */
public class Player implements Serializable, OnlinePlayer {

    private static final long serialVersionUID = 1;

    private static final int HUMAN_PLAYER = 1;
    private static final int COMPUTER_PLAYER = 2;

    /** each player is either human or robot. */
    private int type_;

    /** Becomes true if this player has won the game. */
    private boolean hasWon_ = false;

    private PlayerOptions options_;

    /**
     * Constructor.
     * @param options player options for this new player.
     * @param isHuman true if human rather than computer player
     */
    public Player(PlayerOptions options, boolean isHuman) {
        options_ = options;
        type_ = (isHuman) ? HUMAN_PLAYER : COMPUTER_PLAYER;
    }

    /**
     * Constructor.
     * @param name name of the player
     * @param color some color identifying th eplayer in the ui.
     * @param isHuman true if human rather than computer player
     */
    public Player(String name, Color color, boolean isHuman) {
        this(new PlayerOptions(name, color), isHuman);
    }

    public String getName() {
        return options_.getName();
    }


    public void setName( String name ) {
        this.options_.setName(name);
    }


    public Color getColor() {
        return options_.getColor();
    }

    public PlayerOptions getOptions() {
        return options_;
    }

    public boolean isHuman() {
        return (type_ == HUMAN_PLAYER);
    }

    public void setHuman( boolean human ) {
        type_ =  (human) ? HUMAN_PLAYER : COMPUTER_PLAYER;
    }

    public boolean hasWon() {
        return hasWon_;
    }

    /**
     * Once you have won you should not return to the not-won state
     * unless you are starting a new game.
     */
    public void setWon(boolean won) {
        hasWon_ = won;
    }

    public boolean isSurrogate() {
        return false;
    }

    public Player createSurrogate(IServerConnection connection) {
        return new SurrogatePlayer(this, connection);
    }

    /** if this is a surrogate player then this method should return the actual player behind the surrogate */
    public Player getActualPlayer() {
        return this;
    }

    /**
     * Two players are considered equal if their name and type are the same.
     */
    @Override
    public boolean equals(Object p) {
        Player p1 = (Player) p;
        return (getName().equals(p1.getName()) && isHuman() == p1.isHuman());
    }

    @Override
    public int hashCode() {
        int hash = (isHuman() ? 100000000: 0);
        hash += 10 * getName().hashCode();
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[ *").append(getName()).append("* ");
        if (!isHuman())
            sb.append(" (computer)");
        sb.append(additionalInfo()).append(" ]");
        return sb.toString();
    }

    protected String additionalInfo() {
        return "";
    }
}
