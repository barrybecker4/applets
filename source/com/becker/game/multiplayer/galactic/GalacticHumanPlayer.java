package com.becker.game.multiplayer.galactic;

import javax.swing.*;
import java.awt.*;


/**
 * Represents an Admiral commanding an intergalactic fleet of starships.
 *
 * @author Barry Becker
 */
class GalacticHumanPlayer extends GalacticPlayer
{

    private static final int NUM_ICONS = 4;
    private static int iconIndexCounter_ = 0;

    GalacticHumanPlayer(String name, Planet homePlanet, Color color, ImageIcon icon) {
        super(name, homePlanet, color, true, icon);
    }

    GalacticHumanPlayer(String name, Planet homePlanet, Color color) {
        super(name, homePlanet, color, true);
        iconBaseName_ = "human_icon";
        iconIndex_ = iconIndexCounter_++ % NUM_ICONS;
    }

}



