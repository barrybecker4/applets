/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.hiq.model;


import com.barrybecker4.common.geometry.Location;

/**
 * Definition for a peg jumping another peg.
 * Immutable.
 *@author Barry Becker
 */
public final class PegMove {

    /* the position to move to */
    private Location toPosition;

    /** The position we moved from. */
    private Location fromPosition;


    /**
     * create a move object representing a transition on the board.
     * A naive implementation might use 4 four byte integers to store the from and to values.
     * This would use 16 bytes of memory per move.
     * If we do this, we will quickly run out of memory because fo the vast numbers of moves that must be stored.
     * I will use just 1 byte to store the move information.
     * All we need to know is the from position (which can be stored in 6 bits) and the to direction (which can be stored in 2 bits)
     * I know that a jump is always 2 spaces.
     */
    PegMove( byte fromRow, byte fromCol,
             byte destinationRow, byte destinationCol) {
        fromPosition = new Location(fromRow, fromCol);
        toPosition = new Location(destinationRow, destinationCol);
    }

    PegMove( Location fromPosition,
             Location destinationPosition) {
        this.fromPosition = fromPosition.copy();
        this.toPosition = destinationPosition.copy();
    }

    /**
     * @return a deep copy.
     */
    public PegMove copy() {
        return new PegMove(fromPosition, toPosition);
    }

    public byte getFromRow() {
        return fromPosition.getRow();
    }
    public byte getFromCol() {
        return fromPosition.getCol();
    }

    public byte getToRow() {
        return toPosition.getRow();
    }
    public byte getToCol() {
        return toPosition.getCol();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("from(").append(fromPosition).append(") to (");
        s.append(toPosition).append(')');
        return s.toString();
    }
}

