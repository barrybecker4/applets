/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.simulation.liquid.config;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test out all of my testable methods.
 *
 * @author Barry Becker
 */
public class TestAll extends TestCase {


    /**
     * @return all the junit test cases to run (in this class)
     */
    public static Test suite() {

        TestSuite suite =  new TestSuite("Config Tests");

        suite.addTestSuite(TestSource.class);
        //suite.addTest(TestSource.suite);

        return suite;
    }

}
