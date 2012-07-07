// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.game.twoplayer.pente.analysis.differencers;

import com.becker.game.twoplayer.pente.PenteBoard;
import com.becker.game.twoplayer.pente.analysis.Direction;
import com.becker.game.twoplayer.pente.pattern.Patterns;


/**
 * Verify that we correctly evaluate patterns on the board.
 *
 * @author Barry Becker
 */
public class DownDiagonalDifferencerTest extends ValueDifferencerTst  {

    @Override
    protected ValueDifferencer createDifferencer(PenteBoard board, Patterns patterns) {
        return differencerFactory.createValueDifferencer(Direction.DOWN_DIAGONAL);
    }

    public void testBlank() {
            verifyLine(2, 1, "___XO_");
        }

    public void testLeftEdge() {
        verifyLine(4, 1, "_OXOO_");
    }


    public void testBottomLeftCorner() {
        verifyLine(10, 1, "_");
    }

    public void testBottomRightCorner() {
        verifyLine(10, 8, "_X_O__");
    }

    public void testTopLeftCorner() {
        verifyLine(1, 1, "____X_");
    }

    public void testTopRightCorner() {
        verifyLine(1, 8, "_");
    }


    public void testPlayer1() {
        verifyLine(1, 4, "X_O__");
    }

    public void testPlayer2() {
        verifyLine(2, 4, "_O__X_");
    }

    public void testPlayer2Single() {
        verifyLine(5, 2, "_OXOO_O");
    }

    public void testPlayer1Single() {
        verifyLine(9, 2, "_X_");
    }

    public void testPlayer1Surrounded() {
        verifyLine(6, 3, "_OXOO_O");
    }

    public void testPlayer2Surrounded() {
        verifyLine(7, 4, "_OXOO_O");
    }

    public void testPlayer2Edge() {
        verifyLine(10, 7, "OXOO_O");
    }

    public void testPlayer2AmongFriends() {
        verifyLine(8, 7, "__XO_O_");
    }
}