package com.becker.game.twoplayer.common.search.strategy;

/**
 * Manages alpha and beta - the search window thresholds.
 *
 * @author Barry Becker
 */
public class SearchWindow {

    public int alpha;
    public int beta;

    public SearchWindow() {
        this(SearchStrategy.INFINITY, -SearchStrategy.INFINITY);
    }

    /**
     * init with min and max valeus of the range.
     * @param minimum min value for range
     * @param maximum max value for range
     */
    public SearchWindow(int minimum, int maximum) {
        this.alpha = minimum;
        this.beta = maximum;
    }

    public SearchWindow copy()    {
        return new SearchWindow(alpha, beta);
    }

    /**
     *
     * @return a new window which spas and negates the alpha and beta values.
     */
    public SearchWindow negateAndSwap() {
       return new SearchWindow(-alpha, -beta); 
    }

    public int getExtent() {
        if (alpha > beta) {
            return -Integer.MAX_VALUE;
        }
        return (beta - alpha);
    }

    /**
     * @return midway betwee alpha and beta.
     */
    public int getMidPoint() {
        return (alpha +  beta)/2;
    }

    public String toString() {
        return "alpha=" + alpha + " beta=" + beta;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchWindow that = (SearchWindow) o;

        if (beta != that.beta) return false;
        if (alpha != that.alpha) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = alpha;
        result = 31 * result + beta;
        return result;
    }


}