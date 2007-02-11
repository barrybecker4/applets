package com.becker.game.twoplayer.chess;

import com.becker.game.common.*;
import com.becker.game.twoplayer.common.*;

/**
 * These weights determine how the computer values features of the board
 * if only one computer is playing, then only one of the weights arrays is used.
 * @author Barry Becker Date: Feb 11, 2007
 */
public class ChessWeights extends GameWeights {


    /** use these weights if no others are provided. */
    private static final double[] DEFAULT_CHESS_WEIGHTS = {
        1.1,  7.0,  10.0,  10.0,  14.0,  2.0 * TwoPlayerController.WINNING_VALUE,  0.5
    };

    /** don't allow the weights to exceed these maximum values. */
    private static final double[] MAX_CHESS_WEIGHTS = {
        10.0,  100.0,  100.0,  100.0,  100.0,  2.0 * TwoPlayerController.WINNING_VALUE+1.0,  10.0
    };

    private static final String[] CHESS_WEIGHT_SHORT_DESCRIPTIONS = {
        "Pawn weight",
        "Knight weight",
        "Rook weight",
        "Bishop weight",
        "Queen weight",
        "King weight",
        "Pawn Advancement weight"
    };

    private static final String[] CHESS_WEIGHT_DESCRIPTIONS = {
        "Weight to associate with each remaining pawn",
        "Weight to associate with Knights",
        "Weight to associate with Rooks",
        "Weight to associate with Bishops",
        "Weight to associate with the Queen",
        "Weight to associate with the King",
        "Weight to associate with pawn advancement"
    };

    static final int PAWN_WEIGHT_INDEX = 0;
    static final int KNIGHT_WEIGHT_INDEX = 1;
    static final int ROOK_WEIGHT_INDEX = 2;
    static final int BISHOP_WEIGHT_INDEX = 3;
    static final int QUEEN_WEIGHT_INDEX = 4;
    static final int KING_WEIGHT_INDEX = 5;
    static final int PAWN_ADVANCEMENT_WEIGHT_INDEX = 6;

    public ChessWeights() {
        super( DEFAULT_CHESS_WEIGHTS, MAX_CHESS_WEIGHTS, CHESS_WEIGHT_SHORT_DESCRIPTIONS, CHESS_WEIGHT_DESCRIPTIONS );
    }
}
