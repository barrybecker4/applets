package com.becker.game.multiplayer.poker.online.ui;

import com.becker.game.common.GameContext;
import com.becker.game.common.online.OnlineGameTable;
import com.becker.game.common.player.Player;
import com.becker.game.multiplayer.common.MultiGameOptions;
import com.becker.game.multiplayer.common.online.ui.MultiPlayerOnlineGameTablesTable;
import com.becker.game.multiplayer.poker.PokerOptions;
import com.becker.game.multiplayer.poker.online.OnlinePokerTable;
import com.becker.game.multiplayer.poker.player.PokerHumanPlayer;
import com.becker.ui.table.TableButtonListener;

/**
 * Show poker specific game options in the table row.
 *
 * @author Barry Becker Date: May 13, 2006
 */
 public class PokerOnlineGameTablesTable extends MultiPlayerOnlineGameTablesTable {

    

    private static final int ANTE_INDEX = NUM_BASE_COLUMNS;
    private static final int MAX_RAISE_INDEX = NUM_BASE_COLUMNS + 1;
    private static final int INITIAL_CASH_INDEX = NUM_BASE_COLUMNS + 2;

    private static final String ANTE = GameContext.getLabel("ANTE");
    private static final String MAX_RAISE = GameContext.getLabel("MAX_RAISE");
    private static final String INITIAL_CASH = GameContext.getLabel("INITIAL_CASH");

    private static final String[] POKER_COLUMN_NAMES = {JOIN, MIN_NUM_PLAYERS, PLAYER_NAMES, ANTE, MAX_RAISE, INITIAL_CASH};

    /**
     *
     * @param actionListener  that gets called when the player selects a different table to join.
     */
    public PokerOnlineGameTablesTable(TableButtonListener tableButtonListener) {
         super(POKER_COLUMN_NAMES, tableButtonListener);
    }


    @Override
    protected Object[] getRowObject(OnlineGameTable onlineTable, boolean localPlayerAtTable)
    {
        Object d[] = new Object[getNumColumns()];
        // false if active player is in this table.
        // You cannot join a table you are already at
        d[JOIN_INDEX] = !(localPlayerAtTable);
        d[NUM_PLAYERS_INDEX] = onlineTable.getNumPlayersNeeded();
        d[PLAYER_NAMES_INDEX] = onlineTable.getPlayerNames();
        PokerOptions options = (PokerOptions) onlineTable.getGameOptions();
        d[ANTE_INDEX] = options.getAnte();
        d[MAX_RAISE_INDEX] = options.getMaxAbsoluteRaise();
        d[INITIAL_CASH_INDEX] = options.getInitialCash();
        return d;
    }

    @Override
    public OnlineGameTable createOnlineTable(String ownerPlayerName, MultiGameOptions options) {
        Player player = createPlayerForName(ownerPlayerName);
        return new OnlinePokerTable(getUniqueName(), player, options);
    }

    @Override
    public Player createPlayerForName(String playerName) {
        return new PokerHumanPlayer(playerName, 100, getRandomColor());
    }

}
