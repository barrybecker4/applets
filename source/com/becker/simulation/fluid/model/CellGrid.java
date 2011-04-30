package com.becker.simulation.fluid.model;


import javax.swing.*;

/**
 * Data behind the Fluid.
 * @author Barry Becker
 */
public class CellGrid {

    private int dimX_;
    private int dimY_;

    public double u[][];
    public double v[][];
    public double density[][];

    /**
     * Creates a new instance of Grid
     */
    public CellGrid(int dimX, int dimY) {
        
        dimX_ = dimX;
        dimY_ = dimY;

        u =  new double[dimX_ + 2][dimY_ + 2];
        v =  new double[dimX_ + 2][dimY_ + 2];
        density = new double[dimX_ + 2][dimY_ + 2];

        addInitialInkDensity();                
    }

    public double[][] getProperty(CellProperty prop) {
        double[][] ret = null;
         switch (prop) {
            case U : ret = u; break;
            case V : ret = v; break;
            case DENSITY : ret = density; break;
        }
        return ret;
    }

    public void setProperty(CellProperty prop, double[][] values) {
         switch (prop) {
            case U : u = values; break;
            case V : v = values; break;
            case DENSITY : density = values; break;
        }
    }

    public void addInitialInkDensity() {
        for ( int i=2; i<dimX_/2; i++) {
            for ( int j=2; j<dimY_/2; j++) {

                u[i][j] = (0.01 + (Math.cos(0.4*i)+Math.sin(0.3*j))/10.0);
                v[i][j] = (.1 - (Math.sin(.2*i) + Math.sin(0.1 * j)/10.0));
                density[i][j] = (0.1 - Math.sin((i + j)/4.0)/10.0);
            }
        }
    }

}
