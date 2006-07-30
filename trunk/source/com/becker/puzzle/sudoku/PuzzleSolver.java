package com.becker.puzzle.sudoku;

import com.becker.common.*;

/**
 * This does the hard work of actually solving the puzzle.
 * Controller in the model-view-controller pattern.
 *
 * @author Barry Becker
 */
public class PuzzleSolver {

    private int delay_;

    /**
     * Constructor
     */
    public PuzzleSolver() {
        delay_ = 0;
    }

    /**
     * Solves the puzzle.
     * This implements the main algorithm for solving the red puzzle.
     * @param board
     * @return
     */
    public boolean solvePuzzle( Board board) {
        return solvePuzzle(board, null);
    }

    public void setDelay(int delay)  {
        delay_ = delay;
    }

    /**
     * Solves the puzzle.
     * This implements the main algorithm for solving the red puzzle.
     * @param puzzlePanel
     * @return
     */
    protected  boolean solvePuzzle(PuzzlePanel puzzlePanel) {
        return solvePuzzle(puzzlePanel.getBoard(), puzzlePanel);
    }

    /**
     * Solves the puzzle.
     * This implements the main algorithm for solving the red puzzle.
     * @param board
     * @return
     */
    protected boolean solvePuzzle( Board board, PuzzlePanel puzzlePanel) {
        boolean solved = false;
        int ct = 0;
        int maxIterations = 2 * board.getEdgeLength();  // @@ not sure what this should be.

        do {
            // find missing row and column numbers
            board.updateCellCandidates();
            refresh(puzzlePanel);
            Util.sleep(delay_);
            board.checkAndSetUniqueValues();

            refresh(puzzlePanel);
            Util.sleep(2*delay_);
            board.setNumIterations(++ct);
            solved = board.solved();

        } while (!solved && ct < maxIterations);

        refresh(puzzlePanel);

        // if we get here and solved is not true, we did not find a puzzlePanel
        return solved;
    }



    private static void refresh(PuzzlePanel puzzlePanel) {
        if (puzzlePanel == null)
            return;
        puzzlePanel.repaint();
        Util.sleep(20);  // give it a chance to repaint.
    }
}
