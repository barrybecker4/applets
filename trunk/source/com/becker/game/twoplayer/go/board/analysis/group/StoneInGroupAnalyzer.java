package com.becker.game.twoplayer.go.board.analysis.group;

import com.becker.game.twoplayer.go.board.elements.position.GoStone;
import com.becker.game.twoplayer.go.board.elements.group.IGoGroup;

/**
 * Analyze the strength of stone relative to the group that it is in.
 *
 * @author Barry Becker
 */
public class StoneInGroupAnalyzer {

    /**
     * an opponent stone must be at least this much more unhealthy to be considered part of an eye.
     * if its not that much weaker then we don't really have an eye.
     * @@ make this a game parameter .6 - 1.8 that can be optimized.
     */
    private static final float DIFFERENCE_THRESHOLD = 0.6f;

    /** used to determine if a stone is dead or alive. */
    private static final float MIN_LIFE_THRESH = 0.2f;

    /** The group of go stones that we are analyzing. */
    private IGoGroup group_;

    /**
     * Constructor.
     * @param group group to analyze.
     */
    public StoneInGroupAnalyzer(IGoGroup group) {
        group_ = group;
    }


    /**
     * @return true if the stone is much weaker than the group
     */
    public boolean isStoneMuchWeakerThanGroup(GoStone stone) {
        return isStoneWeakerThanGroup(stone, DIFFERENCE_THRESHOLD);
    }

    /**
     * @param stone
     * @param threshold
     * @return return true of the stone is greater than threshold weaker than the group.
     */
    private boolean isStoneWeakerThanGroup(GoStone stone, float threshold) {
        float groupHealth = group_.getAbsoluteHealth();

        // for purposes of determining relative weakness. Don't allow the outer group to go out of its living range.
        if (group_.isOwnedByPlayer1() &&  groupHealth < MIN_LIFE_THRESH) {
            groupHealth = MIN_LIFE_THRESH;
        } else if (!group_.isOwnedByPlayer1() && groupHealth > -MIN_LIFE_THRESH) {
            groupHealth = -MIN_LIFE_THRESH;
        }
        float stoneHealth = stone.getHealth();
        boolean muchWeaker;
        if (stone.isOwnedByPlayer1())  {
            assert (!group_.isOwnedByPlayer1());

            muchWeaker = (-groupHealth - stoneHealth > threshold);
        }
        else {
            assert (group_.isOwnedByPlayer1());
            muchWeaker = (groupHealth + stoneHealth > threshold);
        }

        return muchWeaker;
    }
}
