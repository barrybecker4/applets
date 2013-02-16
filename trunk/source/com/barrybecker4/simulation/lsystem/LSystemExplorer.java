// Copyright by Barry G. Becker, 2013. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.simulation.lsystem;

import com.barrybecker4.simulation.common.Profiler;
import com.barrybecker4.simulation.common.ui.Simulator;
import com.barrybecker4.simulation.common.ui.SimulatorOptionsDialog;
import com.barrybecker4.simulation.lsystem.algorithm.LSystemAlgorithm;

import javax.swing.JPanel;
import java.awt.Graphics;

/**
 * Interactively explores the Henon Phase attractors.
 * See   http://mathworld.wolfram.com/HenonMap.html
 * See   http://www.complexification.net/gallery/machines/henonPhaseDeep/
 *
 * @author Barry Becker.
 */
public class LSystemExplorer extends Simulator {

    private LSystemAlgorithm algorithm_;
    private DynamicOptions options_;

    private boolean useFixedSize_ = false;

    protected static final double INITIAL_TIME_STEP = 10.0;
    protected static final int DEFAULT_STEPS_PER_FRAME = 10;


    public LSystemExplorer() {
        super("LSystem Tree Explorer");
        commonInit();
    }

    /**
     * @param fixed if true then the render area does not resize automatically.
     */
    public void setUseFixedSize(boolean fixed) {
        useFixedSize_ = fixed;
    }

    public boolean getUseFixedSize() {
        return useFixedSize_;
    }

    private void commonInit() {
        algorithm_ = new LSystemAlgorithm();
        initCommonUI();
        reset();
    }

    @Override
    protected void reset() {

        algorithm_.reset();
        setNumStepsPerFrame(DEFAULT_STEPS_PER_FRAME);

        if (options_ != null) {
            options_.reset();
        }
    }

    @Override
    protected SimulatorOptionsDialog createOptionsDialog() {
        return new OptionsDialog( frame_, this );
    }

    @Override
    protected double getInitialTimeStep() {
        return INITIAL_TIME_STEP;
    }

    @Override
    public double timeStep() {
        if ( !isPaused() ) {

            if (!useFixedSize_) {
                algorithm_.setSize(this.getWidth(), this.getHeight());
            }
            algorithm_.timeStep(timeStep_);
        }
        return timeStep_;
    }

    @Override
    public void paint( Graphics g ) {
        if (g == null) return;
        super.paint(g);

        Profiler.getInstance().startRenderingTime();
        g.drawImage(algorithm_.getImage(), 0, 0, null);
        Profiler.getInstance().stopRenderingTime();
    }

    @Override
    public void setScale( double scale ) {}

    @Override
    public double getScale() {
        return 0.01;
    }

    @Override
    public JPanel createDynamicControls() {
        options_ = new DynamicOptions(algorithm_, this);
        return options_;
    }
}
