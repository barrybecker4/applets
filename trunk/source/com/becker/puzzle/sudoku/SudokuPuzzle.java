package com.becker.puzzle.sudoku;

import com.becker.ui.components.GradientButton;
import com.becker.common.concurrency.Worker;
import com.becker.puzzle.sudoku.test.*;
import com.becker.ui.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Sudoku Puzzle UI.
 * This program solves a Sudoku puzzle.
 * Its difficult to solve by hand because of all the possible permutations.
 *
 * @author Barry becker
 */
public final class SudokuPuzzle extends JApplet
                                implements ActionListener, ItemListener
{

    private SudokuPanel puzzlePanel_;
    // buttons
    private JButton generateButton_;
    private JButton solveButton_;

    private SizeSelector sizeSelector_;
    private SpeedSelector speedSelector_;


    /**
     * Construct the application and set the look and feel.
     */
    public SudokuPuzzle() {
        GUIUtil.setCustomLookAndFeel();
    }

    /**
     * create and initialize the puzzle
     * (init required for applet)
     */
    @Override
    public void init() {
        puzzlePanel_ = new SudokuPanel(Data.SAMPLE1);

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(createButtonPanel(), BorderLayout.NORTH);
        panel.add(puzzlePanel_, BorderLayout.CENTER);
        getContentPane().add( panel);
    }

    /**
     * start solving the puzzle.
     * called by the browser after init(), if running as an applet
     */
    @Override
    public void start() {
        puzzlePanel_.setSize(this.getSize());
    }

    /**
     * stop and cleanup.
     */
    @Override
    public void stop() {}

    /**
     * solve and generate button at the top.
     */
    public JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        generateButton_ = new GradientButton("Generate");
        generateButton_.addActionListener(this);
        solveButton_ = new GradientButton("Solve");
        solveButton_.addActionListener(this);

        panel.add(generateButton_);
        panel.add(solveButton_);
        sizeSelector_ = new SizeSelector();
        speedSelector_ = new SpeedSelector();
        sizeSelector_.addItemListener(this);
        speedSelector_.addItemListener(this);
        panel.add(sizeSelector_);
        panel.add(speedSelector_);
        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    public void actionPerformed(ActionEvent e) {

        // must execute long tasks in a separate thread,
        // otherwise you don't see the steps of the animation.
        Worker worker;
        Object src = e.getSource();

        if (src == generateButton_)  {
            worker = new Worker() {

                public Object construct() {
                    puzzlePanel_.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    puzzlePanel_.generateNewPuzzle();
                    return null;
                }

                public void finished() {
                    puzzlePanel_.repaint();
                    puzzlePanel_.setCursor(Cursor.getDefaultCursor());
                }
            };
            worker.start();
            solveButton_.setEnabled(true);
        }
        else if (src == solveButton_)  {

            worker = new Worker() {

                public Object construct() {
                    puzzlePanel_.startSolving();
                    return null;
                }

                public void finished() {
                    puzzlePanel_.repaint();
                }
            };
            worker.start();
            solveButton_.setEnabled(false);
        }
    }

    /**
     * size choice selected.
     * @param e  item event.
     */
    public void itemStateChanged(ItemEvent e) {

        if (e.getSource() == sizeSelector_)  {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            int size = sizeSelector_.getSelectedSize();
            SudokuGenerator generator = new SudokuGenerator(size);
            Board b = generator.generatePuzzleBoard(null);
            puzzlePanel_.setBoard(b);
            puzzlePanel_.repaint();
            setCursor(Cursor.getDefaultCursor());
        }
        else if (e.getSource() == speedSelector_)  {
            puzzlePanel_.setDelay(speedSelector_.getSelectedDelay());
        }
    }

    /**
     * use this to run as an application instead of an applet.
     */
    public static void main( String[] args )  {

        SudokuPuzzle applet = new SudokuPuzzle();

        // this will call applet.init() and start() methods instead of the browser
        GUIUtil.showApplet( applet, "Sudoku Puzzle Solver" );
    }
}
