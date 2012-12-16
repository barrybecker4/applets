/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.common.online.ui;

import com.barrybecker4.common.CommandLineOptions;
import com.barrybecker4.game.common.online.server.OnlineGameServer;
import com.barrybecker4.game.common.plugin.PluginManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * UI Frame to show what the GameServer is doing.
 * There is nothing game specific about the server it just accepts commands and delegates to the controller.
 * Invoke with
 *   java OnlineGameServer -game <game>
 * for example:
 *   java OnlineGameServer -game poker
 *
 * @author Barry Becker
 */
public class OnlineGameServerFrame  extends JFrame {

    private JTextArea textArea;


    /**
     * Create the online game server to serve all online clients.
     */
    private OnlineGameServerFrame(String gameName) {
        initUI(gameName);
        new OnlineGameServer(gameName, textArea);
    }

    /**
     * initialize UI for server.
     */
    private void initUI(String gameName) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Commands received over the socket:");
        textArea = new JTextArea(20, 44);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.white);
        panel.add("North", label);
        panel.add("Center", new JScrollPane(textArea));
        String gameLabel = PluginManager.getInstance().getPlugin(gameName).getLabel();
        setTitle(gameLabel + " Server");

        getContentPane().add(panel);

        WindowListener l = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        };
        addWindowListener(l);
        pack();
        setVisible(true);
    }

    /**
     * Objects created in run method are finalized when
     * program terminates and thread exits.
     */
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * create and show the server.
     */
    public static void main(String[] args) {

        CommandLineOptions options = new CommandLineOptions(args);

        if (OnlineGameServer.verifyCmdLineOptions(options)) {
            String gameName = options.getValueForOption(OnlineGameServer.GAME_OPTION);
            OnlineGameServerFrame frame = new OnlineGameServerFrame(gameName);
            frame.setVisible(true);
        }
    }
}
