package com.becker.simulation.habitat.creatures;

import com.becker.simulation.habitat.model.Cell;
import com.becker.simulation.habitat.model.HabitatGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * Everything we need to know about a creature.
 * There are many different sorts of creatures, but they are all represented by instance of this class.
 *
 * @author Barry Becker
 */
public class Neighbors {

    /** only pursue prey that is this close to us */
    private static final double SMELL_NEIGHBOR_DISTANCE = 0.05;

    public Creature nearestPrey;
    public Creature nearestFriend;
    List<Creature> flockFriends;

    Creature creature;
    HabitatGrid grid;

    /**
     * Constructor
     */
    public Neighbors(Creature creature, HabitatGrid grid) {
        flockFriends = new ArrayList<Creature>();
        this.creature = creature;
        this.grid = grid;
        findNeighbors();
    }

    /**
     *
     * @return neighbors.
     */
    private void findNeighbors() {

        CreatureType type = creature.getType();

        nearestPrey = null;
        nearestFriend = null;
        double nearestPreyDistance = Double.MAX_VALUE;
        double nearestFriendDistance = Double.MAX_VALUE;

        List<Cell> cells = grid.getNeighborCells(grid.getCellForPosition(creature.getLocation()));

        for (Cell cell : cells) {
            for (Creature nearbyCreature : cell.getCreatures()) {

                double dist = nearbyCreature.getLocation().distance(creature.getLocation());
                if (dist < SMELL_NEIGHBOR_DISTANCE)  {
                    if (nearbyCreature.getType() == type)  {
                        flockFriends.add(nearbyCreature);
                        if (dist < nearestFriendDistance) {
                            nearestFriendDistance = dist;
                            nearestFriend = nearbyCreature;
                        }
                    }
                    else if (type.getPreys().contains(nearbyCreature.getType())) {
                        if (dist < nearestPreyDistance) {
                            nearestPreyDistance = dist;
                            nearestPrey = nearbyCreature;
                        }
                    }
                }
            }
        }
    }

}
