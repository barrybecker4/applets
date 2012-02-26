// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.game.twoplayer.pente.analysis.differencers;

import com.becker.game.twoplayer.common.TwoPlayerBoard;
import com.becker.game.twoplayer.pente.pattern.Patterns;
import com.becker.game.twoplayer.pente.analysis.LineFactory;

/**
 * Determines the difference in value between the most recent move
 * and how it was before in the up/down or left right direction.
 *
 * @author Barry Becker
*/
public abstract class DiagonalDifferencer extends ValueDifferencer {

    protected int numRows;
    protected int numCols;


    public DiagonalDifferencer(TwoPlayerBoard board, Patterns patterns,
                               LineFactory lineFactory) {
        super(board, patterns, lineFactory);
    }

    protected void init() {
        numRows = board_.getNumRows();
        numCols = board_.getNumCols();
    }
}