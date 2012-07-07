/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.tictactoe;


import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Master test suite to test all aspects of my tic-tac-toe program.
 *
 * @author Barry Becker
 */
public class TestAll  {


    private TestAll() {}

    public static Test suite() {

        TestSuite suite =  new TestSuite("All TicTacToe Tests");

        suite.addTestSuite(MoveEvaluationTest.class);
        suite.addTestSuite(TicTacToeSearchableTest.class);
        suite.addTestSuite(MiniMaxStrategyTest.class);
        suite.addTestSuite(NegaMaxStrategyTest.class);
        suite.addTestSuite(NegaScoutStrategyTest.class);
        suite.addTestSuite(ComputerVsComputerMiniMaxTest.class);

        return suite;
    }
}