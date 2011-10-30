/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.puzzle.common;

import com.becker.puzzle.common.solver.PuzzleSolver;

/**
 * Enum for type of solver to employ when solving the puzzle.
 * Solver for a given puzzle position P and state transition/move M.
 * 
 * @author Barr Becker
 */
public interface AlgorithmEnum<P, M> {
    
    String getLabel();

    int ordinal();
    
    PuzzleSolver<P, M> createSolver(PuzzleController<P, M> controller,
                                               Refreshable<P, M> ui);
    
}
