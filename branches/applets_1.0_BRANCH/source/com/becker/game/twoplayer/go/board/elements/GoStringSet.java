package com.becker.game.twoplayer.go.board.elements;

import java.util.LinkedHashSet;

/**
 *  A set of GoStrings.
 *
 *  @author Barry Becker
 */
public class GoStringSet extends LinkedHashSet<GoString>
{

    public GoStringSet() {}
    
    /**
     * Copy constructor.
     * @param set
     */
    public GoStringSet(GoStringSet set) {
        super(set);
    }

    /**
     *
     * @param pos
     * @return the stirng that contains pos if any. Null if none.
     */
    public GoString findStringContainingPosition(GoBoardPosition pos) {
        for (GoString str : this) {
            if (str.contains(pos)) {
                return str;
            }
        }
        return null;
    }
}