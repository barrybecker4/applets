/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.common;


import com.becker.game.common.board.Board;
import com.becker.game.common.player.Player;
import com.becker.game.common.player.PlayerList;

/**
 * This is the interface that all game controllers should implement.
 * Providing both an interface and an abstract implementation is a pattern
 * which maximizes flexibility in a framework. The interface defines the
 * public contract. The abstract class may be package private if we don't
 * want to expose it. Other classes may implement this interface without
 * extending the abstract base class.
 *
 * @see GameController for the abstract implementation of this interface
 * @see Board
 *
 * @author Barry Becker
 */
public interface IGameController {

    /**
     * @return the board representation object.
     */
    Board getBoard();

    /**
     * @return the class which shows the current state of the game board.
     * May be null if there is no user visible representation of the game.
     */
    GameViewable getViewer();

    /**
     * retract the most recently played move
     * @return  the move which was undone (null returned if no prior move)
     */
    Move undoLastMove();

    /**
     * this makes an arbitrary move (assumed valid) and
     * adds it to the move list.
     * For two player games, calling this does not keep track of weights or the search.
     * Its most common use is for browsing the game tree.
     *  @param m the move to play.
     */
    void makeMove( Move m );

    /**
     * @return the list of moves made so far.
     */
    MoveList getMoveList();

    /**
     * @return  the number of moves currently played.
     */
    int getNumMoves();

    /**
     * @return an array of the players playing the game
     */
    PlayerList getPlayers();

    /**
     *
     * @return the player who's turn it is now.
     */
    Player getCurrentPlayer();

    /**
     * a computer player makes the first move
     */
    void computerMovesFirst();

    /**
     *
     * @return true if the game is over.
     */
    boolean isDone();
}
