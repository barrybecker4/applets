/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.multiplayer.galactic.player;


import com.becker.game.multiplayer.galactic.Galaxy;
import com.becker.game.multiplayer.galactic.Order;
import com.becker.game.multiplayer.galactic.Planet;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Robot Admiral commanding an intergalactic fleet of starships.
 * These Robot Admirals have there own unique strategy for playing.
 * @@ for now there is only one type, but in the future this should be an abstract base class for other types.
 *
 * @author Barry Becker
 */
public class MethodicalRobotPlayer extends GalacticRobotPlayer {
    private static final int NUM_ICONS = 7;
    private static int iconIndexCounter_ = 0;

    public MethodicalRobotPlayer(String name, Planet homePlanet, Color color, ImageIcon icon) {
        super(name, homePlanet, color);
        if (icon != null) {
            icon_ = icon;
        } else {
            iconBaseName_ = "methodical_robot_icon";
            iconIndex_ = iconIndexCounter_++ % NUM_ICONS;
        }
    }

    /**
     * @return the current list of this Robot's orders.
     */
    @Override
    public List<Order> makeOrders(Galaxy galaxy, int numYearsRemaining) {
        List<Order> newOrders = new ArrayList<Order>();

        List<Planet> ownedPlanets = Galaxy.getPlanets(this);
        for (Planet origin : ownedPlanets) {
            if (origin.getNumShips() > 200)
                newOrders.addAll(getOrders(origin, 4, 50, numYearsRemaining));
            else if (origin.getNumShips() > 100)
                newOrders.addAll(getOrders(origin, 3, 30, numYearsRemaining));
            else if (origin.getNumShips() > 50)
                newOrders.addAll(getOrders(origin, 2, 20, numYearsRemaining));
            // else do nothing.
        }
        orders_.addAll(newOrders);

        return orders_;
    }

}



