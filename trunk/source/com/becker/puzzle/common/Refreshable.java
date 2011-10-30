/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.puzzle.common;

import java.util.List;

/**
 * A UI element that can be refreshed to show the current state.
 *
 * @author Barry Becker
 */
public interface Refreshable <P, M> {
    
    /**
     * Call when you want the UI to update.
     * @param pos if the current position to show.
     * @param numTries number of tries so far.
     */
    void refresh(P pos, long numTries);
    
    /**
     *Show the path to the solution at the end.
     *@param path list of moves that gets to the solution. If path is null then there was not solution found.
     *@param position the final board state.
     *@param numTries number of tries it took to find that final state.
     *@param elapsedMillis number of milliseconds it took to find the solution.
     */
    void finalRefresh(List<M> path, P position, long numTries, long elapsedMillis);
    
    /**
     *Make a sound of some sort
     */
    void makeSound();

}
