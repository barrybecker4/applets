// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.game.twoplayer.pente.analysis.differencers;

import com.barrybecker4.game.twoplayer.common.search.SearchableHelper;
import com.barrybecker4.game.twoplayer.pente.PenteBoard;
import com.barrybecker4.game.twoplayer.pente.PenteController;
import com.barrybecker4.game.twoplayer.pente.pattern.Patterns;
import com.barrybecker4.game.twoplayer.pente.pattern.PentePatterns;
import com.barrybecker4.game.twoplayer.pente.pattern.SimpleWeights;
import com.barrybecker4.optimization.parameter.ParameterArray;
import junit.framework.TestCase;

/**
 * Base class for all differencer tests.
 *
 * @author Barry Becker
 */
public abstract class ValueDifferencerTst extends TestCase  {

    private static final String TEST_CASE_DIR =
           SearchableHelper.EXTERNAL_TEST_CASE_DIR + "pente/cases/analysis/differencers/";

    /** instance under test */
    protected ValueDifferencer differencer;
    protected ValueDifferencerFactory differencerFactory;
    protected ParameterArray weights = new SimpleWeights().getDefaultWeights();
    protected StubLineFactory lineFactory;


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        PenteBoard board = restoreBoard("exampleBoard");
        Patterns patterns = new PentePatterns();
        lineFactory = new StubLineFactory();
        differencerFactory = new ValueDifferencerFactory(board, patterns, lineFactory);
        differencer = createDifferencer(board, patterns);
    }

    protected abstract ValueDifferencer createDifferencer(PenteBoard board, Patterns patterns);

    /**
     * Restore a game file
     * @param problemFileBase the saved game to restore and test.
     */
    private PenteBoard restoreBoard(String problemFileBase) {
        PenteController controller = new PenteController();
        controller.restoreFromFile(TEST_CASE_DIR + problemFileBase + ".sgf");
        return (PenteBoard) controller.getBoard();
    }

    protected void verifyLine(int row, int col, String expectedLineContent) {
        int diff = differencer.findValueDifference(row, col, weights);
        assertEquals("Unexpected difference", 0, diff);
        assertEquals("Unexpected line created", expectedLineContent, lineFactory.getLineContent());
    }
}