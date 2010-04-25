package com.becker.common.math.function;

import com.becker.common.math.Range;

/**
 * Defines interface for generic 1-1 function f(x).
 * @author Barry Becker
 */
public interface InvertibleFunction extends Function {

    /**
     * Given a y value (i.e. f(x)) return the corresponding x value.
     * Inverse of the above.
     * @param value some y value.
     * @return the x value for the specified y value
     */
    double getInverseValue(double value);
}