/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.puzzle.sudoku;

import com.becker.common.concurrency.Worker;
import com.becker.puzzle.sudoku.ui.SudokuPanel;

import java.awt.*;


/**
 * Controller part of the MVC pattern.
 *
 * @author Barry becker
 */
public final class SudokuController {

    private SudokuPanel puzzlePanel_;

    /**
     * Construct the application and set the look and feel.
     */
    public SudokuController(SudokuPanel panel) {
        puzzlePanel_ = panel;
    }

    public void setShowCandidates(boolean show) {
        puzzlePanel_.setShowCandidates(show);
    }

    public void generatePuzzle(final int delay, final int size) {
        Worker worker = new Worker() {

            @Override
            public Object construct() {
                puzzlePanel_.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                SudokuGenerator generator = new SudokuGenerator(size, puzzlePanel_);
                generator.setDelay(delay);
                puzzlePanel_.generateNewPuzzle(generator);
                return null;
            }

            @Override
            public void finished() {
                puzzlePanel_.repaint();
                puzzlePanel_.setCursor(Cursor.getDefaultCursor());
            }
        };
        worker.start();
    }

    public void solvePuzzle(final int delay) {
        Worker worker = new Worker() {

            @Override
            public Object construct() {
                SudokuSolver solver = new SudokuSolver();
                solver.setDelay(delay);
                puzzlePanel_.startSolving(solver);
                return null;
            }

            @Override
            public void finished() {
                puzzlePanel_.repaint();
            }
        };
        worker.start();
    }

    public void validatePuzzle() {
        puzzlePanel_.validatePuzzle();
    }
}
