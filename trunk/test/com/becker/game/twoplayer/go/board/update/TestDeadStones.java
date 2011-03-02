package com.becker.game.twoplayer.go.board.update;

import com.becker.game.twoplayer.go.GoTestCase;


/**
 * @author Barry Becker
 */
public class TestDeadStones extends GoTestCase {

    /** instance under test */
    private DeadStones deadStones = new DeadStones();

    public void testNoDeadStones() {

        assertEquals("Unexpected dead stones", 0, deadStones.getNumberOnBoard(true));
        assertEquals("Unexpected dead stones", 0, deadStones.getNumberOnBoard(false));
    }

    public void testIncBlackDeadStones() {

        deadStones.increment(true);
        assertEquals("Unexpected dead stones", 1, deadStones.getNumberOnBoard(true));

    }

    public void testIncBlackAndWhiteDeadStones() {

        deadStones.increment(false);
        deadStones.increment(true);
        deadStones.increment(false);

        assertEquals("Unexpected black dead stones", 1, deadStones.getNumberOnBoard(true));
        assertEquals("Unexpected white dead stones", 2, deadStones.getNumberOnBoard(false));
    }
}