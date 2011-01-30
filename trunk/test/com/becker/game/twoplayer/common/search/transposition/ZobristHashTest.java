package com.becker.game.twoplayer.common.search.transposition;

import com.becker.common.Location;
import com.becker.game.common.board.BoardPosition;
import com.becker.game.common.board.GamePiece;
import com.becker.game.twoplayer.common.TwoPlayerBoard;
import com.becker.game.twoplayer.common.TwoPlayerMove;
import com.becker.game.twoplayer.tictactoe.TicTacToeBoard;
import junit.framework.TestCase;

import java.util.Random;

/**
 * Verify expected hash key are generated based on board state.
 * @author Barry Becker
 */
public class ZobristHashTest extends TestCase {

    private static final Long CENTER_X_HASH = 428667830982598836L;
    private static final Long CORNER_O_HASH = -6688467811848818630L;

    private ZobristHash hash;
    private TwoPlayerBoard board;

    @Override
    public void setUp() {
        board = new TicTacToeBoard();
        createZHash(board);
    }

    public void testEmptyBoardHash() {
        assertEquals("Unexpected hashkey for empty board", new Long(0), hash.getKey());
    }

    public void testCenterXHash() {
        applyMoveToHash(2, 2, true);
        assertEquals("Unexpected hashkey for board with center X",
                CENTER_X_HASH, hash.getKey());
    }

    public void testCenterXBoard() {
        TwoPlayerMove m = TwoPlayerMove.createMove(new Location(2, 2), 0, new GamePiece(true));
        board.makeMove(m);

        hash = createZHash(board);
        assertEquals("Unexpected hashkey for board with center X",
                CENTER_X_HASH, hash.getKey());
    }

    public void testCornerOHash() {
        applyMoveToHash(1, 1, false);
        assertEquals("Unexpected hashkey for board with corner O",
                CORNER_O_HASH, hash.getKey());
    }

    public void testCornerOBoard() {
        TwoPlayerMove m = TwoPlayerMove.createMove(new Location(1, 1), 0, new GamePiece(false));
        board.makeMove(m);
        hash = createZHash(board);
        assertEquals("Unexpected hashkey for board with corner O",
                CORNER_O_HASH, hash.getKey());
    }

    public void testHashAfterUndo() {

        applyMoveToHash(2, 2, true);
        applyMoveToHash(2, 2, true);
        assertEquals("Unexpected hashkey for entry board after undo",
                new Long(0), hash.getKey());
    }


    public void testHashAfterTwoMoves() {

        applyMoveToHash(2, 2, true);
        applyMoveToHash(1, 1, false);
        assertEquals("Unexpected hashkey for board after 2 moves",
                new Long(-6422371760107745138L), hash.getKey());
    }

    public void testHashAfterTwoMovesThenUndoFirst() {

        applyMoveToHash(2, 2, true);
        applyMoveToHash(1, 1, false);

        applyMoveToHash(2, 2, true);
        assertEquals("Unexpected hashkey for board after 2 moves then an undo",
                CORNER_O_HASH, hash.getKey());        
    }

    public void testHashAfterTwoMovesThenUndoSecond() {

        applyMoveToHash(2, 2, true);
        applyMoveToHash(1, 1, false);

        applyMoveToHash(1, 1, false);
        assertEquals("Unexpected hashkey for board after 2 moves then an undo",
                CENTER_X_HASH, hash.getKey());
    }

    public void testHashAfterTwoMovesThenUndoMoveO() {

        board.makeMove(TwoPlayerMove.createMove(new Location(1, 1), 0, new GamePiece(false)));
        board.makeMove(TwoPlayerMove.createMove(new Location(2, 2), 0, new GamePiece(true)));

        board.undoMove();
        hash = createZHash(board);
        assertEquals("Unexpected hashkey for board after 2 moves then an undo move",
                CORNER_O_HASH, hash.getKey());
    }

    public void testHashAfterTwoMovesThenUndoMoveX() {

        board.makeMove(TwoPlayerMove.createMove(new Location(2, 2), 0, new GamePiece(true)));
        board.makeMove(TwoPlayerMove.createMove(new Location(1, 1), 0, new GamePiece(false)));

        board.undoMove();
        hash = createZHash(board);
        assertEquals("Unexpected hashkey for board after 2 moves then an undo move",
                CENTER_X_HASH, hash.getKey());
    }


    public void testHashAfterTwoMovesThenTwoUndos() {

        board.makeMove(TwoPlayerMove.createMove(new Location(2, 2), 0, new GamePiece(true)));
        board.makeMove(TwoPlayerMove.createMove(new Location(1, 1), 0, new GamePiece(false)));

        board.undoMove();
        board.undoMove();
        hash = createZHash(board);
        assertEquals("Unexpected hashkey for board after 2 moves then two undos",
                new Long(0), hash.getKey());
    }

    public void testHashesForDifferentBoardsStatesUnequal() {
        board.makeMove(TwoPlayerMove.createMove(new Location(2, 2), 0, new GamePiece(true)));
        board.makeMove(TwoPlayerMove.createMove(new Location(1, 1), 0, new GamePiece(false)));
        hash = createZHash(board);
        Long hash1 = hash.getKey();

        board = new TicTacToeBoard();
        board.makeMove(TwoPlayerMove.createMove(new Location(2, 2), 0, new GamePiece(true)));
        board.makeMove(TwoPlayerMove.createMove(new Location(3, 1), 0, new GamePiece(false)));
        hash = createZHash(board);

        assertFalse("Hash keys for different moves unexpectedly equal. both=" + hash1,
                hash1.equals(hash.getKey()));
    }

    public void testHashesForDifferentBoardsStatesUnequalUsingUndo() {
        board.makeMove(TwoPlayerMove.createMove(new Location(2, 2), 0, new GamePiece(true)));
        board.makeMove(TwoPlayerMove.createMove(new Location(1, 1), 0, new GamePiece(false)));
        hash = createZHash(board);
        Long hash1 = hash.getKey();

        board.undoMove();
        board.undoMove();
        board.makeMove(TwoPlayerMove.createMove(new Location(2, 2), 0, new GamePiece(true)));
        board.makeMove(TwoPlayerMove.createMove(new Location(3, 1), 0, new GamePiece(false)));
        hash = createZHash(board);

        assertFalse("Hash keys for different moves unexpectedly equal. both=" + hash1,
                hash1.equals(hash.getKey()));
    }

    private void applyMoveToHash(int row, int col, boolean player1) {
        GamePiece p = new GamePiece(player1);
        TwoPlayerMove m = TwoPlayerMove.createMove(new Location(row, col), 0, p);
        int stateIndex = board.getStateIndex(new BoardPosition(row, col, p));
        hash.applyMove(m, stateIndex);
    }

    private ZobristHash createZHash(TwoPlayerBoard board) {
        hash = new ZobristHash(board);
        hash.injectRandom(new Random(0));
        return hash;
    }
}
