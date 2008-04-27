package com.becker.game.multiplayer.poker.online;

import com.becker.game.common.*;
import com.becker.game.multiplayer.common.MultiGamePlayer;
import com.becker.game.multiplayer.common.online.SurrogatePlayer;
import com.becker.game.multiplayer.poker.player.*;
import com.becker.game.multiplayer.poker.*;
import com.becker.game.multiplayer.common.online.ui.*;

import java.awt.*;

/**
 * @author Barry Becker Date: May 13, 2006
 */
public class OnlinePokerTable extends MultiPlayerOnlineTable {
    
    private static final long serialVersionUID = 1;

    public OnlinePokerTable(String name, Player initialPlayer, GameOptions options) {
        super(name, initialPlayer, options);
    }

    /**
     * add robot player i to the table.
     * Since this is on the client, robots are surrogate players.
     */
    protected void addRobotPlayer(int i) {
      
        Color newColor = MultiGamePlayer.getNewPlayerColor(getPlayers());

        PokerOptions options = (PokerOptions) getGameOptions();
        String name = "Robot " + (i+1);
        PokerPlayer robot = PokerRobotPlayer.getRandomRobotPlayer(name, options.getInitialCash(), newColor);
        //SurrogatePlayer sp = new SurrogatePlayer(robot);
        this.addPlayer(robot);
    }

}
