/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.go.board.update;

import com.becker.common.geometry.Location;
import com.becker.game.twoplayer.go.GoTestCase;
import com.becker.game.twoplayer.go.board.GoBoard;
import com.becker.game.twoplayer.go.board.analysis.group.GroupAnalyzer;
import com.becker.game.twoplayer.go.board.analysis.group.GroupAnalyzerMap;
import com.becker.game.twoplayer.go.board.elements.position.GoBoardPosition;
import com.becker.game.twoplayer.go.board.elements.position.GoStone;
import com.becker.game.twoplayer.go.board.move.GoMove;


/**
 * @author Barry Becker
 */
public class TestPostMoveUpdater extends GoTestCase {

    private static final String PREFIX = "board/update/";

    GoBoard board;

    public void testTrivialMove() {

        board = new GoBoard(5, 0);

        // a black move on a virgin board.
        Location location = new Location(2, 2);
        GoMove move = new GoMove(location, 0, new GoStone(true));
        board.makeMove(move);  // the board already has its own update. Should we use that or inject?


        UpdateStats stats = new UpdateStats(0, 4, 1, 1, 0, 1, true);
        verifyStats(stats);
    }


    public void testMoveWhichJoinsTwoStrings() {
        UpdateStats stats = new UpdateStats(0, 10, 4, 1, 0, 3, true);
        verifyPostMove("join_two_strings", new Location(4, 4), stats);
    }

    public void testMoveWhichJoinsThreeStrings() {
        UpdateStats stats = new UpdateStats(0, 13, 7, 1, 0, 2, true);
        verifyPostMove("join_three_strings", new Location(5, 5), stats);
    }

    public void testMoveWhichJoinsFourStrings() {
        UpdateStats stats = new UpdateStats(0, 15, 9, 1, 0, 2, true);
        verifyPostMove("join_four_strings", new Location(5, 5), stats);
    }

    public void testTigerMouthCapture() {
        UpdateStats stats = new UpdateStats(1, 4, 1, 4, 1, 2, true);
        verifyPostMove("tiger_mouth_capture", new Location(6, 5), stats);
    }

    public void testJoinTwoGroups() {
        UpdateStats stats = new UpdateStats(0, 4, 1, 5, 0, 2, true);
        verifyPostMove("join_two_groups", new Location(5, 8), stats);
    }

    private void verifyPostMove(String file, Location moveLocation, UpdateStats stats) {

        restore(PREFIX  + file);

        GoMove move = new GoMove(moveLocation, 0, new GoStone(true));

        board = (GoBoard)controller_.getBoard();
        controller_.makeMove(move);

        verifyStats(stats);
    }

    /** probably too many things to verify for a unit test. */
    private void verifyStats(UpdateStats stats) {
        GoMove move = (GoMove) board.getMoveList().getLastMove();
        GoBoardPosition pos = (GoBoardPosition) board.getPosition(move.getToLocation());
        GroupAnalyzer groupAnalyzer = new GroupAnalyzer(pos.getGroup(), new GroupAnalyzerMap());

        assertEquals("Unexpected captures",
                stats.expCaptures, move.getCaptures().size());
        assertEquals("Unexpected captures for black",
                stats.expCaptures, board.getNumCaptures(false));
        assertEquals("Unexpected number of liberties for the string",
                stats.expStringLiberties, pos.getString().getNumLiberties(board));
        assertEquals("Unexpected number of stones in string",
                stats.expNumStonesInString, pos.getString().size());
        assertEquals("Unexpected number of strings in group",
                stats.expNumStringsInGroup, pos.getGroup().size());
        assertEquals("Unexpected number eyes in group",
                stats.expNumEyesInGroup, groupAnalyzer.getEyes(board).size());
        assertEquals("Unexpected number of groups",
                stats.expGroupsOnBoard, board.getGroups().size());
        assertEquals("The group is new so should be valid",
                stats.expValid, groupAnalyzer.isValid());
    }
}