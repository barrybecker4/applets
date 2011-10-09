package com.becker.puzzle.sudoku.ui;

import com.becker.game.twoplayer.go.board.analysis.eye.information.FalseEyeInformation;
import com.becker.puzzle.sudoku.SudokuController;
import com.becker.ui.components.GradientButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Buttons at the top for generating and solving the puzzle using different strategies.
 *
 * @author Barry becker
 */
public final class TopControlPanel extends JPanel
                                   implements ActionListener, ItemListener {
    private SudokuController controller_;

    /** click this button to generate a new puzzle */
    private JButton generateButton_;
    /** click this button to validate user entries. */
    private JButton validateButton_;
    /** click this button to solve the current puzzle. */
    private JButton solveButton_;

    private SizeSelector sizeSelector_;
    private SpeedSelector speedSelector_;
    private JCheckBox showCandidatesCheckBox_;

    /** initial value of the show candidates checkbox. */
    private static final boolean DEFAUL_SHOW_CANDIDATES = false;

    /**
     * The solve and generate button at the top.
     */
    public TopControlPanel(SudokuController controller) {
        controller_ = controller;
        controller_.setShowCandidates(DEFAUL_SHOW_CANDIDATES);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(createRowOneControls());
        add(createRowTwoControls());
    }

    private JPanel createRowOneControls() {

        JPanel rowOne = new JPanel();
        rowOne.setLayout(new BoxLayout(rowOne, BoxLayout.X_AXIS));

        generateButton_ = new GradientButton("Generate");
        generateButton_.addActionListener(this);

        //validateButton_ = new GradientButton("Validate");
        //validateButton_.addActionListener(this);

        solveButton_ = new GradientButton("Solve");
        solveButton_.addActionListener(this);

        sizeSelector_ = new SizeSelector();
        sizeSelector_.addItemListener(this);

        rowOne.add(generateButton_);
        //rowOne.add(validateButton_);
        rowOne.add(solveButton_);
        rowOne.add(sizeSelector_);
        rowOne.add(Box.createHorizontalGlue());
        rowOne.add(Box.createHorizontalGlue());
        rowOne.add(Box.createHorizontalGlue());

        return rowOne;
    }

    private JPanel createRowTwoControls() {

        JPanel rowTwo = new JPanel();
        rowTwo.setLayout(new BoxLayout(rowTwo, BoxLayout.X_AXIS));

        showCandidatesCheckBox_ = new JCheckBox("Show Candidates", DEFAUL_SHOW_CANDIDATES);
        showCandidatesCheckBox_.addActionListener(this);

        speedSelector_ = new SpeedSelector();
        speedSelector_.addItemListener(this);

        rowTwo.add(showCandidatesCheckBox_);
        rowTwo.add(speedSelector_);
        rowTwo.add(Box.createHorizontalGlue());
        rowTwo.add(Box.createHorizontalGlue());
        rowTwo.add(Box.createHorizontalGlue());
        rowTwo.add(Box.createHorizontalGlue());

        return rowTwo;
    }
    /**
     * Must execute long tasks in a separate thread,
     * otherwise you don't see the steps of the animation.
     */
    public void actionPerformed(ActionEvent e) {

        Object src = e.getSource();

        if (src == generateButton_)  {
            generatePuzzle(speedSelector_.getSelectedDelay());
        }
        else if (src == solveButton_)  {
            solvePuzzle();
        }
        else if (src == validateButton_) {
             solvePuzzle(); // todo
        }
        else if (src == showCandidatesCheckBox_) {
             controller_.setShowCandidates(showCandidatesCheckBox_.isSelected());
        }
    }

    private void generatePuzzle(final int delay) {
        controller_.generatePuzzle(delay, sizeSelector_.getSelectedSize());
        solveButton_.setEnabled(true);
    }

    /** */
    private void solvePuzzle() {
        controller_.solvePuzzle(speedSelector_.getSelectedDelay());
        solveButton_.setEnabled(false);
    }

    /**
     * size choice selected.
     * @param e  item event.
     */
    public void itemStateChanged(ItemEvent e) {

        if (e.getSource() == sizeSelector_)  {
            generatePuzzle(speedSelector_.getSelectedDelay());
        }
    }
}
