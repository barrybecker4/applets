/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.simulation.common.ui;

import com.becker.common.format.FormatUtil;
import com.becker.common.util.FileUtil;
import com.becker.optimization.Optimizee;
import com.becker.optimization.parameter.ParameterArray;
import com.becker.ui.animation.AnimationComponent;
import com.becker.ui.components.GradientButton;
import com.becker.ui.util.GUIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Base class for all simulations.
 *
 * @author Barry Becker
 */
public abstract class Simulator extends AnimationComponent
                                implements Optimizee {

    protected static final String CONFIG_FILE_PATH_PREFIX = FileUtil.PROJECT_HOME + "source/com/becker/simulation/";
    protected static final String ANIMATION_FRAME_FILE_NAME_PREFIX = FileUtil.PROJECT_HOME + "temp/animations/simulation/";

    /** debug level of 0 means no debug info, 3 is all debug info */
    public static final int DEBUG_LEVEL = 0;

    protected SimulatorOptionsDialog optionsDialog_ = null;
    protected static JFrame frame_ = null;

    // rendering options
    protected double timeStep_;
    protected boolean useAntialiasing_ = true;

    /**
     *
     * @param name the name fo the simulator (eg Snake, Liquid, or Trebuchet)
     */
    public Simulator(String name) {
        setName(name);
        timeStep_ = getInitialTimeStep();
    }

    protected void initCommonUI() {
        GUIUtil.setCustomLookAndFeel();
    }

    protected abstract double getInitialTimeStep();

    public void setTimeStep( double timeStep )  {
        timeStep_ = timeStep;
    }

    public double getTimeStep() {
        return timeStep_;
    }

    public void setAntialiasing( boolean use ) {
        useAntialiasing_ = use;
    }
    public boolean getAntialiasing() {
        return useAntialiasing_;
    }

    public void setScale( double scale ) {}
    
    public double getScale() {
        return 1;
    }

    protected GradientButton createOptionsButton() {

        GradientButton button = new GradientButton( "Options" );

        optionsDialog_ = createOptionsDialog();

        button.addActionListener( new ActionListener() {

            public void actionPerformed( ActionEvent e ) {

                optionsDialog_.setLocationRelativeTo( (Component) e.getSource() );
                // pause the snake while the options are open
                final Simulator simulator = optionsDialog_.getSimulator();
                final boolean oldPauseVal = simulator.isPaused();
                simulator.setPaused( true );
                optionsDialog_.showDialog();
                simulator.setPaused( oldPauseVal );
            }
        } );
        return button;
    }


    protected abstract SimulatorOptionsDialog createOptionsDialog();
    
    /**
     *return to the initial state.
     */
    protected abstract void reset();


    public JPanel createTopControls() {

        JPanel controls = new JPanel();
        controls.add( createStartButton() );
        
        controls.add( createResetButton() );

        controls.add( createOptionsButton() );
        //controls.add(simulator.getStepButton());
        return controls;
    }

    @Override
    protected String getFileNameBase() {

        return this.getClass().getName();
    }
    
    /**
     *
     * @return  a reset button that allows you to restore the initial condition of the simulation.
     */
    protected JButton createResetButton() {

        final JButton resetButton = new JButton( "Reset");
        resetButton.addActionListener( new ActionListener()  {
            public void actionPerformed( ActionEvent e )
            {    
                reset();
            }
        });
        return resetButton;
    }
    
    
    /**
     * Override this to return ui elements that can be used to modify the simulation as it is running.
     */
    public JPanel createDynamicControls() {
        return null;
    }

    @Override
    protected String getStatusMessage() {
        return "frames/second=" + FormatUtil.formatNumber(getFrameRate());
    }


    // the next methods implement the unused methods of the optimizee interface.
    // Simulators must implement evaluateFitness ///

    public void doOptimization() {
       System.out.println("not implemented for this simulator");
    }

    public boolean evaluateByComparison() {
        return false;
    }


    /**
     * part of the Optimizee interface
     */
    public double getOptimalFitness() {
        return 0;
    }

    /** {@inheritDoc} */
    public double compareFitness(ParameterArray params1, ParameterArray params2) {
        return evaluateFitness(params1) - evaluateFitness(params2);
    }

    /**
     * *** implements the key method of the Optimizee interface
     *
     * evaluates the fitness.
     */
    public double evaluateFitness( ParameterArray params ) {
        assert false : "not implemented yet";
        return 0.0;
    }
}
