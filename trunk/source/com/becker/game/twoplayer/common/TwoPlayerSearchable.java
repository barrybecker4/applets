package com.becker.game.twoplayer.common;

import com.becker.game.common.*;
import com.becker.game.common.board.BoardPosition;
import com.becker.game.common.player.PlayerList;
import com.becker.game.twoplayer.common.search.Searchable;
import com.becker.game.twoplayer.common.search.options.SearchOptions;
import com.becker.game.twoplayer.common.search.transposition.ZobristHash;
import com.becker.optimization.parameter.ParameterArray;

import static com.becker.game.twoplayer.common.search.strategy.SearchStrategy.WINNING_VALUE;

/**
 * For searching two player games
 *
 * @author Barry Becker
 */
public abstract class TwoPlayerSearchable implements Searchable {

    protected final TwoPlayerBoard board_;
    protected final SearchOptions options_;
    protected final PlayerList players_;
    protected final MoveList moveList_;

    /** helps to find the best moves. */
    protected final BestMoveFinder bestMoveFinder_;

    /** Used to generate hashkeys. */
    private final ZobristHash hash;


    public TwoPlayerSearchable(final TwoPlayerBoard board,  PlayerList players, SearchOptions options) {

        board_ = board;
        moveList_ = board.getMoveList();
        players_ = players;
        options_ = options;

        hash = new ZobristHash(board_);
        bestMoveFinder_ = new BestMoveFinder(getSearchOptions().getBestMovesSearchOptions());
    }

    public TwoPlayerSearchable(TwoPlayerSearchable searchable) {

        this((TwoPlayerBoard)searchable.getBoard().copy(), (PlayerList)searchable.players_.clone(), searchable.options_);

        ////
        /*assert searchable.getHashKey().equals(getHashKey()) :
                 "key=" + searchable.getHashKey() +  " for\n"
                + searchable.getBoard() + " different from key=" + getHashKey() + " for\n" + getBoard()
                         +"\n b1 moves="+ searchable.getBoard().getMoveList() + "\nb2 moves=" + getBoard().getMoveList();
        */

    }

    public TwoPlayerBoard getBoard() {
        return board_;
    }

    public int getNumMoves() {
        return moveList_.getNumMoves();
    }

    public MoveList getMoveList() {
        return moveList_;
    }

    public SearchOptions getSearchOptions() {
        return options_;
    }

    protected AbstractGameProfiler getProfiler() {
        return GameProfiler.getInstance();
    }

    /**
     * @param m the move to play.
     */
    public final void makeInternalMove( TwoPlayerMove m ) {

        TwoPlayerMove lastMove = (TwoPlayerMove)(moveList_.getLastMove());
        if (moveList_.getNumMoves() > 0) {
            assert(lastMove.isPlayer1() != m.isPlayer1()):
                    "can't go twice in a row m=" + m + "\n getLastMove()=" + lastMove + "\n movelist = " + moveList_;
        }

        getBoard().makeMove( m );

        BoardPosition pos = getBoard().getPosition(m.getToLocation());
        //assert pos != null : "pos was null at " + m.getToLocation() + " pass="+  m.isPassingMove();
        hash.applyMove(m, getBoard().getStateIndex(pos));
    }

    /**
     * takes back the most recent move.
     * @param m  move to undo
     */
    public final void undoInternalMove( TwoPlayerMove m ) {
        TwoPlayerMove lastMove = (TwoPlayerMove)moveList_.getLastMove();
        assert m.equals(lastMove) : "The move we are trying to undo ("+m+") in list="
                + moveList_+" was not equal to the last move ("+lastMove+"). all move=" + getBoard().getMoveList();
        hash.applyMove(m, getBoard().getStateIndex(getBoard().getPosition(m.getToLocation())));
        getBoard().undoMove();
    }


    /**
     * Evaluates from player 1's perspective
     * @return an integer value for the worth of the move.
     *  must be between -SearchStrategy.WINNING_VALUE and SearchStrategy.WINNING_VALUE.
     */
    public abstract int worth( Move lastMove, ParameterArray weights);

    /**
     * given a move, determine whether the game is over.
     * If recordWin is true, then the variables for player1/2HasWon can get set.
     *  sometimes, like when we are looking ahead we do not want to set these.
     * @param lastMove the move to check. If null then return true. This is typically the last move played.
     * @param recordWin if true then the controller state will record wins
     */
    public boolean done( TwoPlayerMove lastMove, boolean recordWin ) {
        // the game can't be over if no moves have been made yet.
        if (moveList_.getNumMoves() == 0) {
            return false;
        }
        if (moveList_.getNumMoves() > 0 && lastMove == null) {
            GameContext.log(0, "Game is over because there are no more moves");
            return true;
        }
        if (players_.anyPlayerWon()) {
            GameContext.log(0, "Game over because one of the players has won.");
            return true;
        }

        boolean won = (Math.abs( lastMove.getValue() ) >= WINNING_VALUE);

        if ( won && recordWin ) {
            if ( lastMove.getValue() >= WINNING_VALUE )
                players_.getPlayer1().setWon(true);
            else
                players_.getPlayer2().setWon(true);
        }
        boolean maxMovesExceeded = moveList_.getNumMoves() >= getBoard().getMaxNumMoves();

        return (maxMovesExceeded || won);
    }

    /**
     * @return true if the specified move caused one or more opponent pieces to become jeopardized
     */
    public boolean inJeopardy( TwoPlayerMove lastMove, ParameterArray weights) {
        return false;
    }

    /**
     * @return get a hash key that represents this board state (with negligibly small chance of conflict)
     */
    public Long getHashKey() {
        return hash.getKey();
    }
}