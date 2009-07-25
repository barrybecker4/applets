package com.becker.puzzle.common;

import java.util.List;
import java.util.Set;

/**
 * PuzzleController constructor.
 * <p/>
 * Abstraction for puzzles like the 'sliding blocks puzzle'
 *The type parameters P and M correspond to a position (state) and a move (transition from one state to the next).
 *
 * @author Brian Goetz, and Tim Peierls
 */
public interface PuzzleController<P, M> {
    
    P initialPosition();

    /**
     * @return true if the position is the goal state.
     */
    boolean isGoal(P position);

    /**
     *@return a list of legal next immutable moves.
     */
    List<M> legalMoves(P position);

    /**
     * @return the position (immutable) that you get after applying the specified move.
     */
    P move(P position, M move);
    
    /**
     * Add the position to the seen set of position if not already seen.
     *
     * @param position to check
     * @param seen Map of seen positions. 
     * @return true if the specified position was already seen (possibly taking into account symmetries).
     */
    boolean alreadySeen(P position, Set<P> seen);
    
    /**
     *specify the algorithm to use.
     */
    void setAlgorithm(AlgorithmEnum algorithm);

    /**
     *get the algorithm to use.
     */
    AlgorithmEnum getAlgorithm();
    
    /**
     *begin the search to find a solution to the puzzle.
     */
    void startSolving();          
}
