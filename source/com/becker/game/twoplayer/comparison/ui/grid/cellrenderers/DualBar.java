// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.game.twoplayer.comparison.ui.grid.cellrenderers;

import java.awt.*;

/**
 * A two part bar showing a result for when each side starts the game.
 *
 * @author Barry Becker
 */
public class DualBar extends SegmentedBar {

    private Color color;
    private double[] segments;

    /**
     * Constructor
     */
    public DualBar(Color barColor) {
        color = barColor;
    }

    public void setBarSegments(double[] barSegments) {
        segments = barSegments;
    }

    @Override
    protected void drawBar(Graphics2D g2) {

        drawDualBar(segments, color, g2);
    }
}
                                            