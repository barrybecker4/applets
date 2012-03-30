/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.common.search.strategy;

import com.becker.game.twoplayer.common.search.Searchable;
import com.becker.game.twoplayer.common.search.examples.OneLevelGameTreeExample;
import com.becker.optimization.parameter.ParameterArray;

/**
 * Test mtd strategy independent of any particular game implementation.
 * 
 * @author Barry Becker
 */
public class MtdNegaMaxSearchStrategyTest extends NegaMaxMemorySearchStrategyTest {

    @Override
    protected SearchStrategy createSearchStrategy(Searchable searchable, ParameterArray weights) {
        return new MtdStrategy(new NegaMaxMemoryStrategy(searchable, weights));
    }

    /**
     * Look ahead one level and get the best move.
     */
    @Override
    public void testOneLevelLookAheadPlayer1Search() {
        bruteSearchOptions.setLookAhead(1);
        verifyResult(new OneLevelGameTreeExample(true, getEvaluationPerspective()),
                getOneLevelLookAheadPlayer1Result());
    }


    // seems wrong
    @Override
    protected SearchResult getPruneFourLevelWithABSearchPlayer1() {
        return new SearchResult( "0", 3, 14);
    }

    /*
    @Override
    protected SearchResult getOneLevelLookAheadPlayer1Result() {
        return new SearchResult("0", -2, 4);
    }

    @Override
    protected SearchResult getOneLevelLookAheadPlayer2Result() {
        return new SearchResult("0", -8, 4);
    }


    @Override
    protected SearchResult getTwoLevelPlayer1Result() {
        return new SearchResult("0", 7, 12);
    }
    
    @Override
    protected SearchResult getPruneTwoLevelWithoutABResultPlayer1() {
        return new SearchResult("0", -5, 12);
    }

    @Override
    protected SearchResult getPruneTwoLevelWithABSearchPlayer1() {
        return new SearchResult("0", -5, 7);
    }

    @Override
    protected SearchResult getPruneTwoLevelWithABSearchPlayer2() {
        return new SearchResult("1", 4, 9);
    }


    @Override
    protected SearchResult getThreeLevelPlayer1Result() {
        return new SearchResult("0", -5, 28);
    }

    @Override
    protected SearchResult getThreeLevelPlayer1WithABResult() {
        return new SearchResult("0", -5, 15);
    }    */
}