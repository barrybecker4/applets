/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.twoplayer.common;

import com.barrybecker4.game.common.GameContext;
import com.barrybecker4.game.common.Move;
import com.barrybecker4.game.common.board.Board;
import com.barrybecker4.game.common.board.BoardPosition;
import com.barrybecker4.game.common.board.GamePiece;

import java.util.List;

/**
 * Defines the structure of the board and the pieces on it.
 * Each BoardPosition can contain a piece.
 *
 * @author Barry Becker
 */
public abstract class TwoPlayerBoard extends Board {

    /** default constructor */
    public TwoPlayerBoard() {}

    /** copy constructor */
    public TwoPlayerBoard(TwoPlayerBoard board) {
        super(board);
    }

    public abstract TwoPlayerBoard copy();

    /**
     * given a move specification, execute it on the board
     * This places the players symbol at the position specified by move.
     * @param move the move to make, if possible.
     * @return false if the move is illegal.
     */
    @Override
    protected boolean makeInternalMove( Move move ) {

        TwoPlayerMove m = (TwoPlayerMove)move;
        if ( !m.isPassOrResignation() ) {
            BoardPosition pos = getPosition(m.getToLocation());
            assert(m.getPiece() != null) : "move's piece was null :" + m;
            pos.setPiece(m.getPiece());
            GamePiece piece = pos.getPiece();
            assert (piece != null):
                    "The piece was " + piece + ". Moved to " + m.getToRow() + ", " + m.getToCol();
            if ( GameContext.getDebugMode() > 0 ) {
                piece.setAnnotation( Integer.toString(getMoveList().getNumMoves()) );
            }
        }
        return true;
    }

    /**
     * @param moves list of moves to make all at once.
     */
    protected void makeMoves(List<Move> moves) {
        for (Move move : moves) {
            makeMove(move);
        }
    }

    /**
     * Num different states. E.g. black queen.
     * This is used primarily for the Zobrist hash. You do not need to override if yo udo not use it.
     * @return number of different states this position can have.
     */
    public abstract int getNumPositionStates();


    /**
     * The index of the state for this position.
     * Perhaps this would be better abstract.
     * @return The index of the state for this position.
     */
    public int getStateIndex(BoardPosition pos) {
        assert pos.isOccupied() : "this should only be called on occupied positions";
        return pos.getPiece().isOwnedByPlayer1()? 1 : 2;
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder(1000);
        bldr.append("\n");
        int nRows = getNumRows();
        int nCols = getNumCols();
        TwoPlayerMove m = (TwoPlayerMove) getMoveList().getLastMove();

        for ( int i = 1; i <= nRows; i++ )   {
            boolean followingLastMove = false;
            for ( int j = 1; j <= nCols; j++ ) {
                BoardPosition pos = this.getPosition(i,j);
                if (pos.isOccupied()) {
                    if (pos.getLocation().equals(m.getToLocation())) {
                        bldr.append("[").append(pos.getPiece()).append("]");
                        followingLastMove = true;
                    }
                    else  {
                        bldr.append(followingLastMove?"":" ").append(pos.getPiece());
                        followingLastMove = false;
                    }
                }
                else {
                    bldr.append(followingLastMove?"":" " + "_");
                }
            }
            bldr.append("\n");
        }
        return bldr.toString();
    }
}