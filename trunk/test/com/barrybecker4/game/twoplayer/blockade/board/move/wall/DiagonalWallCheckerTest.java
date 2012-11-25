// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.game.twoplayer.blockade.board.move.wall;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.game.common.GameContext;
import com.barrybecker4.game.common.board.GamePiece;
import com.barrybecker4.game.twoplayer.blockade.BlockadeTestCase;
import com.barrybecker4.game.twoplayer.blockade.board.BlockadeBoard;
import com.barrybecker4.game.twoplayer.blockade.board.BlockadeBoardPosition;
import com.barrybecker4.game.twoplayer.blockade.board.move.BlockadeMove;
import com.barrybecker4.game.twoplayer.blockade.board.move.MoveGenerator;

import java.util.List;

/**
 * @author Barry Becker
 */
public class DiagonalWallCheckerTest extends BlockadeTestCase {

    /** instance under test */
    DiagonalWallChecker checker;
    BlockadeBoard board;

    BlockadeBoardPosition topLeft;
    BlockadeBoardPosition topRight;
    BlockadeBoardPosition bottomLeft;

    BlockadeBoardPosition topLeftLeft;
    BlockadeBoardPosition topRightRight;
    BlockadeBoardPosition topTopLeft;
    BlockadeBoardPosition bottomBottomLeft;

    /**
     * Common initialization for all test cases.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        board = new BlockadeBoard(7, 5);
        checker = new DiagonalWallChecker(board);

        topLeft = board.getPosition(2, 3);
        topRight = board.getPosition(2, 4);
        bottomLeft = board.getPosition(3, 3);

        topTopLeft = board.getPosition(1, 3);
        topLeftLeft = board.getPosition(2, 2);
        topRightRight = board.getPosition(2, 5);
        bottomBottomLeft = board.getPosition(4, 3);
    }

    public void testCheckWhenNoWallsOnBoard() {

        BlockadeWallList expWalls = new BlockadeWallList();
        expWalls.add(new BlockadeWall(topLeft, topRight));
        expWalls.add(new BlockadeWall(topLeft, bottomLeft));

        verifyWalls(expWalls);
    }

    public void  testCheckWhenLeftWallOnBoard() {

        board.addWall(new BlockadeWall(topLeftLeft, topLeft));

        BlockadeWallList expWalls = new BlockadeWallList();
        expWalls.add(new BlockadeWall(topLeft, bottomLeft));
        expWalls.add(new BlockadeWall(topRight, topRightRight));

        verifyWalls(expWalls);
    }

    public void testCheckWhenRightWallOnBoard() {

        board.addWall(new BlockadeWall(topRight, topRightRight));

        BlockadeWallList expWalls = new BlockadeWallList();
        expWalls.add(new BlockadeWall(topLeft, bottomLeft));
        expWalls.add(new BlockadeWall(topLeftLeft, topRight));

        verifyWalls(expWalls);
    }

    public void testCheckWhenTopWallOnBoard() {

        board.addWall(new BlockadeWall(topTopLeft, topLeft));

        BlockadeWallList expWalls = new BlockadeWallList();
        expWalls.add(new BlockadeWall(topLeft, topRight));
        expWalls.add(new BlockadeWall(bottomLeft, bottomBottomLeft));

        verifyWalls(expWalls);
    }

    public void testCheckWhenBottomWallOnBoard() {

        board.addWall(new BlockadeWall(bottomLeft, bottomBottomLeft));

        BlockadeWallList expWalls = new BlockadeWallList();
        expWalls.add(new BlockadeWall(topLeft, topRight));
        expWalls.add(new BlockadeWall(topTopLeft, topLeft));

        verifyWalls(expWalls);
    }

    public void testCheckWhenLeftAndBottomWalls() {

        board.addWall(new BlockadeWall(topLeftLeft, topLeft));
        board.addWall(new BlockadeWall(bottomLeft, bottomBottomLeft));

        BlockadeWallList expWalls = new BlockadeWallList();
        expWalls.add(new BlockadeWall(topRight, topRightRight));
        expWalls.add(new BlockadeWall(topTopLeft, topLeft));

        verifyWalls(expWalls);
    }

    public void testCheckWhenLeftAndTopWalls() {

        board.addWall(new BlockadeWall(topLeftLeft, topLeft));
        board.addWall(new BlockadeWall(topTopLeft, topLeft));

        BlockadeWallList expWalls = new BlockadeWallList();
        expWalls.add(new BlockadeWall(topRight, topRightRight));
        expWalls.add(new BlockadeWall(bottomLeft, bottomBottomLeft));

        verifyWalls(expWalls);
    }

    public void testCheckWhenRightAndTopWalls() {

        board.addWall(new BlockadeWall(topTopLeft, topLeft));
        board.addWall(new BlockadeWall(topRight, topRightRight));

        BlockadeWallList expWalls = new BlockadeWallList();
        expWalls.add(new BlockadeWall(topLeftLeft, topLeft));
        expWalls.add(new BlockadeWall(bottomLeft, bottomBottomLeft));

        verifyWalls(expWalls);
    }

    public void testCheckWhenRightAndBotttomWalls() {

        board.addWall(new BlockadeWall(topRight, topRightRight));
        board.addWall(new BlockadeWall(bottomLeft, bottomBottomLeft));

        BlockadeWallList expWalls = new BlockadeWallList();
        expWalls.add(new BlockadeWall(topTopLeft, topLeft));
        expWalls.add(new BlockadeWall(topLeftLeft, topLeft));

        verifyWalls(expWalls);
    }

    private void verifyWalls(BlockadeWallList expWalls) {
        BlockadeWallList walls = checker.checkWalls(topLeft, topRight, bottomLeft);
        assertEquals("Unexpected walls", expWalls, walls);
    }
}
