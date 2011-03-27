package com.becker.simulation.liquid.model;

import javax.vecmath.Vector2d;

/**
 *  Create a grid with uniform velocity at each cell.
 *
 *  @author Barry Becker
 */
public class UniformGrid extends Grid {


    /**
     * Constructor.
     */
    public UniformGrid(int xDim, int yDim, Vector2d velocity) {

        this(xDim, yDim, velocity, CellStatus.EMPTY);
    }

    /**
     * Constructor.
     */
    public UniformGrid(int xDim, int yDim, Vector2d velocity, CellStatus status) {

        super(xDim, yDim);

        for (int j = 1; j < yDim-1; j++ ) {
            for (int i = 1; i < xDim-1; i++ ) {
                setVelocity(i, j, velocity);
                this.getCell(i, i).setStatus(status);
            }
        }
    }

}
