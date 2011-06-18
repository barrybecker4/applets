package com.becker.game.twoplayer.go.board.elements.group;

import com.becker.common.geometry.Box;
import com.becker.game.twoplayer.go.board.elements.IGoSet;
import com.becker.game.twoplayer.go.board.elements.position.GoBoardPosition;
import com.becker.game.twoplayer.go.board.elements.position.GoBoardPositionSet;
import com.becker.game.twoplayer.go.board.elements.string.GoStringSet;
import com.becker.game.twoplayer.go.board.elements.string.IGoString;

/**
 * Makes some unit tests much simpler if we create the tests to use this interface instead
 * of the full-blown GoGroup class.
 *
 * @author Barry Becker
 */
public interface IGoGroup extends IGoSet {

    void addMember(IGoString string);

    GoStringSet getMembers();

    //float getAbsoluteHealth();

    boolean isOwnedByPlayer1();

    void addChangeListener(GroupChangeListener listener);
    void removeChangeListener(GroupChangeListener listener);

    int getNumStones();

    //GoEyeSet getEyes(GoBoard board);

    //float getRelativeHealth(GoBoard board, boolean useCachedValue);

    boolean containsStone(GoBoardPosition stone);

    void remove(IGoString string);

    GoBoardPositionSet getStones();

    //float calculateAbsoluteHealth( GoBoard board);

    //float calculateRelativeHealth( GoBoard board);

    void updateTerritory( float health );

    Box findBoundingBox();

    //boolean isStoneMuchWeaker(GoStone stone);

    String toHtml();
}