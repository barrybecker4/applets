package com.becker.game.common.board;

import com.becker.game.common.Move;

/**
 * This is the interface that all game boards should implement.
 * We assume that the board is composed of a 2D array of BoardPositions.
 *
 * Providing both an interface and an abstract implementation is a pattern
 * which maximizes flexibility in a framework. The interface defines the
 * public contract. The abstract class may be package private if we don't
 * want to expose it. Or other classes may implement this interface without
 * extending the abstract base class.
 *
 * @see Board for the base implementation of this interface
 * @author Barry Becker
 */
public interface IBoard {

    /**
     *  Reset the board to its initial starting state.
     */
    void reset();

    /**
     *  Change the dimensions of this game board.
     *  Note: we must call reset after changing the size, since the original game board will now be invalid.
     *
     *  @param numRows the new number of rows for the board to have.
     *  @param numCols the new number of cols for the board to have.
     */
    void setSize( int numRows, int numCols );

     /**
      * @return  retrieve the number of rows that the board has.
      */
     int getNumRows();

     /**
      * @return  retrieve the number of cols that the board has.
      */
     int getNumCols();

     /**
      *  There can be no more than this many moves in a game
      * In many games, it is not just the number of squares.
      * the main purpose of this function is to avoid cases where
      * a game can go on forever by making repeat moves.
      *
      * @return upper limit on the number of moves that the board can support
      */
    int getMaxNumMoves();

    /**
     * returns null if there is no game piece at the position specified.
     * @return the piece at the specified location. Returns null if there is no piece there.
     */
    BoardPosition getPosition( int row, int col );

    /**
     * given a move specification, execute it on the board
     * This places the players symbol at the position specified by move.
     *
     * @param move the move to make, if possible.
     * @return false if the move is illegal.
     */
    boolean makeMove( Move move );

    /**
     * Allow reverting a move so we can step backwards in time.
     * Board is returned to the exact state it was in before the last move was made.
     * @return the move that was just undone.
     */
    Move undoMove();

    /**
     * @return true if the specified position is within the bounds of the board
     */
    boolean inBounds( int r, int c );

    /**
     * We should be able to create a deep copy of ourselves
     * @return deep copy of the board.
     */
    IBoard copy();
}
