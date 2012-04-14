// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.puzzle.tantrix.ui.rendering;

import com.becker.common.geometry.Location;
import com.becker.puzzle.common.PuzzleRenderer;
import com.becker.puzzle.tantrix.model.TantrixBoard;
import com.becker.puzzle.tantrix.model.TilePlacement;

import java.awt.*;

import static com.becker.puzzle.tantrix.ui.rendering.HexUtil.ROOT3;
import static com.becker.puzzle.tantrix.ui.rendering.HexUtil.ROOT3D2;

/**
 * Renders the the tantrix puzzle onscreen.
 * @author Barry Becker
 */
public class TantrixBoardRenderer implements PuzzleRenderer<TantrixBoard> {

    static final double MARGIN_FRAC = 0.05;

    private static final Color GRID_COLOR = new Color(110, 120, 180);

    private double hexRadius;
    private HexTileRenderer tileRenderer;

    /**
     * Constructor
     */
    public TantrixBoardRenderer() {
        tileRenderer = new HexTileRenderer();
    }

    /**
     * This renders the current state of the TantrixBoard to the screen.
     */
    public void render(Graphics g, TantrixBoard board, int width, int height)  {

        if (board == null) return;
        Graphics2D g2 = (Graphics2D) g;
        int minEdge = Math.min(width, height);
        hexRadius = (1.0 - 3.0 * MARGIN_FRAC) * minEdge / (board.getEdgeLength() * ROOT3);

        setHints(g2);
        drawGrid(g2, board);

        Location topLeftCorner = board.getBoundingBox().getTopLeftCorner();

        for (Location loc : board.getTantrixLocations()) {
            TilePlacement placement = board.getTilePlacement(loc);
            tileRenderer.render(g2, placement, topLeftCorner, hexRadius);
        }
    }

    private void setHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    /**
     * Draw the gridlines over the background.
     */
    protected void drawGrid(Graphics2D g2, TantrixBoard board) {

        int edgeLen = board.getEdgeLength();
        int xpos, ypos;
        int i;
        int start = 0;
        int margin = (int)(hexRadius/2.0);
        double hexWidth = 2 * hexRadius * ROOT3D2;
        int rightEdgePos = (int)(margin + hexWidth * edgeLen);
        int bottomEdgePos = (int)(margin + hexWidth * edgeLen);

        g2.setColor( GRID_COLOR );
        for ( i = start; i <= edgeLen; i++ )  //   -----
        {
            ypos = (int)(margin + i * hexWidth);
            g2.drawLine( margin, ypos, rightEdgePos, ypos );
        }
        for ( i = start; i <= edgeLen; i++ )  //   ||||
        {
            xpos = (int)(margin + i * hexWidth);
            g2.drawLine( xpos, margin, xpos, bottomEdgePos );
        }
    }
}