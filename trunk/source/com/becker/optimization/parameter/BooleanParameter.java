package com.becker.optimization.parameter;

import com.becker.optimization.parameter.redistribution.BooleanRedistribution;
import com.becker.optimization.parameter.ui.BooleanParameterWidget;
import com.becker.optimization.parameter.ui.ParameterWidget;

/**
 *  represents a boolean parameter to an algorithm
 *
 *  @author Barry Becker
 */
public class BooleanParameter extends IntegerParameter
{

    public BooleanParameter( boolean val,  String paramName )
    {
        super(val?1:0, 0, 1, paramName);    
    }
    
    public static BooleanParameter createSkewedParameter(
                                                           boolean value, String paramName, 
                                                           double percentTrue)  {
        BooleanParameter param = new BooleanParameter(value, paramName);
        param.setRedistributionFunction(new BooleanRedistribution(percentTrue));    
        return param;
    }

    @Override
    public Parameter copy()
    {
        BooleanParameter p =  new BooleanParameter( (Boolean)getNaturalValue(), getName() );
        p.setRedistributionFunction(redistributionFunction_);
        return p;
    }
    
    public Object getNaturalValue() {
        // true if getValue is odd.
        return Boolean.valueOf(((int)getValue() % 2) == 1);
   }

    public Class getType() {
        return boolean.class; 
    }
    
    public ParameterWidget createWidget(ParameterChangeListener listener) {
        return new BooleanParameterWidget(this, listener);
    }
}
