package com.becker.game.twoplayer.go.board;

import com.becker.game.common.MoveList;
import com.becker.game.common.board.CaptureList;

/**
 * All the stuff used to compute the worth.
 * temp class used for debugging.
 *
 * @author Barry Becker
 */
public class WorthInfo {

    private double gameStageBoost;
    private double territoryDelta;
    private double captureScore;
    private int blackCap;
    private int whiteCap;
    private double positionalScore;
    private PositionalScore[][] positionalScores;
    private CaptureList captures;
    private double worth;
    private MoveList moves;

    /**
     * Constructor.
     */
    public WorthInfo(double gameStageBoost, double territoryDelta,
                     double captureScore, int blackCap, int whiteCap, double positionalScore, PositionalScore[][] positionalScores,
                     CaptureList captures, double worth, MoveList moves) {
        this.gameStageBoost = gameStageBoost;
        this.territoryDelta = territoryDelta;
        this.captureScore = captureScore;
        this.blackCap = blackCap;
        this.whiteCap = whiteCap;
        this.positionalScore = positionalScore;
        this.positionalScores  = positionalScores;
        this.captures = captures;
        this.worth = worth;
        this.moves = moves;
    }

    /**
     *  Show all the worth information
     *  @return string form.
     */
    public String toString() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("gameStageBoost=" + gameStageBoost  +" territoryDelta=" + territoryDelta
                + " captureScore=" + captureScore + "(b="+ blackCap + " w="+ whiteCap + ") positionalScore="+positionalScore
                + (captures!=null? " captures=" + captures  : "")
                + " worth=" + worth + " \nposScores:\n");
        for (PositionalScore[] posScoreRow : positionalScores ) {
            for (PositionalScore pscore : posScoreRow) {
                bldr.append(pscore);
            }
            bldr.append("\n");
        }
        bldr.append("moves=" + moves);
        bldr.append("\n");

        return bldr.toString();
    }
}