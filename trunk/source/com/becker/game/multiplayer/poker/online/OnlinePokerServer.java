package com.becker.game.multiplayer.poker.online;

import com.becker.game.common.online.*;

/**
 * @author Barry Becker Date: May 14, 2006
 */
public class OnlinePokerServer extends OnlineGameServer {


    // must be unique for each game server, so that multiple servers can be used simultaneously.
    public static final int PORT = 4443;


    public OnlinePokerServer() {}

    public int getPort() {
        return PORT;
    }

    public static void main(String[] args) {
        OnlineGameServer frame = new OnlinePokerServer();

    }
}
