// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.puzzle.tantrix.ui;

import com.becker.puzzle.common.*;
import com.becker.puzzle.tantrix.Algorithm;
import com.becker.puzzle.tantrix.TantrixController;
import com.becker.puzzle.tantrix.model.*;
import com.becker.ui.util.GUIUtil;

import javax.swing.*;

/**
 * Tantrix Puzzle Application to show the solving of the puzzle.
 *
 * @author Barry becker
 */
public final class TantrixPuzzle extends PuzzleApplet<TantrixBoard, TilePlacement> {

    /**
     * Construct the application.
     */
    public TantrixPuzzle() {}

    @Override
    protected PuzzleViewer<TantrixBoard, TilePlacement> createViewer() {

        //TantrixBoard board = new TantrixBoard(new HexTiles());
        return new TantrixViewer();
    }

    @Override
    protected PuzzleController<TantrixBoard, TilePlacement>
                createController(Refreshable<TantrixBoard, TilePlacement> viewer) {
        return new TantrixController(viewer);
    }
    
    @Override
    protected AlgorithmEnum<TantrixBoard, TilePlacement>[] getAlgorithmValues() {
        return Algorithm.values();
    }
    
    @Override
    protected JPanel createCustomControls() {
        return new JPanel();
    }

    /**
     * use this to run as an application instead of an applet.
     */
    public static void main( String[] args )  {

        PuzzleApplet applet = new TantrixPuzzle();

        // this will call applet.init() and start() methods instead of the browser
        GUIUtil.showApplet(applet, "Tantrix Puzzle Solver");
    }
}
