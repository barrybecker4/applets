package com.becker.game.multiplayer.poker.online.ui;

import com.becker.game.common.*;
import com.becker.game.common.ui.*;
import com.becker.game.multiplayer.common.online.ui.*;
import com.becker.game.multiplayer.poker.ui.*;

import com.becker.ui.table.TableButtonListener;
import javax.swing.event.*;
import java.awt.event.*;

/**
 * Manage online player games.
 * Player can join no more than one table at a time.
 *
 * @author Barry Becker Date: May 14, 2006
 */
public class OnlinePokerManagerPanel extends MultiPlayerOnlineManagerPanel {

    private static final long serialVersionUID = 1;
    
    public OnlinePokerManagerPanel(GameViewable viewer, ChangeListener dlg) {
        super(viewer, dlg);
    }

    @Override
    protected MultiPlayerOnlineGameTablesTable createOnlineGamesTable(String playersName,
                                                                      TableButtonListener tableButtonListener) {
        return new PokerOnlineGameTablesTable(tableButtonListener);
    }

    /**
     * You are free to set your own options for the table that you are creating.
     */
    @Override
    protected GameOptionsDialog createNewGameTableDialog() {
        return new PokerOptionsDialog(null, controller_);
    }


}
