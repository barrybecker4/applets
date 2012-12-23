// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.game.common.online.server;

import com.barrybecker4.game.common.GameContext;
import com.barrybecker4.game.common.online.GameCommand;
import com.barrybecker4.game.common.online.OnlineGameTable;
import com.barrybecker4.game.common.online.OnlineGameTableList;
import com.barrybecker4.game.common.player.Player;

/**
 * Handles operations on the list of online game tables.
 *
 * @author Barry Becker
 */
class GameTableManager {

    /** Maintain a list of game tables. */
    private OnlineGameTableList tables_;


    /**
     * Create the online game server to serve all online clients.
     */
    public GameTableManager() {

        tables_ = new OnlineGameTableList();
    }

    public OnlineGameTableList getTables() {
        return tables_;
    }

    void removePlayer(String player) {
        tables_.removePlayer(player);
    }

    void removeTable(OnlineGameTable table) {
        tables_.remove(table);
    }

    /**
     * @param table the table to add
     */
    void addTable(OnlineGameTable table) {

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
     * Get the most recently added human player from the table and have them join the table with the same name.
     * If there is a table now ready to play after this change, then start it.
     * @return start game command if all players present, else returns null.
     */
    GameCommand joinTable(OnlineGameTable table) {

        GameCommand response = null;
        // if the player at this new table is already sitting at another table,
        // remove him from the other tables(s) and delete those other tables (if no one else is there).
        Player p = table.getNewestHumanPlayer();
        GameContext.log(2, "in join table on the server p="+p);
        tables_.removePlayer(p);
        tables_.join(table.getName(), p);
        OnlineGameTable tableToStart = tables_.getTableReadyToPlay(p.getName());
        if (tableToStart != null) {

            response = new GameCommand(GameCommand.Name.START_GAME,  tableToStart);
            tables_.remove(tableToStart);
        }
        return response;
    }

    /**
     * Change the players name from oldName to newName.
     */
    void changeName(String oldName, String newName) {

        tables_.changeName(oldName, newName);
    }

    /**
     * @param name the name to verify uniqueness of.
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
