package com.becker.game.twoplayer.go.board.elements.string;

import com.becker.game.twoplayer.go.board.elements.group.TestGoGroup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Barry Becker
 */
public class TestAll {

    private TestAll() {}

    /**
     * @return all the junit test cases to run (in this class)
     */
    public static Test suite() {

        TestSuite suite =  new TestSuite("Go string Tests");

        suite.addTestSuite(TestGoString.class);

        return suite;
    }

}

