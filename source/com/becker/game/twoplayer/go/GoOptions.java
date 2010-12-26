package com.becker.game.twoplayer.go;

import com.becker.game.twoplayer.common.TwoPlayerOptions;
import com.becker.game.twoplayer.common.search.options.BestMovesSearchOptions;
import com.becker.game.twoplayer.common.search.options.BruteSearchOptions;
import com.becker.game.twoplayer.common.search.options.MonteCarloSearchOptions;
import com.becker.game.twoplayer.common.search.options.SearchOptions;

/**
 * @author Barry Becker
 */
public class GoOptions extends TwoPlayerOptions {

    /** The komi can vary, but 5.5 seems most commonly used. */
    private static final float DEFAULT_KOMI = 5.5f;

    /** initial look ahead factor. */
    private static final int DEFAULT_LOOK_AHEAD = 3;

    /** for any given ply never consider more that BEST_PERCENTAGE of the top moves. */
    private static final int DEFAULT_PERCENT_LESS_THAN_BEST_THRESH = 60;

    /** for any given ply never consider more that BEST_PERCENTAGE of the top moves. Not used for go */
    private static final int DEFAULT_PERCENTAGE_BEST_MOVES = 0;

    /** for any given ply never consider less taht this many moves. */
    private static final int DEFAULT_MIN_BEST_MOVES = 6;


    /**
     *  additional score given to black or white to bring things into balance.
     * sort of like giving a partial handicap stone.
     */
    private float komi_ = DEFAULT_KOMI;

    public GoOptions() {}

    public GoOptions(SearchOptions searchOptions,
                     String preferredTone, float komi) {
        super(searchOptions, preferredTone);
        setKomi(komi);
    }

    @Override
    protected SearchOptions createDefaultSearchOptions() {

        return new SearchOptions(new BruteSearchOptions(DEFAULT_LOOK_AHEAD, 16),
                                 new BestMovesSearchOptions(DEFAULT_PERCENTAGE_BEST_MOVES,
                                                        DEFAULT_MIN_BEST_MOVES,
                                                        DEFAULT_PERCENT_LESS_THAN_BEST_THRESH),
                                 new MonteCarloSearchOptions(200, 1.0, 10));
    }


    public float getKomi() {
        return komi_;
    }

    public void setKomi(float komi) {
        this.komi_ = komi;
    }

}
