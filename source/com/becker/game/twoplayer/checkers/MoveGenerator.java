package com.becker.game.twoplayer.checkers;

import com.becker.game.common.BoardPosition;
import com.becker.game.common.CaptureList;
import com.becker.game.common.GameContext;
import com.becker.game.common.MoveList;
import com.becker.game.twoplayer.common.TwoPlayerMove;
import com.becker.optimization.parameter.ParameterArray;

import java.util.LinkedList;
import java.util.List;

/**
 * Creates a list of possible next moves.
 *
 * @author Barry Becker
 */
public class MoveGenerator  {

    private CheckersSearchable searchable_;
    private CheckersBoard board_;

    private MoveList moveList_;
    private ParameterArray weights_;
    private boolean player1sPerspective_;

    /**
     * Construct the Checkers game controller.
     * @param searchable
     * @param moveList add the potential moves to this existing list.
     * @param weights to use.
     * @param player1sPerspective if true evaluate the moves from player1s perspective.
     */
    public MoveGenerator(CheckersSearchable searchable, MoveList moveList,
                         ParameterArray weights, boolean player1sPerspective) {
        searchable_ = searchable;
        board_ = (CheckersBoard) searchable_.getBoard();
        moveList_ = moveList;
        weights_ = weights;
        player1sPerspective_ = player1sPerspective;
    }


    /**
     * Find all the moves a piece p can make and insert them into moveList.
     *
     * @param p the piece to check.
     * @return the number of moves added.
     */
    public int addMoves( BoardPosition p, TwoPlayerMove lastMove) {

        int direction = -1;
        if ( p.getPiece().isOwnedByPlayer1() )
            direction = 1;

        int numMovesAdded = 0;
        int initialNumMoves = moveList_.size();
        // check left and right forward diagonals
        numMovesAdded += addMovesForDirection( p, direction, -1, lastMove );
        numMovesAdded += addMovesForDirection( p, direction, 1, lastMove);

        // if its a KING we need to check the other direction too
        CheckersPiece piece = (CheckersPiece) p.getPiece();
        if ( piece.isKing() ) {
            numMovesAdded += addMovesForKing(p, lastMove, direction, numMovesAdded, initialNumMoves);
        }
        return numMovesAdded;
    }


    /**
     * Find all the moves piece p can make in a given diagonal direction.
     *
     * @param pos the piece to check
     * @return the number of moves added
     */
    private int addMovesForDirection( BoardPosition pos,
                                      int rowInc, int colInc, TwoPlayerMove lastMove ) {
        BoardPosition next = board_.getPosition( pos.getRow() + rowInc, pos.getCol() + colInc );
        if ( next!=null && next.isUnoccupied() ) {
            addSimpleMove(pos, rowInc, colInc, lastMove);
            // only one move added
            return 1;
        }
        // if just a simple move was not possible, we check for jump(s)
        BoardPosition beyondNext = board_.getPosition( pos.getRow() + 2 * rowInc, pos.getCol() + 2 * colInc );
        if ( next!=null && next.isOccupied() &&
               (next.getPiece().isOwnedByPlayer1() != pos.getPiece().isOwnedByPlayer1()) &&
                beyondNext!=null && beyondNext.isUnoccupied()) {
            return addJumpMoves(pos, rowInc, lastMove, next, beyondNext);
        }
        return 0; // no moves added
    }


    private void addSimpleMove(BoardPosition pos, int rowInc, int colInc, TwoPlayerMove lastMove) {
        CheckersMove m;
        int val = 0;
        if ( lastMove != null ) {
            // then not the first move of the game
            val = lastMove.getValue();
        }
        m = CheckersMove.createMove( pos.getRow(), pos.getCol(),
                (pos.getRow() + rowInc), (pos.getCol() + colInc),
                null, val, pos.getPiece().copy() );

        // no need to evaluate it since there were no captures
        moveList_.add( m );
    }


    private int addJumpMoves(BoardPosition pos, int rowInc, TwoPlayerMove lastMove,
                             BoardPosition next, BoardPosition beyondNext) {
       CheckersMove m;
       CaptureList capture = new CaptureList();
       capture.add( next.copy() );
       // make it blank so a king doesn't loop back and take it again.
       // next.setPiece(null);
       m = CheckersMove.createMove( pos.getRow(), pos.getCol(), beyondNext.getRow(), beyondNext.getCol(),
               capture, lastMove.getValue(),  pos.getPiece().copy() );

       List<CheckersMove> jumps = findJumpMoves( beyondNext, rowInc, m, weights_ );
       moveList_.addAll( jumps );

       return jumps.size();
   }

    /**
     * Check to see if this jump requires additional jumps
     * If it does, we create a new move, because there could potentially be 2 jumps possible
     * from the last position.
     * @return number of additional jump moves added.
     */
    private int checkJumpMove( BoardPosition current,
                               CheckersMove m, int rowInc, int colInc,
                               List<CheckersMove> jumpMoves, ParameterArray weights ) {
        BoardPosition next = board_.getPosition( current.getRow() + rowInc, current.getCol() + colInc );
        BoardPosition beyondNext = board_.getPosition( current.getRow() + 2 * rowInc, current.getCol() + 2 * colInc );
        // if the adjacent square is an opponent's piece, and the space beyond it
        // is empty, and we have not already capture this peice, then take another jump.
        boolean opponentAdjacent =
                next!=null && next.isOccupied() && (next.getPiece().isOwnedByPlayer1() != m.isPlayer1());
        if ( opponentAdjacent
              && beyondNext!=null && beyondNext.isUnoccupied()
              && (m.captureList != null) && (!m.captureList.alreadyCaptured( next )) ) {
            // then there is another jump. We must take it.
            CheckersMove mm = (CheckersMove) m.copy();  // base it on the original jump
            mm.setToRow(beyondNext.getRow());
            mm.setToCol(beyondNext.getCol());
            mm.captureList.add( next.copy() );
            // next.setPiece(null); ?

            boolean justKinged = false;   // ?? may be superfluous
            GameContext.log( 2, "calling findJumpMoves on " +
                    beyondNext + " rowinc=" + rowInc + "length of capturelist=" + mm.captureList.size() );
            if ( (mm.getPiece().getType() == CheckersPiece.REGULAR_PIECE) &&
                    ((mm.isPlayer1() && mm.getToRow() == CheckersBoard.SIZE)
                    || (!mm.isPlayer1() && mm.getToRow() == 1)) ) {
                mm.kinged = true;
                justKinged = true;
                mm.getPiece().setType(CheckersPiece.KING);
                GameContext.log( 2, "KINGED: " + mm );
            }
            else
                mm.kinged = false;

            List<CheckersMove> list;
            // we cannot make more jumps if we just got kinged.
            if (!justKinged) {    // may be superfluous
                list = findJumpMoves( beyondNext, rowInc, mm, weights );
                assert ( list!=null );
                jumpMoves.addAll( list );
                return list.size();
            }
        }
        return 0;  // no additional move added
    }

    /**
     * Find all the possible jumps using the specified piece.
     * Remember that in checkers, all possible jumps in a row must be taken.
     * For example, you can not do just a double jump if a triple jump is possible.
     * This cannot return null since there is at least the first jump.
     * When jumping we remove the piece and add it to the captureList so they
     * won't be taken twice in the same move. At the end we return the captured
     * pieces to the board so the state is not change.
     * @return list of jump moves.
     */
    private List<CheckersMove> findJumpMoves( BoardPosition current,
                                      int rowInc, CheckersMove m,
                                      ParameterArray weights )  {
        List<CheckersMove> jumpMoves = new LinkedList<CheckersMove>();
        // if there are jumps beyond this we have to make them.
        // We have at least the current jump m.

        // once moreJumps becomes true we must add additional moves
        boolean moreJumps = false;

        // first check the forward moves
        if ( checkJumpMove( current, m, rowInc, -1, jumpMoves, weights ) > 0 )
            moreJumps = true;
        if ( checkJumpMove( current, m, rowInc, 1, jumpMoves, weights ) > 0 )
            moreJumps = true;

        // note you cannot continue making jumps from the point at which you are kinged.
        if ( m.getPiece().getType() == CheckersPiece.KING && !m.kinged ) {
            if ( checkJumpMove( current, m, -rowInc, -1, jumpMoves, weights ) > 0 )
                moreJumps = true;
            if ( checkJumpMove( current, m, -rowInc, 1, jumpMoves, weights ) > 0 )
                moreJumps = true;
        }

        if ( !moreJumps ) { // base case of recursion
            // we can finally add the move after we evaluate its worth
            board_.makeMove( m );
            m.setValue(searchable_.worth( m, weights, player1sPerspective_ ));
            board_.undoMove();

            jumpMoves.add( m );

            return jumpMoves;
        }
        return jumpMoves;
    }

    /**
     * @return number of king moves added.
     */
    private int addMovesForKing(BoardPosition p, TwoPlayerMove lastMove,
                                int direction, int numMovesAdded, int initialNumMoves) {
        int numKingMoves = 0;
        numKingMoves += addMovesForDirection( p, -direction, -1, lastMove);
        numKingMoves += addMovesForDirection( p, -direction, 1, lastMove);

        // we also need to verify that we are not cycling over previous moves (not allowed).
        // check moves in the list against the move 4 moves back if the same, we must remove it
        // we can skip if there were captures, since captures cannot be undone.
        int numMoves = searchable_.getNumMoves();

        if ( numMoves - 4 > 1 ) {
            CheckersMove moveToCheck = (CheckersMove) searchable_.getMoveList().get( numMoves - 4 );
            if ( moveToCheck.captureList == null ) {
                int i = 0;

                while ( i < numKingMoves ) {
                    CheckersMove m = (CheckersMove)moveList_.get( initialNumMoves + numMovesAdded + i );
                    GameContext.log( 1, "lastMove="+ lastMove);
                    assert ( m.isPlayer1() == moveToCheck.isPlayer1()):
                            "player ownership not equal comparing \n"+m+" with \n"+moveToCheck;
                    if ( m.captureList == null &&
                            m.getToRow() == moveToCheck.getToRow() &&
                            m.getToCol() == moveToCheck.getToCol() &&
                            m.getFromRow() == moveToCheck.getFromRow() &&
                            m.getFromCol() == moveToCheck.getFromCol() &&
                            m.getPiece().getType() == moveToCheck.getPiece().getType() ) {
                        GameContext.log(0, "found a cycle. new score = " + m.getValue() +
                                " old score=" + moveToCheck.getValue() + " remove move= " + m );
                        moveList_.remove( m );
                        numKingMoves--;
                        break;
                    }
                    else {
                        i++;
                    }
                }
            }
        }
        return numKingMoves;
    }
}
