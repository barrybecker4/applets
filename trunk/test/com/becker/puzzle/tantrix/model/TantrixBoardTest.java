// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.puzzle.tantrix.model;

import com.becker.common.geometry.Location;
import com.becker.common.math.MathUtil;
import junit.framework.TestCase;
import static com.becker.puzzle.tantrix.model.TantrixTstUtil.*;

/**
 * @author Barry Becker
 */
public class TantrixBoardTest extends TestCase {

    /** instance under test */
    TantrixBoard board;

    @Override
    public void setUp() {
        MathUtil.RANDOM.setSeed(1);
    }

    public void testBoardConstruction() {
        board = place3UnsolvedTiles();
        TilePlacement expLastPlaced =
                new TilePlacement(TILES.getTile(3), new Location(22, 21), Rotation.ANGLE_180);
        assertEquals("Unexpected last tile placed", expLastPlaced, board.getLastTile());
        assertEquals("Unexpected edge length", 3, board.getEdgeLength());
        assertEquals("Unexpected primary path color",
                TILES.getTile(1).getPrimaryColor(), board.getPrimaryColor());
        assertEquals("All the tiles should have been placed",
                0, board.getUnplacedTiles().size());
    }

    public void test3TilesIsNotSolved() {
        board = place3UnsolvedTiles();
        System.out.println(board);
        assertFalse("Unexpectedly solved", board.isSolved());
    }

    public void test3TilesIsSolved() {
        board = place3SolvedTiles();
        System.out.println(board);
        assertTrue("Unexpectedly not solved", board.isSolved());
    }


    public void test4TilesIsNotSolved() {
        board = place4UnsolvedTiles();
        assertFalse("Unexpectedly solved", board.isSolved());
    }

    public void test4TilesIsSolved() {
        board = place4SolvedTiles();
        assertTrue("Unexpectedly not solved", board.isSolved());
    }

}