/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.multiplayer.common;

import com.barrybecker4.game.common.GameContext;
import com.barrybecker4.game.common.GameController;
import com.barrybecker4.game.common.GameOptions;
import com.barrybecker4.game.common.Move;
import com.barrybecker4.game.common.MoveList;
import com.barrybecker4.game.common.board.Board;
import com.barrybecker4.game.common.online.server.IServerConnection;
import com.barrybecker4.game.common.online.server.connection.ServerConnection;
import com.barrybecker4.game.common.player.Player;
import com.barrybecker4.game.common.player.PlayerAction;
import com.barrybecker4.game.multiplayer.common.online.SurrogateMultiPlayer;
import com.barrybecker4.game.multiplayer.common.ui.MultiGameViewer;
import com.barrybecker4.optimization.parameter.ParameterArray;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for multi player game controllers.
 *
 * Online play should work like this:
 *  <ul>
 *    <li>One of the human players creates a table with certain parameters that define
 *     the game to be played.  </li>
 *   <li>Each client and the server create an instance of the game controller.
 *     On each client there is a human player representing the player on that client,
 *     and surrogate player objects representing all the other human and robot players. </li>
 *   <li>The server will have surrogates for all the human players, and robot players for the computer players. </li>
 *   <li>When it is a given players turn, they specify their action. </li>
 *     That action is sent in a message to the server. The server then broadcasts
 *     the response (in this case the players action) to all OnlineChangeListeners.
 *     Since all the surrogates for that player (one on the server and one on each client
 *     except the one representing that actual player) are OnlineChangeListeners,
 *     they will get the event and know to set the action so that it can be retrieved by
 *     that controller.
 *   <li>When a surrogate is asked for its action, it blocks until it actually received the action
 *     in that response message from the server.  </li>
 *  </ul>
 * @author Barry Becker
 */
public abstract class MultiGameController extends GameController {

    protected int currentPlayerIndex_;

    /** there is a different starting player each round */
    protected int startingPlayerIndex_ = 0;

    /** the ith play in a given round */
    protected int playIndex_ = 0;

    /** size of the board. Assumed to be a grid. Probably should be abstracted to an options class */
    private Dimension size;

    private List<PlayerAction> recentRobotActions_;

    /** Constructor */
    protected  MultiGameController()  {
        size = new Dimension(0, 0);
    }

    /**
     *  Construct the game controller given an initial board size
     */
    protected MultiGameController(int nrows, int ncols ) {
        size = new Dimension(nrows, ncols);
        initializeData();
    }

    @Override
    protected Board createBoard() {
        return createTable(size.width, size.height);
    }

    protected abstract Board createTable(int nrows, int ncols);


    /**
     * Return the game board back to its initial opening state
     */
    @Override
    public void reset() {
        super.reset();
        initializeData();
    }

    @Override
    protected void initializeData() {
        startingPlayerIndex_ = 0;
        playIndex_ = 0;
        currentPlayerIndex_ = 0;
        recentRobotActions_ = new ArrayList<PlayerAction>();
        initPlayers();
    }

    @Override
    public GameOptions getOptions() {
        if (gameOptions_ == null) {
            gameOptions_ = createOptions();
        }
        return gameOptions_;
    }

    protected abstract GameOptions createOptions();

    /**
     * by default we start with one human and one robot player.
     */
    protected abstract void initPlayers();


    /**
     * @return the player who's turn it is now.
     */
    public MultiGamePlayer getCurrentPlayer() {
        return (MultiGamePlayer) getPlayers().get(currentPlayerIndex_);
    }

    public void computerMovesFirst() {
        MultiGameViewer viewer  = (MultiGameViewer) this.getViewer();
        viewer.doComputerMove(getCurrentPlayer());
    }

    /**
     * @return the server connection.
     */
    @Override
    protected IServerConnection createServerConnection() {

        return new ServerConnection(getServerPort());
    }

    @Override
    public boolean isOnlinePlayAvailable() {
        return getServerConnection().isConnected();
    }

    @Override
    public int getServerPort() {
        assert false : "online game play not supported for " + this.getClass().getName();
        return 0;
    }

    /**
     * Apply the action from the client for the associated player.
     * @param action
     */
    @Override
    public void handlePlayerAction(PlayerAction action) {

        for (Player p : getPlayers()) {
            if (p.getActualPlayer().getName().equals(action.getPlayerName())) {
                ((MultiGamePlayer)p.getActualPlayer()).setAction(action);
                doAdvanceToNextPlayer();
            }
        }
    }

    /**
     * Advance to the next player turn in order.
     * Do so in a separate thread so the UI is not blocked.
     */
    public void advanceToNextPlayer() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                doAdvanceToNextPlayer();
            }
        });
    }

    protected void doAdvanceToNextPlayer() {

        MultiGameViewer pviewer = (MultiGameViewer) getViewer();
        pviewer.refresh();

        // show message when done.
        if (!isDone()) {
            advanceToNextPlayerIndex();
            Player currentPlayer = getCurrentPlayer();
            if (currentPlayer.isSurrogate()) {
                GameContext.log(0, "about to do surrogate move for " + currentPlayer
                        + " in controller=" + this + " in thread=" + Thread.currentThread().getName());
                pviewer.doSurrogateMove((SurrogateMultiPlayer)currentPlayer);
            }
            else if (!currentPlayer.isHuman()) {
                GameContext.log(0, "now moving for computer player  = " + currentPlayer);
                pviewer.doComputerMove(currentPlayer);
            }
        }
        // fire game changed event
        pviewer.sendGameChangedEvent(null);
    }

    public void addRecentRobotAction(PlayerAction action) {
        recentRobotActions_.add(action);
    }

    /** get all the actions since last asked and clear them out */
    public List<PlayerAction> getRecentRobotActions() {
        List<PlayerAction> actions = new ArrayList<PlayerAction>();
        actions.addAll(recentRobotActions_);
        recentRobotActions_.clear();
        return actions;
    }

    /**
     * @return the player with the best Trivial hand
     */
    public abstract MultiGamePlayer determineWinner();

    /**
     * make it the next players turn
     * @return the index of the next player
     */
    protected abstract int advanceToNextPlayerIndex();

    /**
     *  @return the player that goes first.
     *
    public Player getFirstPlayer() {
        return getPlayers().get(startingPlayerIndex_);
    }*/

    protected MultiGamePlayer getPlayer(int index) {
        return (MultiGamePlayer) getPlayers().get(index);
    }

    /**
     *  Statically evaluate the board position
     *  @return the lastMoves value modified by the value add of the new move.
     *   a large positive value means that the move is good from the specified players viewpoint
     */
    protected double worth( Move lastMove, ParameterArray weights ) {
        return lastMove.getValue();
    }

    /*
     * generate all possible next moves.
     * impossible for this game.
     */
    public MoveList generateMoves( Move lastMove, ParameterArray weights) {
        return new MoveList();
    }

    /**
     * return any moves that result in a win
     */
    public List generateUrgentMoves( Move lastMove, ParameterArray weights) {
        return null;
    }
}
