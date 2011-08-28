package com.becker.simulation.habitat.model;

import com.becker.simulation.habitat.creatures.Creature;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Barry Becker
 */
public class Cell {

    /** The creatures in this cell. */
    List<Creature> creatures;


    int xIndex;
    int yIndex;

    public Cell(int xIndex, int yIndex)  {
        this.xIndex = xIndex;
        this.yIndex = yIndex;
        creatures = new ArrayList<Creature>();
    }

    public void addCreature(Creature c) {
        creatures.add(c);
    }

    public void removeCreature(Creature c) {
        creatures.remove(c);
    }

    public List<Creature> getCreatures() {
        return creatures;
    }

    public int getNumCreatures() {
        return creatures.size();
    }
}
