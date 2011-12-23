// Copyright by Barry G. Becker, 2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.optimization.parameter.types;

import com.becker.optimization.TestAnalyticFunctionProblem;
import com.becker.optimization.TestSevenElevenProblem;
import com.becker.optimization.TestTravelingSalesmanProblem;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Master test suite to test all aspects the optimization package.
 *
 * @author Barry Becker
 */

public class TestAll extends TestCase {

    public static Test suite() {
        TestSuite suite =  new TestSuite("All parameter type Tests");

        suite.addTestSuite(BooleanParameterTest.class);
        suite.addTestSuite(DoubleParameterTest.class);
        suite.addTestSuite(IntegerParameterTest.class);

        return suite;
    }
}