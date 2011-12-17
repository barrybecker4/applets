/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.go.board.analysis;

import com.becker.game.common.GameContext;
import com.becker.game.twoplayer.go.board.GoBoard;
import com.becker.game.twoplayer.go.board.PositionalScore;
import com.becker.game.twoplayer.go.board.elements.position.GoBoardPosition;
import com.becker.game.twoplayer.go.board.elements.position.GoStone;
import com.becker.game.twoplayer.go.options.GoWeights;
import com.becker.optimization.parameter.ParameterArray;

/**
 * Used to keep track of evaluating a measure of score passed only on values at positions.
 *
 * @author Barry Becker
 */
public final class PositionalScoreAnalyzer {

    /** a lookup table of scores to attribute to the board positions when calculating the worth */
    private final float[][] positionalScore_;

    /** we assign a value to a stone based on the line on which it falls when calculating worth. */
    private static final float[] LINE_VALS = {
       -0.5f,   // first line
        0.1f,   // second line
        1.0f,   // third line
        0.9f,   // fourth line
        0.1f    // fifth line
    };

    /**
     * Construct the Go game controller.
     */
    public PositionalScoreAnalyzer(int numRows, int numCols) {
        positionalScore_ = createPositionalScoreArray(numRows, numCols);
    }

    /**
     * @param gameStageBoost weight differently based on how far into the game we are
     * @param weights game weights
     * @return accumulated totalScore so far.
     */
    public PositionalScore determineScoreForPosition(GoBoard board, int row, int col, double gameStageBoost,
                                                     ParameterArray weights) {

        GoBoardPosition position = (GoBoardPosition) board.getPosition(row, col);
        double positionalScore = positionalScore_[row][col];
        PositionalScore score = calcPositionalScore(board, position, weights, positionalScore, gameStageBoost);

        position.setScoreContribution(score.getPositionScore());
        return score;
    }

    /**
     * Create the lookup table of scores to attribute to the board positions when calculating the worth.
     * These weights are counted more heavily at te beginning of the game.
     * @return lookup of position scores.
     */
    private float[][] createPositionalScoreArray(int numRows, int numCols) {

        int row, col, rowmin, colmin;
        float[][] positionalScore = new float[numRows + 1][numCols + 1];

        for ( row = 1; row <= numRows; row++ ) {
            rowmin = Math.min( row, numRows - row + 1 );
            for ( col = 1; col <= numCols; col++ ) {
                colmin = Math.min( col, numCols - col + 1 );
                // default neutral value
                positionalScore[row][col] = 0.0f;

                int lineNo = Math.min(rowmin, colmin);
                if (lineNo < LINE_VALS.length) {
                    if (rowmin == colmin)  {
                        // corners get emphasized
                        positionalScore[row][col] = 1.5f * (LINE_VALS[lineNo - 1]);
                    }
                    else {
                        positionalScore[row][col] = LINE_VALS[lineNo - 1];
                    }
                }
            }
        }
        return positionalScore;
    }

    /**
     * @return the score contribution from a single point on the board
     */
    private PositionalScore calcPositionalScore(GoBoard board, GoBoardPosition position, ParameterArray weights,
                                                double positionalScore, double gameStageBoost) {

        PositionalScore score = new PositionalScore();

        if (position.isInEye())  {
            updateEyePointScore(score, position);
        }
        else if (position.isOccupied()) {
            updateNormalizedOccupiedPositionScore(board, score, position, weights, positionalScore, gameStageBoost);
        }

        score.calcPositionScore();
        return score;
    }

    /**
     * Score gets updated with value.
     * A dead enemy stone in the eye counts twice.
     */
    private void updateEyePointScore(PositionalScore score, GoBoardPosition position) {

        // was 2, but one works better.
        double scoreForPosition = position.getEye().isOwnedByPlayer1()? 1.0 : -1.0;
        if (position.isOccupied()) {
            score.deadStoneScore = scoreForPosition;
        }
        else {
            score.eyeSpaceScore = scoreForPosition;
        }
    }

    /**
     * Normalize each of the scores by the game weights.
     */
    private void updateNormalizedOccupiedPositionScore(GoBoard board, PositionalScore score, GoBoardPosition position,
                                                       ParameterArray weights, double positionalScore,
                                                       double gameStageBoost) {
        GoStone stone = (GoStone)position.getPiece();

        int side = position.getPiece().isOwnedByPlayer1()? 1: -1;
        double badShapeWt = weights.get(GoWeights.BAD_SHAPE_WEIGHT_INDEX).getValue();
        double positionalWt = weights.get(GoWeights.POSITIONAL_WEIGHT_INDEX).getValue();
        double healthWt = weights.get(GoWeights.HEALTH_WEIGHT_INDEX).getValue();
        double totalWeight = badShapeWt + positionalWt + healthWt;
        StringShapeAnalyzer shapeAnalyzer = new StringShapeAnalyzer(board);

        // penalize bad shape like empty triangles
        score.badShapeScore =
                -(side * shapeAnalyzer.formsBadShape(position) * badShapeWt) / totalWeight;

        // Usually a very low weight is assigned to where stone is played unless we are at the start of the game.
        score.posScore = side * positionalWt * gameStageBoost * positionalScore / totalWeight;
        score.healthScore = healthWt * stone.getHealth() / totalWeight;

        if (GameContext.getDebugMode() > 1)  {
            stone.setPositionalScore(score);
        }
    }
}