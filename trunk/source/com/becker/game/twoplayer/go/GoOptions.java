package com.becker.game.twoplayer.go;

import com.becker.game.twoplayer.common.*;
import com.becker.game.twoplayer.common.search.SearchOptions;
import com.becker.sound.*;

/**
 * @author Barry Becker Date: Nov 23, 2006
 */
public class GoOptions extends TwoPlayerOptions {


    /** The komi can vary, but 5.5 seems most commonly used. */
    public static final float DEFAULT_KOMI = 5.5f;

    /** initial look ahead factor. */
    static final int DEFAULT_LOOK_AHEAD = 2;

    /** for any given ply never consider more that BEST_PERCENTAGE of the top moves. */
    static final int DEFAULT_PERCENTAGE_BEST_MOVES = 70;

    /** for any given ply never consider less taht this many moves. */
    static final int DEFAULT_MIN_BEST_MOVES = 10;


    // additional score given to black or white to bring things into balance.
    // sort of like giving a partial handicap stone.
    private float komi_ = DEFAULT_KOMI;

    public GoOptions() {}

    public GoOptions(SearchOptions searchOptions,
                     String preferredTone, float komi) {
        super(searchOptions, preferredTone);
        setKomi(komi);
    }

    @Override
    protected SearchOptions createDefaultSearchOptions() {
        return new SearchOptions(DEFAULT_LOOK_AHEAD, DEFAULT_PERCENTAGE_BEST_MOVES, DEFAULT_MIN_BEST_MOVES, 16);
    }


    public float getKomi() {
        return komi_;
    }

    public void setKomi(float komi) {
        this.komi_ = komi;
    }

}
