package com.becker.puzzle.hiq;

import com.becker.puzzle.common.AlgorithmEnum;
import com.becker.puzzle.common.ConcurrentPuzzleSolver;
import com.becker.puzzle.common.PuzzleController;
import com.becker.puzzle.common.PuzzleSolver;
import com.becker.puzzle.common.Refreshable;
import com.becker.puzzle.common.SequentialPuzzleSolver;

/**
 * Type of HiQ solver to use.
 * 
 * @author Barry Becker
 */
public enum Algorithm implements AlgorithmEnum<PegBoard, PegMove> {
    
    SEQUENTIAL("Solve sequentially"),  
    CONCURRENT_BREADTH("Solve concurrently (breadth first)"), 
    CONCURRENT_DEPTH("Solve concurrently (mostly depth first)"), 
    CONCURRENT_OPTIMUM("Solve concurrently (optimized)");
    
    private String label;
    
    /**
     *Private constructor
     * Creates a new instance of Algorithm
     */
    private Algorithm(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }

    
    /**
     * Create an instance of the algorithm given the controller and a refreshable.
     */
    public PuzzleSolver<PegBoard, PegMove> createSolver(PuzzleController<PegBoard, PegMove> controller, Refreshable<PegBoard, PegMove> ui) {

        switch (this) {
            case SEQUENTIAL :
                return new SequentialPuzzleSolver<PegBoard, PegMove>(controller, ui);
            case CONCURRENT_BREADTH :
                return new ConcurrentPuzzleSolver<PegBoard, PegMove>(controller, 0.6f, ui);
            case CONCURRENT_DEPTH :
                return new ConcurrentPuzzleSolver<PegBoard, PegMove>(controller, 0.1f, ui);
            case CONCURRENT_OPTIMUM :
                return new ConcurrentPuzzleSolver<PegBoard, PegMove>(controller, 0.2f, ui);
        }
        return null;
    }

}
