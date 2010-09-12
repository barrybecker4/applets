package com.becker.simulation.fractals;

import com.becker.common.ColorMap;
import com.becker.simulation.fractals.algorithm.MandelbrotAlgorithm;

import java.awt.*;

/**
 * Default colormap for visualization.
 * May be edited in the UI.
 * 
 * @author Barry Becker
 */
public class FractalColorMap extends ColorMap {

    private static final double MIN_VALUE = 0;
    private static final  double MAX_VALUE = MandelbrotAlgorithm.MAX_ITERATIONS;
    private static final  double RANGE = MAX_VALUE - MIN_VALUE;

    private static final double[] VALUES = {
          MIN_VALUE,
          MIN_VALUE + 0.04 * RANGE,
          MIN_VALUE + 0.1 * RANGE,
          MIN_VALUE + 0.3 * RANGE,
          MIN_VALUE + 0.5 * RANGE,
          MIN_VALUE + 0.7 * RANGE,
          MIN_VALUE + 0.94 * RANGE,
          MIN_VALUE + RANGE
    };

    private static final Color[] COLORS =  {
        Color.WHITE,
        new Color(0, 0, 255),     // .04
        new Color(100, 0, 250),   // .1
        new Color(0, 255, 255),   // .3
        new Color(0, 255, 0),     // .5
        new Color(255, 255, 0),   // .7
        new Color(255, 0, 0),     // .94
        Color.BLACK
    };

    public FractalColorMap() {
        super(VALUES, COLORS);
    }
}
