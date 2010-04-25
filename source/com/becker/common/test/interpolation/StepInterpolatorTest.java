package com.becker.common.test.interpolation;

import com.becker.common.math.interplolation.Interpolator;
import com.becker.common.math.interplolation.LinearInterpolator;
import com.becker.common.math.interplolation.StepInterpolator;

/**
 * @author Barry Becker
 */
public class StepInterpolatorTest extends InterpolatorTstBase {


    @Override
    protected Interpolator createInterpolator(double[] func) {
          return new StepInterpolator(func);
    }

    @Override
    protected double getExpectedSimpleInterpolation0_1() {
        return 0.0;
    }

    @Override
    protected  double getExpectedSimpleInterpolation0_9() {
        return 2.0;
    }


    @Override
    protected double getExpectedTypicalInterpolation0_1() {
        return 1.0;
    }

    @Override
    protected double getExpectedTypicalInterpolation0_4() {
        return 2.296;
    }

    @Override
    protected double getExpectedTypicalInterpolation0_5() {
        return 2.5;
    }

    @Override
    protected double getExpectedTypicalInterpolation0_9() {
        return 4.0;
    }


    @Override
    protected double getExpectedOnePointInterpolation() {
        return 1.0;
    }


    @Override
    protected double getExpectedInterpolation2Points0_1() {
        return 0.0;
    }
}