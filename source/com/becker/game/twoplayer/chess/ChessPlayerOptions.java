// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.game.twoplayer.chess;

import com.becker.game.twoplayer.common.TwoPlayerPlayerOptions;
import com.becker.game.twoplayer.common.search.options.BestMovesSearchOptions;
import com.becker.game.twoplayer.common.search.options.BruteSearchOptions;
import com.becker.game.twoplayer.common.search.options.SearchOptions;

import java.awt.*;

/**
 * @author Barry Becker
 */
public class ChessPlayerOptions extends TwoPlayerPlayerOptions {

    public ChessPlayerOptions(String name, Color color) {
        super(name, color);
    }

    @Override
    protected SearchOptions createDefaultSearchOptions() {
        return new SearchOptions(new BruteSearchOptions(2),  new BestMovesSearchOptions(80, 10, 0));
    }
}
