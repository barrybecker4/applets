package com.becker.puzzle.sudoku.model;

import java.util.*;

/**
 * A block of n*n cells in a sudoku puzzle.
 * @author Barry Becker
 */
public class BigCell {

    /** The internal data structures representing the game board. Row, column order. */
    protected Cell cells_[][] = null;

    /** The number of Cells in the BigCell is n * n.  */
    protected int n_;

    /** The number which have not yet been used in this big cell. */
    private Candidates candidates_;


    public BigCell(Board board, int rowOffset, int colOffset) {

        n_ = board.getBaseSize();

        cells_ = new Cell[n_][n_];
        for (int i=0; i<n_; i++) {
           for (int j=0; j<n_; j++) {
               cells_[i][j] = board.getCell(rowOffset + i, colOffset + j);
               cells_[i][j].setParent(this);
           }
        }
        candidates_ = new Candidates(board.getValuesList());
    }

    /**
     * @return  retrieve the base size of the board - sqrt(edge magnitude).
     */
    public final int getSize() {
        return n_;
    }

    /** a value has been set, so we need to remove it from all the candidate lists. */
    public void remove(int unique) {
        candidates_.safeRemove(unique);
        clearCaches();
    }

    /** add to the bigCell candidate list and each cells candidates for cells not yet set in stone. */
    public void add(int value) {
        candidates_.add(value);
        clearCaches();
    }

    /** @return all the candidate lists for all the cells in the bigCell except the one specified. */
    public CandidatesArray getCandidatesArrayExcluding(int row, int col) {

        List<Candidates> cands = new ArrayList<Candidates>();

        for (int i = 0; i < n_; i++) {
           for (int j = 0; j < n_; j++) {
               Candidates c = getCell(i, j).getCandidates();
               if (!(i==row && j==col) && c!=null) {
                   cands.add(c);
               }
           }
        }
        return new CandidatesArray(cands.toArray(new Candidates[cands.size()]));
    }

    /**
     * assume all of them, then remove those that are represented.
     */
    public void updateCandidates(ValuesList values) {

        candidates_.clear();
        candidates_.addAll(values);

        for (int i = 0; i < n_; i++) {
           for (int j = 0; j < n_; j++) {
               int v = cells_[i][j].getValue();
               if (v > 0) {
                  candidates_.remove(v);
               }
           }
        }
    }

    /**
     * If this bigCell has a row (0, n_-1) that has the only cells with candidates for value,
     * then return that row, else return -1.
     * @param value
     * @return ro (0 to n-1) if found, else -1
     */
    public int findUniqueRowFor(int value) {
        Set<Integer> rows = new HashSet<Integer>();
        for (int i = 0; i < n_; i++)  {
            for (int j = 0; j < n_; j++) {
                Candidates cands = getCell(i, j).getCandidates();
                if (cands!=null && cands.contains(value)) {
                    rows.add(i);
                    break;
                }
            }
        }
        return (rows.size() == 1) ? rows.iterator().next() : -1;
    }

    /**
     * If this bigCell has a row (0, n_-1) that has the only cells with candidates for value,
     * then return that col, else return -1.
     * @param value
     * @return ro (0 to n-1) if found, else -1
     */
    public int findUniqueColFor(int value) {
        Set<Integer> cols = new HashSet<Integer>();
        for (int j = 0; j < n_; j++)  {
            for (int i = 0; i < n_; i++) {
                Candidates cands = getCell(i, j).getCandidates();
                if (cands!=null && cands.contains(value)) {
                    cols.add(j);
                    break;
                }
            }
        }
        return (cols.size() == 1) ? cols.iterator().next() : -1;
    }

    public Candidates getCandidates() {
        return candidates_;
    }

    /**
     * returns null if there is no game piece at the position specified.
     * @return the piece at the specified location. Returns null if there is no piece there.
     */
    public final Cell getCell( int row, int col ) {
        assert ( row >= 0 && row < n_ && col >= 0 && col < n_);
        return cells_[row][col];
    }

    private void clearCaches() {
        for (int j = 0; j < n_; j++)  {
            for (int i = 0; i < n_; i++) {
                getCell(i, j).clearCache();
            }
        }
    }
}
