// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.puzzle.tantrix.model;

import com.becker.puzzle.sudoku.model.TestBoard;
import com.becker.puzzle.sudoku.model.TestBoardUpdater;
import com.becker.puzzle.sudoku.model.TestCell;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Barry Becker
 */
public class TestAll extends TestCase {

    public static Test suite() {

        TestSuite suite =  new TestSuite("All Tantrix model Tests");

        suite.addTestSuite(TantrixBoardTest.class);
        suite.addTestSuite(BorderFinderTest.class);
        suite.addTestSuite(MoveGeneratorTest.class);

        return suite;
    }
}