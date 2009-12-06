package com.becker.optimization.parameter;

import com.becker.common.math.MathUtil;
import com.becker.optimization.parameter.redistribution.DiscreteRedistribution;
import com.becker.optimization.parameter.ui.DoubleParameterWidget;
import com.becker.optimization.parameter.ui.ParameterWidget;

import java.util.Random;

/**
 *  represents an integer parameter in an algorithm
 *
 *  @author Barry Becker
 */
public class IntegerParameter extends AbstractParameter
{
    public IntegerParameter(int val, int minVal, int maxVal, String paramName )
    {
        super((double)val, (double)minVal, (double)maxVal, paramName, true);    
    }

    public static IntegerParameter createDiscreteParameter(
                                                            int val, int minVal, int maxVal, String paramName, 
                                                            int[] discreteSpecialValues, double[] specialValueProbabilities) {
       IntegerParameter param = new IntegerParameter(val, minVal, maxVal, paramName);
       int numValues = (maxVal - minVal + 1);
        param.setRedistributionFunction(
                new DiscreteRedistribution(numValues, discreteSpecialValues, specialValueProbabilities));    
        return param;
    }
    
    public Parameter copy()
    {
        IntegerParameter p =  new IntegerParameter( (int)getValue(), (int)getMinValue(), (int)getMaxValue(), getName() );
        p.setRedistributionFunction(redistributionFunction_);
        return p;
    }
    
    @Override
    public void randomizeValue(Random rand) {
        value_ = getMinValue() + rand.nextDouble() * (getRange() + 1.0);
    }
    
    @Override
    public void tweakValue(double r, Random rand)
    {      
        if (isOrdered()) {
            super.tweakValue(r, rand);
        }
        else {
            double rr = rand.nextDouble();
            if (rr < r) {
                // if not ordered, then just randomize with probability r
                randomizeValue(rand) ;
            }
        }
   }
    
    protected boolean isOrdered() {
        return true;
    }

    @Override
    public void setValue(double value) {
       
        this.value_ = value;
        // if there is a redistribution function, we need to apply its inverse.
        if (redistributionFunction_ != null) {
            double v = (value - minValue_) / (getRange() + 1.0);
            this.value_= 
                    minValue_ + (getRange() + 1.0) *redistributionFunction_.getInverseFunctionValue(v);
        }
    }
    
    @Override
    public double getValue() {
        double value = value_; 
        if (redistributionFunction_ != null) {
            double v = (value_ - minValue_) / (getRange() + 1.0);
            double rv = redistributionFunction_.getFunctionValue(v);
            value = rv * (getRange() + (1.0 - MathUtil.EPS)) + minValue_;
            //System.out.println("stored="+v + " redistr rv="+rv + " getValue="+value);
        }
        return (int) value; 
    }  

    @Override
    public boolean isIntegerOnly() {
        return true;
    }
    
    public Object getNaturalValue() {
        return Integer.valueOf((int)this.getValue());
    }
    
    @Override
    public Class getType() {
        return int.class; 
    }
    
    public ParameterWidget createWidget(ParameterChangeListener listener) {
        return new DoubleParameterWidget(this, listener);
    }
}
