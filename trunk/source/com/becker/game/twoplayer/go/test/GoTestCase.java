package com.becker.game.twoplayer.go.test;

import junit.framework.TestCase;
import junit.framework.Assert;
import com.becker.game.common.GameContext;
import com.becker.game.twoplayer.go.GoController;
import com.becker.game.twoplayer.go.GoMove;
import com.becker.game.twoplayer.common.search.SearchStrategy;

import java.util.List;
import java.io.File;
import java.io.FilenameFilter;


public class GoTestCase extends TestCase {


    // moved all test cases here so they are not included in the jar and do not need to be searched
    private static final String EXTERNAL_TEST_CASE_DIR =
            GameContext.getHomeDir() +"/test/go/cases/";

    //private static final String TEST_CASE_DIR =
    //        GameContext.getHomeDir() +"/source/"  +
    //        GameContext.GAME_ROOT  + "twoplayer/go/test/cases/";

    private static final String SGF_EXTENSION = ".sgf";

    GoController controller_;

    /**
     * common initialization for all go test cases.
     */
    protected void setUp() {
        // this will load the resources for the specified game.
        GameContext.loadGameResources("go", "com.becker.game.twoplayer.go.ui.GoPanel");
        GameContext.setDebugMode(0);

        controller_ = new GoController(13, 13, 0);

        //controller_.allPlayersComputer();
        controller_.setAlphaBeta(true);
        controller_.setLookAhead(4);
        controller_.setPercentageBestMoves(40);
        //controller_.setQuiescence(true); // take stoo long if on
        controller_.setSearchStrategyMethod(SearchStrategy.MINIMAX);

    }

    protected void restore(String problemFile) {
        controller_.restoreFromFile(EXTERNAL_TEST_CASE_DIR + problemFile + SGF_EXTENSION);
    }


    /**
     * @param pattern
     * @return all the files matching the supplied pattern in the specified directory
     */
    protected String[] getFilesMatching(String directory, String pattern) {

        File dir =  new File(EXTERNAL_TEST_CASE_DIR + directory);
        assert (dir.isDirectory());

        //System.out.println("pattern = "+pattern+ "dir="+dir.getAbsolutePath());
        FilenameFilter filter = new MyFileFilter(pattern);
        String[] list = dir.list(filter);

        return list;
    }

    protected GoMove getNextMove(String problemFile, boolean blackPlays) {

        System.out.println("finding next move for "+problemFile+" ...");
        restore(problemFile);
        controller_.requestComputerMove( true, blackPlays );

        GoMove m = (GoMove) controller_.getBoard().getLastMove();
        System.out.println("got " + m);
        return m;
    }

    protected void checkExpected(GoMove m, int row, int col) {

        Assert.assertTrue("Was expecting "+ row +", "+ col +", but instead got "+m,
                          m.getToRow() == row && m.getToCol() == col);

    }


    void updateLifeAndDeath(String problemFile) {
        System.out.println("finding score for "+problemFile+" ...");
        restore(problemFile);

        // must check the worth of the board once to update the scoreContributions fo empty spaces.
        List moves = controller_.getMoveList();
        double w = controller_.worth((GoMove)moves.get(moves.size()-3), controller_.getDefaultWeights(), true);    
        controller_.updateLifeAndDeath();   // this updates the groups and territory as well.
    }


    protected void tearDown() {

    }


    private class MyFileFilter implements FilenameFilter {

        private String pattern_;

        public MyFileFilter(String pattern) {
            pattern_ = pattern;
        }

        public boolean accept(File dir, String name) {
            return (name.contains(pattern_));
        }
    }

}