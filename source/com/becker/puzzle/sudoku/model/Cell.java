package com.becker.puzzle.sudoku.model;

import javax.xml.transform.Source;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Barry Becker
 */
public class Cell {

    /** must be a number between 1 and nn_  */
    private int value_;

    /** true if part of the original specification.  */
    private boolean original_;

    /** the BigCell to which I belong   */
    private BigCell parent_;

    /** and, most importantly, the intersection of these.    */
    private Candidates candidates_;



    public Cell(int value, BigCell parent) {
        setOriginalValue(value);
        parent_ = parent;
    }


    public int getValue() {
        return value_;
    }

    public boolean isOriginal() {
        return original_;
    }

    /**
     * once the puzzle is started, you can only assign positive values to values of cells.
     * @param value
     */
    public void setValue(int value) {
        assert(value > 0);
        value_ = value;
        original_ = false;
    }

    public void clearValue() {
        value_ = 0;
        original_ = false;
        candidates_ = new Candidates();
    }

    /**
     * once the puzzle is started, you can only assign positive values to values of cells.
     * @param value
     */
    public void setOriginalValue(int value) {
        assert(value >= 0);
        value_ = value;

        // if set to 0 initially, then it is a value that needs to be filled in.
        original_ = value > 0;

        if (original_)  {
            candidates_ = null;
        }
        else {
            candidates_ = new Candidates();
        }
    }

    public Candidates getCandidates() {
        if (original_) {
            assert(candidates_ == null) : candidates_ +" not null";
        }
        return candidates_;
    }

    /**
     * Intersect the parent big cell candidates with the row and column candidates.
     */
    public void updateCandidates(Candidates rowCandidates, Candidates colCandidates) {

        if (candidates_ == null)
            return;
        candidates_.clear();
        Candidates bigCellSet = parent_.getCandidates();

        //System.out.println("rowCands=" + rowCandidates + " colCands=" + colCandidates);
        for (Integer candidate : bigCellSet)  {
            if (rowCandidates.contains(candidate) && colCandidates.contains(candidate)) {
               candidates_.add(candidate);
           }
        }
    }

    /**
     * @@ Perhaps make a separate pass for setting all the cells that only have one value in the
     * candidate set first.
     * This should improve performance.
     */
    public void checkAndSetUniqueValues(Candidates rowCandidates, Candidates colCandidates) {

        if (candidates_ == null) {
            // nothing to do, the final value is already determined.
            //System.out.println("cands not set yet. final value=" + getValue() +" returning");
            return;
        }

        int unique = parent_.getUniqueValueForCell(this, rowCandidates, colCandidates);
        //System.out.println("unique="+unique);
        if (unique > 0) {
            // set it and remove from appropriate candidate sets
            setValue(unique);
            assert(candidates_.contains(unique));
            candidates_.clear();
            candidates_ = null;
            boolean removed1 = parent_.getCandidates().remove(unique);
            boolean removed2 = rowCandidates.remove(unique);
            boolean removed3 = colCandidates.remove(unique);

            assert (removed1) : "Invalid Puzzle: Could not remove " + unique + " from " + parent_.getCandidates();
            assert (removed2) : "Invalid Puzzle: Could not remove " + unique + " from " + rowCandidates;
            assert (removed3) : "Invalid Puzzle: Could not remove " + unique + " from " + colCandidates;
        }
    }

}
