/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.twoplayer.common;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.game.common.GameContext;
import com.barrybecker4.game.common.player.Player;
import com.barrybecker4.game.common.player.PlayerList;
import com.barrybecker4.game.twoplayer.common.search.options.SearchOptions;
import com.barrybecker4.game.twoplayer.common.search.transposition.HashKey;
import com.barrybecker4.game.twoplayer.common.search.transposition.ZobristHash;
import com.barrybecker4.optimization.parameter.ParameterArray;

import static com.barrybecker4.game.twoplayer.common.search.strategy.SearchStrategy.WINNING_VALUE;

/**
 * For searching two player games
 *
 * @author Barry Becker
 */
public abstract class TwoPlayerSearchable extends AbstractSearchable {

    protected final TwoPlayerBoard board_;
    protected final PlayerList players_;

    /** helps to find the best moves. */
    protected final BestMoveFinder bestMoveFinder_;

    /** Used to generate hashkeys. */
    protected final ZobristHash hash;


    /**
     * Constructor.
     */
    public TwoPlayerSearchable(final TwoPlayerBoard board,  PlayerList players) {

        super(board.getMoveList());
        board_ = board;
        players_ = players;

        hash = new ZobristHash(board_);
        bestMoveFinder_ = new BestMoveFinder(getSearchOptions().getBestMovesSearchOptions());
    }

    /**
     * Copy constructor.
     */
    public TwoPlayerSearchable(TwoPlayerSearchable searchable) {

        this(searchable.getBoard().copy(), (PlayerList)searchable.players_.clone());
    }

    public TwoPlayerBoard getBoard() {
        return board_;
    }

    /**
     * @param move the move to play.
     */
    public void makeInternalMove(TwoPlayerMove move) {

        TwoPlayerMove lastMove = (TwoPlayerMove)(moveList_.getLastMove());
        if (moveList_.getNumMoves() > 0) {
            // @@ we hit this a lot in the tests when running through gradle (because assertions are on). Should fix.
            if (lastMove.isPlayer1() == move.isPlayer1())
            {
                GameContext.log(0, "Should not move twice in a row m=" + move
                        + "\n getLastMove()=" + lastMove + "\n movelist = " + moveList_);
                //assert(lastMove.isPlayer1() != move.isPlayer1()):
                //    "can't go twice in a row m=" + move + "\n getLastMove()=" + lastMove + "\n movelist = " + moveList_;
            }
        }

        getBoard().makeMove(move);

        if (move.isPassingMove())  {
            hash.applyPassingMove();
        } else {
            Location loc = move.getToLocation();
            hash.applyMove(loc, getBoard().getStateIndex(getBoard().getPosition(loc)));
        }
    }

    /**
     * takes back the most recent move.
     * @param move move to undo
     */
    public void undoInternalMove( TwoPlayerMove move) {
        TwoPlayerMove lastMove = (TwoPlayerMove)moveList_.getLastMove();
        assert move.equals(lastMove) : "The move we are trying to undo ("+ move +") in list="
                + moveList_+" was not equal to the last move ("+lastMove+"). all move=" + getBoard().getMoveList();

        Location loc = move.getToLocation();

        if (!move.isPassingMove()) {
            hash.applyMove(loc, getBoard().getStateIndex(getBoard().getPosition(loc)));
        }

        getBoard().undoMove();
    }

    /**
     * Evaluates from player 1's perspective
     * @return an integer value for the worth of the move.
     *  must be between -SearchStrategy.WINNING_VALUE and SearchStrategy.WINNING_VALUE.
     */
    public abstract int worth( TwoPlayerMove lastMove, ParameterArray weights);


    @Override
    public SearchOptions getSearchOptions() {
        //System.out.println("getCurrentPlayer()=" + getCurrentPlayer() +" opts = "
        // + ((TwoPlayerPlayerOptions) getCurrentPlayer().getOptions()).getSearchOptions());
        return ((TwoPlayerPlayerOptions) getCurrentPlayer().getOptions()).getSearchOptions();
    }


    /**
     * Given a move, determine whether the game is over.
     * If recordWin is true, then the variables for player1/2HasWon can get set.
     *  sometimes, like when we are looking ahead we do not want to set these.
     * @param move the move to check. If null then return true. This is typically the last move played.
     * @param recordWin if true then the controller state will record wins
     */
    public boolean done( TwoPlayerMove move, boolean recordWin ) {

        // the game can't be over if no moves have been made yet.
        if (moveList_.getNumMoves() == 0) {
            return false;
        }
        if (players_.anyPlayerWon()) {
            GameContext.log(0, "Game over because one of the players has won.");
            return true;
        }
        if (moveList_.getNumMoves() > 0 && move == null) {
            Player currentPlayer = getCurrentPlayer();
            GameContext.log(0, "Game is over because there are no more moves for player " + currentPlayer);
            if (recordWin) {
                currentPlayer.setWon(true);
            }
            return true;
        }

        int absValue = Math.abs(move.getValue());
        boolean won = (absValue >= WINNING_VALUE);

        if ( won && recordWin ) {
            if ( move.getValue() >= WINNING_VALUE )
                players_.getPlayer1().setWon(true);
            else
                players_.getPlayer2().setWon(true);
        }
        boolean maxMovesExceeded = moveList_.getNumMoves() >= getBoard().getMaxNumMoves();

        return (maxMovesExceeded || won);
    }

    /**
     * @return the player who's turn it is to move next.
     */
    private Player getCurrentPlayer()  {
        TwoPlayerMove move =  (TwoPlayerMove) moveList_.getLastMove();
        return (move==null || !move.isPlayer1()) ?  players_.getPlayer1() : players_.getPlayer2();
    }

    /**
     * @return true if the specified move caused one or more opponent pieces to become jeopardized
     */
    public boolean inJeopardy( TwoPlayerMove move, ParameterArray weights) {
        return false;
    }

    /**
     * @return get a hash key that represents this board state (with negligibly small chance of conflict)
     */
    public HashKey getHashKey() {
        return hash.getKey();
    }
}