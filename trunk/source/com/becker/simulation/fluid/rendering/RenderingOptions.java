/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.simulation.fluid.rendering;

import com.becker.common.concurrency.Parallelizer;


/**
 * Fluid rendering options
 *
 * @author Barry Becker
 */
public class RenderingOptions {

    /** scales the size of everything   */
    public static final double DEFAULT_SCALE = 4;
    private double scale = DEFAULT_SCALE;

    private boolean showVelocities = false;
    private boolean showPressures = true;
    private boolean useLinearInterpolation = false;
    private boolean showGrid = false;

    /** Manages the worker threads. */
    private Parallelizer<RenderWorker> parallelizer;


    /**
     * Constructor
     */
    public RenderingOptions() {
        setParallelized(false);
        setScale(DEFAULT_SCALE);
    }

    public void setParallelized(boolean useParallelization) {

        parallelizer =
             useParallelization ? new Parallelizer<RenderWorker>() : new Parallelizer<RenderWorker>(1);
    }

    public boolean isParallelized() {
        return parallelizer.getNumThreads() > 1;
    }

    public Parallelizer<RenderWorker> getParallelizer() {
        return parallelizer;
    }


    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }

    public void setShowVelocities(boolean show) {
        showVelocities = show;
    }

    public boolean getShowVelocities() {
        return showVelocities;
    }

    public void setShowPressures(boolean show) {
        showPressures = show;
    }

    public boolean getShowPressures() {
        return showPressures;
    }

    public void setUseLinearInterpolation(boolean useInterp) {
        useLinearInterpolation = useInterp;
    }
    public boolean getUseLinearInterpolation() {
        return useLinearInterpolation;
    }

    public boolean getShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }
}
