/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.multiplayer.common.ui;

import com.becker.game.common.player.Player;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Barry Becker
 */
public class PlayerLabel extends JPanel {

    private JPanel swatch;
    private JLabel playerLabel;

    public PlayerLabel() {
        swatch = new JPanel();
        swatch.setPreferredSize(new Dimension(10, 10));
        playerLabel = new JLabel();
        add(swatch);
        add(playerLabel);
    }

    public void setPlayer(Player player) {
        swatch.setBackground(player.getColor());
        playerLabel.setText(player.getName());
    }
}
