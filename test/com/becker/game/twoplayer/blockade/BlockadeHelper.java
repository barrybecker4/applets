/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.blockade;

import com.becker.game.twoplayer.common.TwoPlayerController;
import com.becker.game.twoplayer.common.search.SearchableHelper;

/**
 * @author Barry Becker
 */
public class BlockadeHelper extends SearchableHelper {

    public TwoPlayerController createController() {
        return new BlockadeController();
    }

    @Override
    public String getTestCaseDir() {
        return EXTERNAL_TEST_CASE_DIR + "blockade/cases/searchable/";
    }

    @Override
    protected String getDefaultFileName() {
        return "XXXX";
    }

    @Override
    protected String getStartGameMoveFileName(boolean player1) {
        return player1 ? "x" :"y";
    }

    @Override
    protected String getMiddleGameMoveFileName(boolean player1) {
        return player1 ? "x" :"y";
    }

    @Override
    protected String getEndGameMoveFileName(boolean player1) {
        return player1 ? "x" :"y";
    }
}