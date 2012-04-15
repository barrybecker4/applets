/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.go;

import com.becker.game.common.GameContext;
import com.becker.game.twoplayer.common.TwoPlayerMove;
import com.becker.game.twoplayer.go.board.GoSearchable;
import com.becker.game.twoplayer.go.board.move.GoMove;

/**
 * @author Barry Becker
 */
public class TestScoring extends GoTestCase {

    private static final String PATH_PREFIX = "scoring/";

    /** give some leeway on the territory estimate since its a heuristic. */
    private static final double TOLERANCE = 5;

    public void testScoring1() {
        checkScoring("problem_score1", 0, 0, 0, 0, 74, 57, -7);
    }

    public void testScoring55a() { //                 bt, wt, finalWorth
        checkScoring("problem_score55a", 0, 0, 0, 7,  16,  1,   400);
    }

    public void testScoring55b() { //                 bt, wt,  finalWorth
        checkScoring("problem_score55b", 0, 2, 0, 6,   14, 2,  1604);  // 0, 2, 0, 6, 14, 0);
    }

    public void testScoring2() { //                bt, wt,  finalWorth
        checkScoring("problem_score2", 0, 0, 3, 0, 58, 60,  -138);
    }

    public void testScoringIdentPosition1a() { //           bt, wt,  finalWorth
        checkScoring("problem_identPosition1a", 0, 0, 4, 0,  5, 7, -94); //13, 12, -94);
    }

    public void testScoringIdentPosition1b() {
        checkScoring("problem_identPosition1b", 0, 0, 4, 0, 5, 7, -94); //13, 12, -94);
    }

    public void testScoringIdentPosition2a() {
        checkScoring("problem_identPosition2a", 0, 0, 2, 1, 4, 5, 19);
    }

    public void testScoringIdentPosition2b() {
        checkScoring("problem_identPosition2b", 0, 1, 2, 1, 4, 5, 596);
    }


    /**
     *
     * @param scoringProblem determines problem file to load
     * @param expectedBlackCapturesSoFar number of black stones that were captured by white.
     * @param expectedWhiteCapturesSoFar number of white stones that were captured by black.
     * @param expectedDeadBlackOnBoard number of black stones that that are dead on the board at the end.
     * @param expectedDeadWhiteOnBoard number of white stones that that are dead on the board at the end.
     * @param expectedBlackTerr amount of black territory (excluding dead white stones in the territory)
     * @param expectedWhiteTerr amount of white territory (excluding dead black stones in the territory)
     */
    private void checkScoring(String scoringProblem,
                        int expectedBlackCapturesSoFar, int expectedWhiteCapturesSoFar,
                        int expectedDeadBlackOnBoard, int expectedDeadWhiteOnBoard,
                        int expectedBlackTerr, int expectedWhiteTerr, int expectedFinalWorth) {

        updateLifeAndDeath(PATH_PREFIX + scoringProblem);

        int blackTerrEst = controller_.getFinalTerritory(true);
        int whiteTerrEst = controller_.getFinalTerritory(false);
        GoSearchable searchable = (GoSearchable) controller_.getSearchable();
        int numBlackCaptures = searchable.getNumCaptures(true);
        int numWhiteCaptures = searchable.getNumCaptures(false);
        int numDeadBlack = searchable.getNumDeadStonesOnBoard(true);
        int numDeadWhite = searchable.getNumDeadStonesOnBoard(false);

        GameContext.log(0, "CaptureCounts :          black = " + numBlackCaptures + "   white = "+ numWhiteCaptures);
        GameContext.log(0, "Territory: black = " + blackTerrEst + "   white = "+ whiteTerrEst);
        assertEquals(
                "Unexpected number of black captures. Expected ",
                 expectedBlackCapturesSoFar, numBlackCaptures);
        assertEquals(
                "Unexpected number of white captures. Expected ",
                 expectedWhiteCapturesSoFar, numWhiteCaptures);
        assertEquals(
                "Unexpected number of dead black stones on board",
                 expectedDeadBlackOnBoard, numDeadBlack);
        assertEquals(
                "Unexpected number of dead white stones on board.",
                expectedDeadWhiteOnBoard, numDeadWhite);
        assertTrue("The black territory estimate ("+ blackTerrEst +") was not close to "+ expectedBlackTerr,
                          withinBounds(blackTerrEst, expectedBlackTerr));
        assertTrue("The white territory estimate ("+ whiteTerrEst +") was not close to "+ expectedWhiteTerr,
                          withinBounds(whiteTerrEst, expectedWhiteTerr));
        // see if a given move is in jeopardy
        //controller_.inJeopardy();

        int finalWorth = searchable.worth((TwoPlayerMove) searchable.getMoveList().getLastMove(),
                                     controller_.getComputerWeights().getDefaultWeights());
        assertEquals("Unexpected worth for final move.", expectedFinalWorth, finalWorth);

    }

    protected void updateLifeAndDeath(String problemFile) {
        GameContext.log(0, "finding score for " + problemFile + " ...");
        restore(problemFile);

        // force dead stones to be updated by calling done with resignation move.
        controller_.getSearchable().done(GoMove.createResignationMove(true), true);
    }

    private static boolean withinBounds(int actual, int expected) {
        return (actual > (expected - TOLERANCE) && actual < (expected + TOLERANCE));
    }
}