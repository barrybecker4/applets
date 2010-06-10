package com.becker.common;

import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Represents a location location of something in byte coordinates.
 * The range of bytes are only 0 to 255 and cannot be negative.
 *
 * @author Barry Becker
 */
public final class Location implements Serializable
{
    private static final long serialVersionUID = 1;
    private byte row_ = 0;
    private byte col_ = 0;

    /**
     * Constructs a new point at (0, 0).
     * Default empty constructor
     */
    public Location() {
    }

    /**
     * Constructs a new Location at the given coordinates.
     *
     * @param row  the row  coordinate (0 - 255).
     * @param col  the column coordinate (0 - 255).
     */
    public Location( int row, int col )
    {
        row_ = (byte) row;
        col_ = (byte) col;
    }


    public byte getRow() {
        return row_;
    }

    public void setRow(int row) {
        this.row_ =(byte) row;
    }

    public byte getCol() {
        return col_;
    }

    public void setCol(int col) {
        this.col_ =(byte) col;
    }

    public int getX() {
        return col_;
    }

    public int getY() {
        return row_;
    }

    public void incrementRow(int rowChange) {
        row_ += rowChange;
    }

    public void incrementCol(int colChange) {
        col_ += colChange;
    }

    public void increment(int rowChange, int colChange) {
        incrementRow(rowChange);
        incrementCol(colChange);
    }

    /**
     * Checks to see if the given location has the same coordinates as this
     * one.
     *
     * @param location  The location whose coordinates are to be compared.
     * @return true  The location's coordinates exactly equal this location's.
     */
    @Override
    public boolean equals( Object location )
    {
        if (!(location instanceof Location)) return false;
        Location loc = (Location) location;
        return (loc.getRow() == row_) && (loc.getCol() == col_);
    }
    
    /**
     * If override euals, should also override hashCode
     */
    public int hashCode() {
        return (100 * row_ + col_);
    }

    /**
     * @param loc another location to measure distance from.
     * @return the euclidean distance from this location to another.
     */
    public double getDistanceFrom(Location loc)
    {
        float xDif = Math.abs(col_ - loc.getCol());
        float yDif = Math.abs(row_ - loc.getRow());
        return Math.sqrt( xDif*xDif + yDif*yDif);
    }

    /**
     * @param loc another arbitrary floating point location to measure distance from.
     * @return the euclidean distance from this location to another.
     */
    public double getDistanceFrom(Point2D loc)
    {
        double xDif = Math.abs(col_ - loc.getX());
        double yDif = Math.abs(row_ - loc.getY());
        return Math.sqrt( xDif*xDif + yDif*yDif);
    }

    /**
     * @return the string form
     */
    public String toString()
    {
        return "row=" + row_ + ", colum=" + col_;
    }
}

