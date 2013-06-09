/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.puzzle.hiq.ui;

import com.barrybecker4.puzzle.common.ui.PuzzleViewer;
import com.barrybecker4.puzzle.hiq.model.PegBoard;
import com.barrybecker4.puzzle.hiq.model.PegMove;

import java.awt.Graphics;
import java.util.List;

/**
 *  UI for drawing the current best solution to the puzzle.
 *  @author Barry Becker
 */
final class PegBoardViewer extends PuzzleViewer<PegBoard, PegMove>
                           implements PathNavigator {

    private PegBoardRenderer renderer_ = new PegBoardRenderer();
    private List<PegMove> path_;
    private DoneListener doneListener;

    /**
     * Constructor.
     */
    PegBoardViewer(PegBoard board, DoneListener listener) {
        doneListener = listener;
        board_ = board;
    }

    @Override
    public List<PegMove> getPath() {
        return path_;
    }

    @Override
    public void refresh(PegBoard board, long numTries) {
        if (numTries % 6000 == 0) {
            status_ = createStatusMessage(numTries);
            simpleRefresh(board, numTries);
        }
    }

    @Override
    public void finalRefresh(List<PegMove> path, PegBoard board, long numTries, long millis) {
        super.finalRefresh(path, board, numTries, millis);
        showPath(path, board);
    }

    @Override
    public void makeSound() {
        // add sound
    }


    @Override
    public void moveInPath(int currentPosition, int stepSize) {
        int currentStep = currentPosition;
        int inc = stepSize > 0 ? 1 : -1;
        int toStep = currentStep + stepSize;
        do {
            makeMove(currentStep, (inc < 0));
            currentStep += inc;
        } while (currentStep != toStep);
        repaint();
    }

    public void makeMove(int currentStep, boolean undo) {
        board_ = board_.doMove(getPath().get(currentStep), undo);
    }

    /**
     * This renders the current state of the puzzle to the screen.
     */
    @Override
    protected void paintComponent( Graphics g ) {
        super.paintComponent( g );
        renderer_.render(g, board_, getWidth(), getHeight());
    }

    public void showPath(List<PegMove> path, PegBoard board) {

        path_ = path;
        board_ = board;
        System.out.println("path size="+ path.size());
        System.out.println("path="+ path);
        if (doneListener != null) {
            doneListener.done();
        }
    }
}

