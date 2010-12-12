package com.becker.game.twoplayer.common.search.options;

import com.becker.game.twoplayer.common.search.SearchWindow;

/**
 * The options for search strategies that use brute-force minimax search like MiniMax, NegaMax, NegaScout,
 * and alsot the memory and aspiration variations of these strategies.
 * These methods usually use a search window to do pruning of tree branches.
 *
 * @author Barry Becker
 */
public class BruteSearchOptions {

    /** Number of moves to look ahead while searching for the best move. */
    private static final int DEFAULT_LOOK_AHEAD = 3;

    /** if true then use alpha beta pruning */
    private static final boolean ALPHA_BETA = true;

    /** if true then use quiescent search */
    private static final boolean QUIESCENCE = false;

    /** never search more than this many additional plys during quiescent search. */
    private static final int DEFAULT_MAX_QUIESCENT_DEPTH = 8;

    private boolean alphaBeta_ = ALPHA_BETA;
    private boolean quiescence_ = QUIESCENCE;

    /** default alpha beta values. Some strategies (like nega* reverse them) */
    private static final SearchWindow DEFAULT_SEARCH_WINDOW = new SearchWindow();

    private int lookAhead_;

    private int maxQuiescentDepth_ = DEFAULT_MAX_QUIESCENT_DEPTH;
    private SearchWindow initialSearchWindow_ = DEFAULT_SEARCH_WINDOW;

    private BestMovesSearchOptions bestMovesOptions_;

    /**
     * Default Constructor
     */
    public BruteSearchOptions() {
        bestMovesOptions_ = new BestMovesSearchOptions();
        lookAhead_ = getDefaultLookAhead();
    }

    /**
     * Constructor
     * @param defaultLookAhead default number of moves to look ahead.
     */
    public BruteSearchOptions(int defaultLookAhead, BestMovesSearchOptions bestMovesOptions) {
        lookAhead_ = defaultLookAhead;
        bestMovesOptions_ = bestMovesOptions;
    }

    /**
     * Constructor
     */
    public BruteSearchOptions(int defaultLookAhead, int maxQuiescentDepth, BestMovesSearchOptions bestMovesOptions) {
        this(defaultLookAhead, bestMovesOptions);
        maxQuiescentDepth_ = maxQuiescentDepth;
    }

    public BestMovesSearchOptions getBestMovesSearchOptions() {
        return bestMovesOptions_;
    }

    int getDefaultLookAhead() {
        return DEFAULT_LOOK_AHEAD;
    }

    /**
     * @return the amount of look-ahead (number of plys) used by the search strategy.
     */
    public final int getLookAhead() {
        return lookAhead_;
    }

    /**
     * @param look the number of plys to look ahaead.
     */
    public final void setLookAhead( int look ) {
        lookAhead_ = look;
    }

    /**
     * @return true if alpha-beta pruning is being employed by the search strategy.
     */
    public final boolean getAlphaBeta() {
        return alphaBeta_;
    }

    /**
     * @param ab set whether of not to use alpha-beta pruning
     */
    public final void setAlphaBeta( boolean ab ) {
        alphaBeta_ = ab;
    }

    /**
     * @return whether or not the quiescent search option is being used by the search strategy
     */
    public final boolean getQuiescence() {
        return quiescence_;
    }

    public final void setQuiescence( boolean quiescence )  {
        quiescence_ = quiescence;
    }

    public void setMaxQuiescentDepth(int value) {
        maxQuiescentDepth_ = value;
    }

    public int getMaxQuiescentDepth() {
        return maxQuiescentDepth_;
    }

    public final void setInitialSearchWindow(SearchWindow window) {
        initialSearchWindow_ = window;
    }

    public SearchWindow getInitialSearchWindow() {
        return initialSearchWindow_.copy();
    }
}