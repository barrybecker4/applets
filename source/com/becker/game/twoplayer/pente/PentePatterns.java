package com.becker.game.twoplayer.pente;

import com.becker.game.common.GameContext;


/**
 *  Encapsulates the domain knowledge for Pente.
 *  Its primary client is the PenteController class.
 *  These are key patterns that can occur in the game and are weighted
 *  by importance to let the computer play better.
 *
 *  @author Barry Becker
 */
public class PentePatterns extends Patterns
{
    public static final int WIN_RUN_LENGTH = 5;

    /** total number of patterns used. */
    private static final int NUM_PATTERNS = 210;

    private static final String[] PATTERN_STRING = {
        "_XX", "XXX", "_XXX", "X_XX", "_X_X", "_XX_", "_X_X_", "_X_XX", "_XX_X", "_XXX_",
        "_XXXX", "X_X_X", "X_XXX", "XX_XX", "XXXXX", "_X_X_X", "_X_XX_", "_X_XXX", "_XX_XX", "_XXX_X",
        "_XXXX_", "_XXXXX", "X_X_XX", "X_XX_X", "X_XXXX", "XX_XXX", "XXXXXX", "_X_X_X_", "_X_X_XX", "_X_XX_X",
        "_X_XXX_", "_X_XXXX", "_XX_X_X", "_XX_XX_", "_XX_XXX", "_XXX_XX", "_XXXX_X", "_XXXXX_", "_XXXXXX", "X_X_X_X",
        "X_X_XXX", "X_XX_X_", "X_XX_XX", "X_XXX_X", "X_XXXXX", "XX_X_XX", "XX_XXXX", "XXX_XXX", "XXXXXXX", "_X_X_X_X",
        "_X_X_XX_", "_X_X_XXX", "_X_XX_XX", "_X_XXX_X", "_X_XXXX_", "_X_XXXXX", "_XX_X_XX", "_XX_XX_X", "_XX_XXX_", "_XX_XXXX",
        "_XXX_X_X", "_XXX_XX_", "_XXX_XXX", "_XXXX_X_", "_XXXX_XX", "_XXXXX_X", "_XXXXXX_", "_XXXXXXX", "X_X_X_XX", "X_X_XX_X",
        "X_X_XXXX", "X_XX_XXX", "X_XXX_XX", "X_XXXX_X", "X_XXXXXX", "XX_X_XXX", "XX_XX_XX", "XX_XXXXX", "XXX_XXXX", "XXXXXXXX", "_X_X_X_X_",
        "_X_X_X_XX", "_X_X_XX_X", "_X_X_XXX_", "_X_X_XXXX", "_X_XX_X_X", "_X_XX_XX_", "_X_XX_XXX", "_X_XXX_X_", "_X_XXX_XX", "_X_XXXX_X", "_X_XXXXX_",
        "_X_XXXXXX", "_XX_X_X_X", "_XX_X_XX_", "_XX_X_XXX", "_XX_XX_XX", "_XX_XXX_X", "_XX_XXXX_", "_XX_XXXXX", "_XXX_X_XX", "_XXX_XX_X", "_XXX_XXX_",
        "_XXX_XXXX", "_XXXX_X_X", "_XXXX_XXX", "_XXXXX_XX", "_XXXXXX_X", "_XXXXXXX_", "_XXXXXXXX", "X_X_X_X_X", "X_X_X_XXX", "X_X_XX_XX", "X_X_XXX_X", "X_X_XXXXX", "X_XX_X_XX",
        "X_XX_XX_X", "X_XX_XXXX", "X_XXX_XXX", "X_XXXX_XX", "X_XXXXX_X", "X_XXXXXXX", "XX_X_X_XX", "XX_X_XXXX", "XX_XX_XX_", "XX_XX_XXX", "XX_XXX_XX", "XX_XXXXXX", "XXX_X_XXX",
        "XXX_XXXXX", "XXXX_XXXX", "XXXXXXXXX", "_X_X_X_X_X", "_X_X_X_XX_", "_X_X_X_XXX", "_X_X_XX_X_", "_X_X_XX_XX", "_X_X_XXX_X", "_X_X_XXXX_", "_X_X_XXXXX", "_X_XX_X_X_",
        "_X_XX_X_XX", "_X_XX_XX_X", "_X_XX_XXX_", "_X_XX_XXXX", "_X_XXX_X_X", "_X_XXX_XX_", "_X_XXX_XXX", "_X_XXXX_X_", "_X_XXXX_XX", "_X_XXXXX_X", "_X_XXXXXX_", "_X_XXXXXXX",
        "_XX_X_X_XX", "_XX_X_XX_X", "_XX_X_XXX_", "_XX_X_XXXX", "_XX_XX_X_X", "_XX_XX_XX_", "_XX_XX_XXX", "_XX_XXX_XX", "_XX_XXXX_X", "_XX_XXXXX_", "_XX_XXXXXX", "_XXX_X_X_X",
        "_XXX_X_XXX", "_XXX_XX_XX", "_XXX_XXX_X", "_XXX_XXXX_", "_XXX_XXXXX", "_XXXX_X_XX", "_XXXX_XX_X", "_XXXX_XXXX", "_XXXXX_X_X", "_XXXXX_XXX", "_XXXXXX_XX", "_XXXXXXX_X",
        "_XXXXXXXX_", "_XXXXXXXXX", "X_X_X_X_XX", "X_X_X_XX_X", "X_X_X_XXXX", "X_X_XX_X_X", "X_X_XX_XXX", "X_X_XXX_XX", "X_X_XXXX_X", "X_X_XXXXXX", "X_XX_X_XXX", "X_XX_XX_XX",
        "X_XX_XXX_X", "X_XX_XXXXX", "X_XXX_X_XX", "X_XXX_XXXX", "X_XXXX_XXX", "X_XXXXX_XX", "X_XXXXXX_X", "X_XXXXXXXX", "XX_X_X_XX_", "XX_X_X_XXX", "XX_X_XX_XX", "XX_X_XXXXX",
        "XX_XX_XXXX", "XX_XXX_XXX", "XX_XXXX_XX", "XX_XXXXXXX", "XXX_X_XXX_", "XXX_X_XXXX", "XXX_XX_XXX", "XXX_XXXXX_", "XXX_XXXXXX"
    };

    private static final int[] WEIGHT_INDEX = {
        1, 2, 3, 3, 4, 5, 5, 5, 5, 6,
        7, 4, 6, 7, 9, 5, 6, 7, 7, 7,
        8, 9, 5, 5, 7, 7, 10, 5, 5, 6,
        7, 7, 6, 7, 7, 7, 8, 9, 10, 5,
        7, 6, 7, 8, 9, 5, 7, 7, 11, 6,
        6, 7, 7, 8, 8, 9, 6, 7, 7, 7,
        7, 7, 7, 8, 8, 9, 10, 11, 6, 6,
        7, 7, 8, 8, 10, 7, 8, 9, 7, 11,
        6, 6, 6, 7, 7, 6, 7, 7, 8, 8,
        8, 9, 10, 6, 6, 7, 8, 8, 8, 9,
        7, 7, 7, 7, 8, 8, 9, 10, 11, 11,
        6, 7, 7, 8, 9, 6, 7, 8, 8, 8,
        9, 11, 6, 7, 8, 8, 8, 10, 8, 9,
        7, 11, 6, 6, 7, 6, 7, 8, 8, 9,
        6, 6, 7, 7, 7, 7, 8, 8, 8, 8,
        9, 10, 11, 6, 6, 7, 7, 7, 8, 8,
        8, 8, 9, 10, 7, 8, 8, 8, 8, 9,
        8, 8, 8, 9, 9, 10, 11, 11, 11, 6,
        6, 7, 7, 7, 8, 8, 9, 7, 8, 8,
        9, 8, 8, 8, 9, 10, 11, 6, 7, 7,
        9, 8, 8, 8, 11, 8, 8, 8, 9, 10
    };

    /**
     * Constructor.
     */
    public PentePatterns() {}

    /**
     * This is how many in a row are needed to win
     * if M is five then the game is pente
     */
    public int getWinRunLength() {
        return WIN_RUN_LENGTH;
    }

    public int getMinInterestingLength() {
        return 3;
    }
    
    protected int getNumPatterns() {
        return NUM_PATTERNS;
    }

    protected String getPatternString(int i) {
        return PATTERN_STRING[i];
    }

    protected int getWeightIndex(int i) {
        return WEIGHT_INDEX[i];
    }

     protected String getPatternFile() {
        return  GameContext.GAME_ROOT + "pente/Pente.patterns1.dat";
    }

    protected String getExportFile() {
        return GameContext.GAME_ROOT + "pente/Pente.export.dat";
    }
}
