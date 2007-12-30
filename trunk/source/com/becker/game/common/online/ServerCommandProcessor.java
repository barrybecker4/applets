package com.becker.game.common.online;

import com.becker.common.util.Util;
import com.becker.game.common.*;
import com.becker.common.*;
import com.becker.game.multiplayer.common.online.SurrogatePlayer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles the processing of all commands send to the online game server.
 *
 * @author Barry Becker Date: Sep 16, 2006
 */
public class ServerCommandProcessor {

    /** Maintain a list of game tables. */
    private OnlineGameTableList tables_;

    /** Maintain the master game state on the server. */
    private GameController controller_;

    /**
     * Create the online game server to serve all online clients.
     */
    public ServerCommandProcessor(String gameName) {

        createController(gameName);
        tables_ = new OnlineGameTableList();
    }

    public OnlineGameTableList getTables() {
        return tables_;
    }

    /**
     * Factory method to create the game controller.
     */
    private void createController(String gameName) {
        String controllerClass = PluginManager.getInstance().getPlugin(gameName).getControllerClass();
        Class c = ClassLoaderSingleton.loadClass(controllerClass);
        try {
            controller_ = (GameController) c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return controller_.getServerPort();
    }

    /**
     * Update our internal game table list given the cmd from the client.
     * @param cmd to process. The command that the player has issued.
     * @return the response command(s) to send to all the clients.
     */
    public List<GameCommand> processCommand(GameCommand cmd) {
        
        List<GameCommand> responses = new LinkedList<GameCommand>();
        boolean useUpdateTable = true;
        
        switch (cmd.getName()) {
            case ENTER_ROOM :
                //System.out.println("Entering room.");
                break;
            case LEAVE_ROOM :
                System.out.println("Player "+cmd.getArgument()+" is now leaving the room.");
                tables_.removePlayer((String) cmd.getArgument());
                break;
            case ADD_TABLE :
                addTable((OnlineGameTable) cmd.getArgument());
                break;
            case JOIN_TABLE :
                GameCommand startCmd = joinTable((OnlineGameTable) cmd.getArgument());
                if (startCmd != null)
                    responses.add(startCmd);
                break;
            case CHANGE_NAME :
                String[] names = ((String)cmd.getArgument()).split(GameCommand.CHANGE_TO);
                if (names.length >1) {
                    changeName(names[0], names[1]);
                }
                break;
            case UPDATE_TABLES :
                break;
            case CHAT_MESSAGE :
                //System.out.println("chat message=" + cmd.getArgument());
                useUpdateTable = false;
                responses.add(cmd);
                break;
        }
        
        if (useUpdateTable) {
            GameCommand response = new GameCommand(GameCommand.Name.UPDATE_TABLES, getTables());
            responses.add(0, response);  // add as first command in response.
        }
        
        return responses;
    }

    /**
     *
     * @param table
     */
    private void addTable(OnlineGameTable table) {

        // if the table we are adding has the same name as an existing table change it to something unique
        String uniqueName = verifyUniqueName(table.getName());
        table.setName(uniqueName);
        // if the player at this new table is already sitting at another table,
        // remove him from the other tables, and delete those other tables if no one else is there.
        assert(table.getPlayers().size() >= 1):
            "It is expected that when you add a new table there is at least one player at it" +
            " (exactly one human owner and 0 or more robots).";
        tables_.removePlayer(table.getOwner());
        tables_.add(table);
    }

    /**
     * Get the most recently added human player from table and have them join the table with the same name.
     * If there is a table now ready to paly after this change, then start it.
     */
    private GameCommand joinTable(OnlineGameTable table) {

        GameCommand response = null;
        // if the player at this new table is already sitting at another table,
        // remove him from the other tables(s) and delete those other tables (if no one else is there).
        Player p = table.getNewestHumanPlayer();
        //System.out.println("in join table on the server p="+p);
        tables_.removePlayer(p);
        tables_.join(table.getName(), p);
        OnlineGameTable tableToStart = tables_.getTableReadyToPlay(p.getName());
        if (tableToStart != null) {
            startGame(tableToStart);
            response = new GameCommand(GameCommand.Name.START_GAME,  tableToStart);
        }
        return response;
    }

    /**
     * Change the players name from oldName to newName. 
     */
    private void changeName(String oldName, String newName) {

        tables_.changeName(oldName, newName);
    }


    /**
     * When all the conditions are met for starting a new game, we create a new game controller of the
     * appropriate type and start the game here on the server.
     * All human players will be surrogate and robots will be themselves.
     * @param table
     */
    private void startGame(OnlineGameTable table) {

        System.out.println("NOW starting game on Server! "+ table);

        // Create players from the table and start.
        List<Player> players = table.getPlayers();
        assert (players.size() == table.getNumPlayersNeeded());
        List<Player> newPlayers = new ArrayList<Player>(players.size());
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            // if (paleyr.isHuman()) {
            //    playersArray[i] = new SurrogatePlayer(player);
            // } else {
            //     playersArray[i] = player;
            //}                   
        }
        controller_.setPlayers(newPlayers);
        ////controller_.reset();
        // broadcast the command to start for all the clients

    }

    /**
     * @param name
     * @return a unique name if not unique already
     */
    private String verifyUniqueName(String name) {

        int ct = 0;
        for (OnlineGameTable t : tables_) {
            if (t.getName().indexOf(name + '_') == 0) {
                ct++;
            }
        }
        return name + '_' + ct;
    }

}
