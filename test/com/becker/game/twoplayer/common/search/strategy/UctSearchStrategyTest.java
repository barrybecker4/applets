package com.becker.game.twoplayer.common.search.strategy;

import com.becker.game.twoplayer.common.TwoPlayerOptions;
import com.becker.game.twoplayer.common.search.Searchable;
import com.becker.game.twoplayer.common.search.examples.EvaluationPerspective;
import com.becker.game.twoplayer.common.search.examples.FourLevelGameTreeExample;
import com.becker.game.twoplayer.common.search.options.BruteSearchOptions;
import com.becker.game.twoplayer.common.search.options.MonteCarloSearchOptions;
import com.becker.optimization.parameter.ParameterArray;

/**
 * Test minimax strategy independent of any particular game implementation.
 * 
 * @author Barry Becker
 */
public class UctSearchStrategyTest extends MonteCarloSearchStrategyTst {

    @Override
    protected SearchStrategy createSearchStrategy(Searchable searchable, ParameterArray weights) {
        return new UctStrategy(searchable, weights);
    }

    @Override
    protected EvaluationPerspective getEvaluationPerspective() {
        return EvaluationPerspective.ALWAYS_PLAYER1;
    }

    public void testTenSimulationSearchPlayer1() {
        monteCarloOptions.setMaxSimulations(10);
        verifyResult(new FourLevelGameTreeExample(false, getEvaluationPerspective()),
                getTenSimulationSearchPlayer1Result());
    }
}

