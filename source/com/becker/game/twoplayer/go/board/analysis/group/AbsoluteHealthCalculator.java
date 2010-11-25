package com.becker.game.twoplayer.go.board.analysis.group;

import com.becker.game.common.GameContext;
import com.becker.game.twoplayer.go.GoProfiler;
import com.becker.game.twoplayer.go.board.GoBoard;
import com.becker.game.twoplayer.go.board.elements.GoBoardPositionSet;
import com.becker.game.twoplayer.go.board.elements.GoEye;
import com.becker.game.twoplayer.go.board.elements.GoGroup;
import com.becker.game.twoplayer.go.board.elements.GoString;

import java.util.Set;

/**
 * Determine the absolute health of a group independent of the health of neighboring groups.
 * @author Barry Becker
 */
class AbsoluteHealthCalculator {

    /** The group of go stones that we are analyzing. */
    private GoGroup group_;

    /**
     * This is a number between -1 and 1 that indicates how likely the group is to live
     * independent of the health of the stones around it.
     * all kinds of factors can contribute to the health of a group.
     * Local search should be used to make this as accurate as possible.
     * If the health is 1.0 then the group has at least 2 eyes and is unconditionally alive.
     * If the health is -1.0 then there is no way to save the group even if you could
     * play 2 times in a row.
     * Unconditional life means the group cannot be killed no matter how many times the opponent plays.
     * A score of near 0 indicates it is very uncertain whether the group will live or die.
     */
    private float absoluteHealth_ = 0;

    /** Number of stones in the group. */
    private int cachedNumStonesInGroup_;

    /** Maintains cache of this groups eyes. */
    private GroupEyeCache eyeCache_;

    /**
     * This is the cached number of liberties.
     * It updates whenever something has changed.
     */
    private GoBoardPositionSet cachedLiberties_;


    /**
     * Constructor
     * @param group the group to analyze
     */
    public AbsoluteHealthCalculator(GoGroup group) {
        group_ = group;
        eyeCache_ = new GroupEyeCache(group);
    }

    /**
     * @return true if the group has changed (structurally) in any way.
     */
    public boolean isValid()
    {
        return eyeCache_.isValid();
    }

    public void invalidate() {
        eyeCache_.invalidate();
    }

    /**
     * used only for test.
     * @return eye potential
     */
    public float getEyePotential() {
        return eyeCache_.getEyePotential();
    }

    /**
     * Calculate the absolute health of a group.
     * All the stones in the group have the same health rating because the
     * group lives or dies as a unit
     * (not entirely true - strings live or die as unit, but there is a relationship).
     * Good health of a black group is positive; white, negative.
     * The health is a function of the number of eyes (their type and status), liberties, and
     * the health of surrounding groups. If the health of an opponent bordering group
     * is in worse shape than our own then we get a boost since we can probably
     * kill that group first. See calculateRelativeHealth below.
     * A perfect 1 (or -1) indicates unconditional life (or death).
     * This means that the group cannot be killed (or given life) no matter
     * how many times the opponent plays (see Dave Benson 1977).
     *  http://senseis.xmp.net/?BensonsAlgorithm
     *
     * @@ need expert advice to make this work well.
     * @@ make the constants parameters and optimize them.
     * @@ we currently don't give any bonus for false eyes. should we?
     *
     * @return the overall health of the group independent of nbr groups.
     */
    public float calculateAbsoluteHealth(GoBoard board)
    {
        if ( eyeCache_.isValid() ) {
            return absoluteHealth_;
        }

        int numLiberties = getNumLiberties(board);

        // we multiply by a +/- sign depending on the side
        float side = group_.isOwnedByPlayer1() ? 1.0f : -1.0f;

        // first come up with some approximation for the health so update eyes can be done more accurately.
        float numEyes = eyeCache_.calcNumEyes();
        int numStones = getNumStones();
        EyeHealthEvaluator eyeEvaluator = new EyeHealthEvaluator(group_, board);

        absoluteHealth_ = eyeEvaluator.determineHealth(side, numEyes, numLiberties, numStones);

        GoProfiler.getInstance().start(GoProfiler.UPDATE_EYES);
        eyeCache_.updateEyes(board);  // expensive
        GoProfiler.getInstance().stop(GoProfiler.UPDATE_EYES);

        numEyes = Math.max(eyeCache_.getEyePotential(), eyeCache_.calcNumEyes());

        // health based on eye shape - the most significant factor
        float health = eyeEvaluator.determineHealth(side, numEyes, numLiberties, numStones);

        // No bonus at all for false eyes
        absoluteHealth_ = health;
        if (Math.abs(absoluteHealth_) > 1.0) {
            GameContext.log(0,  "Warning: health exceeded 1.0: " +" health="+health+" numEyes="+numEyes);
            absoluteHealth_ = side;
        }

        return absoluteHealth_;
    }

    /**
     * @return  set of eyes currently identified for this group.
     */
    public Set<GoEye> getEyes(GoBoard board) {
        return eyeCache_.getEyes(board);
    }

    /**
     * Get the number of liberties that the group has.
     * @return the number of liberties that the group has
     */
    public GoBoardPositionSet getLiberties(GoBoard board) {
        if (eyeCache_.isValid()) {
             return cachedLiberties_;
        }
        GoBoardPositionSet liberties = new GoBoardPositionSet();
        for (GoString str : group_.getMembers()) {
            liberties.addAll(str.getLiberties(board));
        }
        cachedLiberties_ = liberties;
        return liberties;
    }

    /**
     * If nothing cached, this may not be accurate.
     * @param board if null, then the number of liberties returned is just what is in the cache and may not be accurate.
     * @return number of cached liberties if board is null, else exact number of liberties.
     */
    public int getNumLiberties(GoBoard board) {
        if (board == null) {
            return cachedLiberties_ == null ? 0 :cachedLiberties_.size();
        }
        return getLiberties(board).size();
    }

    /**
     * Calculate the number of stones in the group.
     * @return number of stones in the group.
     */
    public int getNumStones() {
        if (eyeCache_.isValid()) {
            return cachedNumStonesInGroup_;
        }
        int numStones = 0;
        for (GoString str : group_.getMembers()) {
            numStones += str.size();
        }
        cachedNumStonesInGroup_ = numStones;
        return numStones;
    }
}
