package com.becker.game.twoplayer.go.board;

import com.becker.common.Location;
import com.becker.game.twoplayer.go.GoTestCase;
import com.becker.game.twoplayer.go.board.elements.position.GoBoardPosition;
import com.becker.game.twoplayer.go.board.elements.position.GoStone;
import com.becker.game.twoplayer.go.board.move.GoMove;
import junit.framework.Assert;

/**
 * Verify that all the methods in GoBoard work as expected
 * @author Barry Becker
 */
public class TestGoBoard extends GoTestCase {

    private static final String PREFIX = "board/";
    
    /** verify that the right stones are captured by a given move. */
    public void testFindCaptures1() {
        verifyCaptures("findCaptures1", new Location(5, 6), 6);
    }

    public void testFindCaptures2() {
        verifyCaptures("findCaptures2", new Location(6, 6), 9);
    }

    public void testFindCaptures3() {
        verifyCaptures("findCaptures3", new Location(5, 4), 7);
    }

    public void testFindCaptures4() {
        verifyCaptures("findCaptures4", new Location(4, 8), 16);
    }

    public void testFindCaptures5() {
        verifyCaptures("findCaptures5", new Location(10, 2), 11);
    }

    private void verifyCaptures(String file, Location moveLocation, int expNnumCaptures) {

        restore(PREFIX  + file);

        GoMove move = new GoMove(moveLocation, 0, new GoStone(true));

        GoBoard board = (GoBoard)controller_.getBoard();

        int numWhiteStonesBefore = board.getNumStones(false);

        controller_.makeMove(move);

        int numWhiteStonesAfter = board.getNumStones(false);

        int actualNumCaptures = move.getNumCaptures();

        Assert.assertTrue("move.captures=" + actualNumCaptures + " expected "+expNnumCaptures,
                              actualNumCaptures == expNnumCaptures);
        int diffWhite = numWhiteStonesBefore - numWhiteStonesAfter;
        Assert.assertTrue("diff in num white stones ("+ diffWhite
                + ") not = numcaptures (" + expNnumCaptures
                + ')', diffWhite == expNnumCaptures);

        controller_.undoLastMove();
        // verify that all the captured stones get restored to the board
        numWhiteStonesAfter = board.getNumStones(false);
        Assert.assertTrue("numWhiteStonesBefore="+numWhiteStonesBefore
                + " not equal numWhiteStonesAfter="+numWhiteStonesAfter,
                numWhiteStonesBefore == numWhiteStonesAfter );
    }


    public void testCausedAtari1() {
        restore(PREFIX + "causedAtari1");
        GoBoard board = (GoBoard)controller_.getBoard();

        GoMove m = new GoMove(new Location(4, 4), 0, new GoStone(false));
        int numInAtari = m.causesAtari(board);
        Assert.assertTrue("numInAtri="+numInAtari+" expected="+4, numInAtari == 4);
    }


    public void testCausedAtari2() {
        restore(PREFIX + "causedAtari2");

        GoMove m = new GoMove(new Location(2, 12), 0, new GoStone(true));
        controller_.makeMove(m);
        GoBoard board = (GoBoard)controller_.getBoard();
        int numInAtari = m.causesAtari(board);
        Assert.assertTrue("numInAtri="+numInAtari+" expected="+12, numInAtari == 12);
    }


    public void testNumLiberties1() {
        verifyGroupLiberties("causedAtari2", 2, 9, 14,  2, 10, 2);
    }

    public void testNumLiberties2() {
        verifyGroupLiberties("numLiberties2", 1, 2, 17,   3, 6, 16);
    }

    public void testCopy() {
        GoBoard board = new GoBoard(3, 3, 0);
        Location center = new Location(2, 2);
        board.makeMove(new GoMove(center, 1, new GoStone(true)));
        GoBoard boardCopy = board.copy();


        assertEquals("Center positions not equal.", board.getPosition(center), boardCopy.getPosition(center));
    }

    private void verifyGroupLiberties(String file,
                                      int bRow, int bCol, int expectedBlackLiberties,
                                      int wRow, int wCol, int expectedWhiteLiberties) {
        restore(PREFIX + file);

        GoBoard board = (GoBoard)controller_.getBoard();

        GoBoardPosition pos = (GoBoardPosition)board.getPosition(bRow, bCol);
        int numGroupLiberties = pos.getGroup().getLiberties(board).size();
        Assert.assertTrue("numGroupLiberties="+numGroupLiberties+" expected="+expectedBlackLiberties,
                numGroupLiberties == expectedBlackLiberties);

        pos = (GoBoardPosition)board.getPosition(wRow, wCol);
        numGroupLiberties = pos.getGroup().getLiberties(board).size();
        Assert.assertTrue("numGroupLiberties="+numGroupLiberties+" expected="+expectedWhiteLiberties,
                numGroupLiberties == expectedWhiteLiberties);
    }

}
