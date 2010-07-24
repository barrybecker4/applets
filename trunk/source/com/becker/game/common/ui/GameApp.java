package com.becker.game.common.ui;

import com.becker.common.*;
import com.becker.common.i18n.LocaleType;
import com.becker.game.common.*;
import com.becker.game.common.plugin.PluginManager;
import com.becker.ui.*;

import javax.swing.*;
import java.awt.event.*;

/**
 * This is the application frame wrapper for the game programs.
 * It contains a GamePanel corresponding to the game you have selected to play.
 * If you specify a game class as an argument, then you do not get a menu of all possible games to play.
 *
 * @see GamePanel
 * @author Barry Becker
 */
public class GameApp
{
    private JFrame frame_;

    static {
        GameContext.log(3, "GameApp static init." );
        GUIUtil.setStandAlone((GUIUtil.getBasicService() != null));
    }

    /**
     * Game application constructor.
     * @param initialGame the initial game to show.
     */
    private GameApp(String initialGame)
    {
        GUIUtil.setCustomLookAndFeel();

        frame_ = new JFrame();
        frame_.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
     
        // this will load the resources for the specified game.
        //GameContext.loadGameResources(initialGame);
       
        addMenuBar(initialGame);

        frame_.setBounds(200, 200, 600, 500);
        // display the frame
        frame_.setVisible(true);
        frame_.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * Add a top level menu to allow changing to a different game from the one currently displayed.
     */
    private void addMenuBar(String initialGame)
    {
        GameMenu gameMenu = new GameMenu(frame_, initialGame);
        JMenu fileMenu = new FileMenu(gameMenu);
       
        JMenuBar menubar = new JMenuBar();
        menubar.add(fileMenu);
        menubar.add(gameMenu);

        frame_.getRootPane().setJMenuBar(menubar);
    }
    
    /**
     * Static method to start up the game playing application.
     * The arguments allowed are :
     *  gameName : one of the supported games (eg "go", "checkers", "pente", etc).
     *      If unspecified, the default is DEFAULT_GAME.
     *  locale : The locale (language) to run in. If unspecified, the locale will be "ENGLISH".
     *
     * @param args optionally the game to play and or the locale
     */
    public static void main(String[] args) {

        // do webstart check and set appropriately
        GUIUtil.setStandAlone((GUIUtil.getBasicService() != null));

        String defaultGame = PluginManager.getInstance().getDefaultPlugin().getName();
        String gameName = defaultGame;
        if (args.length == 1) {
            // if there is only one arg assume it is the name of the game
            gameName = args[0];
        }
        else if (args.length > 1) {
            CommandLineOptions options = new CommandLineOptions(args);

            System.out.println("options=" + options);
            if (options.contains("help")) {
                GameContext.log(0, "Usage: -name <game> [-locale <locale>]");
            }
            // create a game panel of the appropriate type based on the name of the class passed in.
            // if no game is specified as an argument, then we show a menu for selecting a game
            gameName = options.getValueForOption("name", defaultGame);

            if (options.contains("locale")) {
                // then a locale has been specified
                String localeName = options.getValueForOption("locale", "ENGLISH");
                LocaleType locale = GameContext.getLocale(localeName, true);
                GameContext.setLocale(locale);
            }
        }

        new GameApp(gameName);
    }

}
