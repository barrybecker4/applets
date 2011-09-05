package com.becker.puzzle.sudoku.model;

/**
 *  An array of sets of integers representing the candidates for the cells in a row or column.
 *
 *  @author Barry Becker
 */
public class BigCellArray {

    /** n by n grid of big cells.   */
    private BigCell[][] bigCells_;

    private int size_;
    private int sizeSq_;

    /**
     * Constructor
     * @param size this size of the row (small grid dim squared).
     */
    public BigCellArray(int size) {

        size_ = size;
        sizeSq_ = size * size;
        bigCells_ = new BigCell[size][size];

        for (int i=0; i<size; i++) {
           for (int j=0; j<size; j++) {
               bigCells_[i][j] = new BigCell(size);
           }
        }
    }

    public BigCell getBigCell(int i, int j) {
        assert ( i >= 0 && i < size_ && j >= 0 && j < size_);
        return bigCells_[i][j];
    }

    /**
     * returns null if there is no game piece at the position specified.
     * @return the piece at the specified location. Returns null if there is no piece there.
     */
    public final Cell getCell( int row, int col ) {
        assert ( row >= 0 && row < sizeSq_ && col >= 0 && col < sizeSq_);
        return bigCells_[row / size_][ col / size_].getCell(row % size_, col % size_);
    }

    public void update(Board board) {
        for (int i=0; i<size_; i++) {
            for (int j=0; j<size_; j++) {
                getBigCell(i, j).updateCandidates(board);
            }
        }
    }

    public void reset() {
        for (int i=0; i<size_; i++) {
           for (int j=0; j<size_; j++) {
               bigCells_[i][j].getCandidates().clear();
           }
        }

        for (int i=0; i<sizeSq_; i++) {
           for (int j=0; j<sizeSq_; j++) {
               Cell c = getCell(i, j);
               if (!c.isOriginal()) {
                   c.clearValue();
               }
           }
        }
    }

    /**
     * @return true if all the cells have been filled in with a value (even if not a valid solution).
     */
    public boolean isFilledIn() {
        for (int row = 0; row < sizeSq_; row++) {
            for (int col = 0; col < sizeSq_; col++) {
                Cell c = getCell(row, col);
                if (c.getValue() <= 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasNoCandidates() {
        for (int row=0; row < sizeSq_; row++) {
            for (int col=0; col < sizeSq_; col++) {
                Cell c = getCell(row, col);
                if (c.getCandidates() != null) {
                    return false;
                }
            }
        }
        return true;
    }
}
