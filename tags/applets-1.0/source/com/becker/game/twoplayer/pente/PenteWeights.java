/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.pente;

import com.becker.game.common.GameWeights;

/**
 * These weights determine how the computer values each pattern
 * if only one computer is playing, then only one of the weights arrays is used.
 *
 * These weights determine how the computer values features of the board
 * if only one computer is playing, then only one of the weights arrays is used.
 * use these weights if no others are provided.
 *
 * @author Barry Becker
 */
public class PenteWeights extends GameWeights {

    /** If greater than this threshold, then opponent is in jeopardy. */
    public static final int JEOPARDY_WEIGHT = 80;

    /** These defaults may be overridden in by the user in the UI. */
    private static final double[] DEFAULT_WEIGHTS = {
        0.0,    0.0,    0.0,    0.5,     2.0,     8.0,      JEOPARDY_WEIGHT,     JEOPARDY_WEIGHT + 7.0,    ASSUMED_WINNING_VALUE,
        2 * ASSUMED_WINNING_VALUE,  2 * ASSUMED_WINNING_VALUE,  2 * ASSUMED_WINNING_VALUE
    };

    /** Don't allow the weights to exceed these maximum values. Upper limit. */
    private static final double[] MAX_WEIGHTS = {
        10.0,  10.0,  10.0,  20.0,  40.0,  40.0,  200.0,  200.0,  ASSUMED_WINNING_VALUE,
        3 * ASSUMED_WINNING_VALUE,  4 * ASSUMED_WINNING_VALUE,  5 * ASSUMED_WINNING_VALUE
    };

     /** Don't allow the weights to go below these minimum values. Upper limit. */
    private static final double[] MIN_WEIGHTS = {
        0.0,    0.0,    0.0,    0.0,     0.0,     1.0,       1.0,        1.0,        10.0,
        ASSUMED_WINNING_VALUE,  ASSUMED_WINNING_VALUE,  ASSUMED_WINNING_VALUE
    };

    private static final String[] WEIGHT_SHORT_DESCRIPTIONS = {
        "1a weight", "1b weight", "1c weight", "2a weight",
        "2b weight", "3a weight", "3b weight", "4a  weight",
        "4b weight", "5 weight", "6 weight", "7 weight"};


    private static final String[] WEIGHT_DESCRIPTIONS = {
        "Open ended two in a row (_XX)",            // 1
        "Closed three in a row (XXX)",                    // 2
        "Three in a row with chance to make 4 (_XXX or X_XX)",   // 3
        "Can be blocked after next move (_X_X, X_X_X)",               // 4
        "Chance to have win after next move (_XX_, _X_X_, _X_XX, _XX_X)",  // 5
        "Likely win if play next (_XXX_, _X_XX_, _X_XX_X ...)",                   // 6
        "Guaranteed to win on next move (_XXXX, X_XXX, XX_XX, _X_XXX, _XX_XX, _XXX_X, ...)",  // 7
        "Guaranteed win even if not moving next (_XXXX_, _XXXX_X, ...)",              // 8
        "Already won. open ended 4b in a row (with options)",                    // 9
        "Already won. Arrangements of 5 in a row weight",                    // 10
        "Already won. Arrangements of 6 in a row weight",                // 11
        "Already won. Arrangements of 7 in a row weight"            // 12
    };

    public PenteWeights() {
        super( DEFAULT_WEIGHTS,  MIN_WEIGHTS, MAX_WEIGHTS, WEIGHT_SHORT_DESCRIPTIONS, WEIGHT_DESCRIPTIONS );
    }

}
