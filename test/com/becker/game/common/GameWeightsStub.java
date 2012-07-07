/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.common;


import com.becker.optimization.parameter.NumericParameterArray;
import com.becker.optimization.parameter.types.DoubleParameter;
import com.becker.optimization.parameter.types.Parameter;

/**
 * Stub GameWeights for testing
 *
 * @author Barry Becker
 */
public class GameWeightsStub extends GameWeights {

    /** not really used. */
    private static final Parameter[] PARAMS = {new DoubleParameter(1, 0, 10, "paramName")};


    public GameWeightsStub() {
        super(new NumericParameterArray(PARAMS));
    }

}

