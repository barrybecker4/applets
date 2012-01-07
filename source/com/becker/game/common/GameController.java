/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.common;

import com.becker.game.common.board.Board;
import com.becker.game.common.online.IServerConnection;
import com.becker.game.common.player.PlayerAction;
import com.becker.game.common.player.PlayerList;


/**
 * This is an abstract base class for a Game Controller.
 * It contains the key logic for n player games.
 * Instance of this class process requests from the GameViewer.
 *
 *  @author Barry Becker
 */
public abstract class GameController
           implements IGameController {

    /** the board has the layout of the pieces. */
    private Board board_;

    /** Use this to draw directly to the ui while thinking (for debugging purposes) . */
    protected GameViewable viewer_;

    /** the list of players actively playing the game, in the order that they move. */
    private PlayerList players_;

    /** collections of game specific options.  They may be modified through the ui (see GameOptionsDialog)*/
    protected GameOptions gameOptions_;

    /**
     * Optional. Only present if we are online
     * this allows us to talk with the game server (if it is available). null if not
     */
    private IServerConnection serverConnection_;


    /**
     * Construct the game controller
     */
    public GameController() {
        GameContext.log( 3, " mem=" + Runtime.getRuntime().freeMemory() );
    }

    /**
     * optionally set a viewer for the controller.
     * @param viewer
     */
    public void setViewer(GameViewable viewer) {
       viewer_ = viewer;
    }

    /**
     * Return the game board back to its initial opening state
     */
    public void reset() {
        getBoard().reset();
    }


    public MoveList getMoveList() {
        return getBoard().getMoveList();
    }

    /**
     * @return the last move played.
     */
    public Move getLastMove() {
        return getMoveList().getLastMove();
    }

    /**
     * @return number of moves made so far.
     */
    public int getNumMoves() {
        return getBoard().getMoveList().getNumMoves();
    }

    /**
     * @return the class which shows the current state of the game board.
     * May be null if the viewer was never set.
     */
    public GameViewable getViewer() {
        return viewer_;
    }

    /**
     * If called before the end of the game it just reutrns 0 - same as it does in the case of a tie.
     * @return some measure of how overwhelming the win was. May need to negate based on which player one.
     */
    public int getStrengthOfWin() {
        return 0;
    }

    /**
     * @return the board representation object.
     */
    public final Board getBoard() {
        if (board_ == null) {
            board_ = createBoard();
        }
        return board_;
    }

    protected abstract Board createBoard();

    /**
     * Setup the initial game state.
     */
    protected abstract void initializeData();


    public void makeMove(Move move) {
        getBoard().makeMove(move);
    }

    /**
     * retract the most recently played move
     * @return  the move which was undone (null returned if no prior move)
     */
    public Move undoLastMove()
    {
        return getBoard().undoMove();
    }

    /**
     * save the current state of the game to a file
     * Use this version when an error occurred and you want to dump the state.
     * There is no default implementation (other than to say it is not implemented).
     * You must override if you want it to work.
     * @param fileName the file to save the state to
     * @param rte exception that occurred upon failure
     */
    public void saveToFile( String fileName, AssertionError rte ) {

        GameContext.log(0,  "Error: saveToFile(name, rte) not implemented yet for " + getClass().getName());
    }

    /**
     * Restore the current state of the game from a file.
     * @param fileName file to load from
     */
    public void restoreFromFile( String fileName) {
        GameContext.log(0,  "Error: restoreFromFile(" + fileName + ") not implemented yet" );
    }

    /**
     *
     * @return a list of the players playing the game (in the order that they move).
     */
    public PlayerList getPlayers() {
        return players_;
    }

    /**
     * Maybe use list of players rather than array.
     * @param players the players currently playing the game
     */
    public void setPlayers( PlayerList players ) {
       players_ = players;
       initializeData();
    }


    public void setOptions(GameOptions options) {
        gameOptions_ = options;
    }

    public abstract GameOptions getOptions();


    protected AbstractGameProfiler getProfiler() {
        return GameProfiler.getInstance();
    }        

    /**
     * You should probably check to see if online play is available before calling this.
     * @return a server connection if it is possible to get one.
     */
    public IServerConnection getServerConnection() {

        if (serverConnection_ == null) {
            serverConnection_ = createServerConnection();
        }
        return serverConnection_;
    }


    /**
     * Most games do not support online play so returning null is the default
     * @return the server connection if one can be created, else null.
     */
    protected IServerConnection createServerConnection() {
        GameContext.log(0, "Cannot create a server connection for "+ this.getClass().getName()
                           +". Online play not supported");
        return null;
    }

    /**
     *
     * @param cmd containing an action for one of the players
     *
    public void handleServerUpdate(GameCommand cmd) {
        // @@ need to put something here for.
        GameContext.log(2, "Need controller implementation for handleServerUpdate");
    }*/

    /**
     *
     * @return true if online pay is supported, and the server is available.
     */
    public abstract boolean isOnlinePlayAvailable();

    /**
     * If a derived class supports online play it must override this.
     * @return server port number
     */
    public int getServerPort() {
        return -1;
    }

    /**
     * Someday 2 player games should use actions rather than moves so
     * that they too can be run over the game server.
     * @param action some action
     */
    public void handlePlayerAction(PlayerAction action) {
        assert false : "handlePlayerAction not implemented for " + this.getClass().getName();
    }

}