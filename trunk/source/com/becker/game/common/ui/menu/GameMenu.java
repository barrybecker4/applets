/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.common.ui.menu;

import com.becker.game.common.GameContext;
import com.becker.game.common.plugin.GamePlugin;
import com.becker.game.common.plugin.PluginManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * The standard main menu for all game programs.
 * Allows such common operations as new, load, save, exit.
 * @author Barry Becker
 */
public class GameMenu extends AbstractGameMenu
                   implements ActionListener  {

    /**
     * Game menu constructor
     * @param initialGame the initially selected game.
     */
    public GameMenu(String initialGame) {
        super(GameContext.getLabel("GAME"));

        for (GamePlugin gamePlugin : getPlugins()) {
            String gameNameLabel = (gamePlugin.getLabel());
            add(createMenuItem(gameNameLabel));
        }
        System.out.println("the initial game is " + initialGame);

        currentGameName = initialGame;
    }


    protected List<GamePlugin> getPlugins() {
        return PluginManager.getInstance().getPlugins();
    }

    /**
     * called when the user has selected a different game to play from the game menu
     * @param e
     */
    public void actionPerformed( ActionEvent e ) {
        JMenuItem item = (JMenuItem) e.getSource();

        currentGameName =
                PluginManager.getInstance().getPluginFromLabel(item.getText()).getName();
        System.out.println("the current game is " + currentGameName);

        notifyOfChange(currentGameName);
    }

}
