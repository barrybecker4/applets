package com.becker.game.twoplayer.go.board.elements;

import com.becker.game.common.GameContext;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *  A set of GoStrings.
 *
 *  @author Barry Becker
 */
public class GoBoardPositionSet extends LinkedHashSet<GoBoardPosition> {

    public GoBoardPositionSet() {}

    public GoBoardPositionSet(Set<GoBoardPosition> set) {
        super(set);
    }

    /**
     * Note: there is no concept of order.
     * @return one of the members
     */
    public GoBoardPosition getOneMember() {
        return iterator().next();
    }

    /**
     * Set the visited flag back to false for a set of stones.
     * Returns the whole list of stones to unvisited state.
     */
    public void unvisitPositions() {
        for (GoBoardPosition position : this) {
            position.setVisited(false);
        }
    }

    /**
     * pretty print a list of all the current groups (and the strings they contain)
     * @return string form of list of stones.
     */
    String toString(String title) {
        StringBuilder buf = new StringBuilder(title);
        buf.append("\n  ");
        for (GoBoardPosition stone : this) {
            buf.append(stone.toString()).append(", ");
        }
        return buf.substring(0, buf.length() - 2);
    }
}