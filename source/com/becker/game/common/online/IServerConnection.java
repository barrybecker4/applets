package com.becker.game.common.online;


import com.becker.game.common.player.Player;
import com.becker.game.common.player.PlayerAction;


/**
 * Opens a connection to the Game server from the client so we can talk to it.
 *
 * @author Barry Becker
 */
public interface IServerConnection
{
    /**
     * @return true if we have a live connection to the server.
     */
    boolean isConnected();

    void addOnlineChangeListener(OnlineChangeListener listener);

    void removeOnlineChangeListener(OnlineChangeListener listener);

    /**
     * @param cmd object to serialize over the wire.
     */
    void sendCommand(GameCommand cmd);

    /**
     * Player has entered the room with intent to play.
     */
    void enterRoom();

    /**
     * Tell the server to add another game table to the list that is available.
     * @param newTable  to add.
     */
    void addGameTable(OnlineGameTable newTable);

    /**
     * @param oldName  old player name
     * @param newName  new player name
     */
    void nameChanged(String oldName, String newName);

    /**
     * Tell the server to add player p to this table.
     * The server will look at the most recently added player to this table to
     * determine who was added.
     */
    void joinTable(Player p, OnlineGameTable table);

    /**
     * @param playerName  the player who has left the room.
     */
    void leaveRoom(String playerName);

    /**
     * @param action the action that the player has performed.
     */
    void playerActionPerformed(PlayerAction action);
}
