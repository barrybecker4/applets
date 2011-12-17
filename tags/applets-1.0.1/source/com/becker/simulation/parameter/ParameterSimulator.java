/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.simulation.parameter;

import com.becker.common.math.function.InvertibleFunction;
import com.becker.common.math.function.LinearFunction;
import com.becker.optimization.parameter.Parameter;
import com.becker.simulation.common.ui.DistributionSimulator;
import com.becker.simulation.common.ui.SimulatorOptionsDialog;
import com.becker.ui.renderers.HistogramRenderer;

/**
 * to see what kind of distribution of numbers you get.
 * 
 * @author Barry Becker
 */
public class ParameterSimulator extends DistributionSimulator {

    private static final int NUM_DOUBLE_BINS = 1000;

    // init with some default
    private Parameter parameter_ = ParameterDistributionType.values()[0].getParameter();

    private boolean showRedistribution_ = true;

    public ParameterSimulator() {
        super("Parameter Histogram");
        initHistogram();
    }

    public void setParameter(Parameter parameter) {
        parameter_ = parameter;
        initHistogram();
    }
    
    public void setShowRedistribution(boolean show) {
        showRedistribution_ = show;
    }
    
    public boolean isShowRedistribution() {
        return showRedistribution_;
    }

    @Override
    protected void initHistogram() {
        if (parameter_.isIntegerOnly()) {
            data_ = new int[(int)parameter_.getRange() + 1];
            histogram_ = new HistogramRenderer(data_);
        }
        else {
            data_ = new int[NUM_DOUBLE_BINS];

            InvertibleFunction xFunc = new LinearFunction(NUM_DOUBLE_BINS/parameter_.getRange(), -parameter_.getMinValue());
            histogram_ = new HistogramRenderer(data_, xFunc);
        }                
    }

    @Override
    protected SimulatorOptionsDialog createOptionsDialog() {
         return new ParameterOptionsDialog( frame_, this );
    }
    
    @Override
    protected double getXPositionToIncrement() {
        if (showRedistribution_) {
            parameter_.randomizeValue(random_);  
        }
        else {
            double scale = parameter_.isIntegerOnly()?  parameter_.getRange() + 1.0 : parameter_.getRange();
            double v = parameter_.getMinValue() + random_.nextDouble() * scale;
            parameter_.setValue(v);
        }

        return parameter_.getValue();
        /*
        int xpos;
        if (parameter_.isIntegerOnly()) {
            xpos = (int) parameter_.getValue();
        }
        else {
            xpos =  (int)((NUM_DOUBLE_BINS-1) * parameter_.getValue() / parameter_.getRange());
        }
        return xpos;  */
    }

    public static void main( String[] args )
    {
        final ParameterSimulator sim = new ParameterSimulator();
        runSimulation(sim);
    }
}


