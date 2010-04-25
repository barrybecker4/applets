package com.becker.game.twoplayer.common.search.strategy;

import com.becker.game.twoplayer.common.search.tree.SearchTreeNode;
import com.becker.game.twoplayer.common.search.*;
import com.becker.optimization.parameter.ParameterArray;
import com.becker.game.twoplayer.common.*;
import com.becker.game.twoplayer.common.search.tree.GameTreeViewable;

/**
 * Memory enhanced Test Driver search.
 * This strategy class defines the MTD search algorithm.
 * See http://people.csail.mit.edu/plaat/mtdf.html
 * <pre>
 * function MTDF(root : node_type; f : integer; d : integer) : integer;
 *    g = f;
 *    upperbound = +INFINITY;
 *    lowerbound = -INFINITY;
 *    repeat
 *       if (g == lowerbound)  beta = g + 1;
 *       else beta = g;
 *       g = AlphaBetaWithMemory(root, beta - 1, beta, d);
 *       if (g < beta) upperbound = g;
 *       else lowerbound = g;
 *    until lowerbound >= upperbound;
 *    return g;
 *</pre>
 *
 * @@ add iterative deepening (https://chessprogramming.wikispaces.com/MTD(f))
 * @@ try implementing NegaMaxWithMemory
 * 
 * @author Barry Becker
 */
public final class MtdStrategy implements SearchStrategy
{
    /**
     * The "memory" search strategy to use. Must use memory/cache to avoid researching overhead.
     * Either a memory enhanced negascout or memory enhanced NegaScout would work.
     */
    private SearchStrategy searchWithMemory_;

    /**
     * Constructor.
    */
    public MtdStrategy(SearchStrategy testSearchStrategy )
    {
        searchWithMemory_ = testSearchStrategy;
    }

    /**
     * @inheritDoc
     */
    public TwoPlayerMove search( TwoPlayerMove lastMove,
                                 int alpha, int beta, SearchTreeNode parent )
    {
        TwoPlayerMove selectedMove = searchInternal( lastMove, 0, parent);
        return (selectedMove != null)? selectedMove : lastMove;
    }


    private TwoPlayerMove searchInternal( TwoPlayerMove lastMove, 
                                          int f,  SearchTreeNode parent )
    {
        int g = f;
        int upperBound =  SearchStrategy.INFINITY;
        int lowerBound = -SearchStrategy.INFINITY;

        TwoPlayerMove selectedMove;
        do  {
            int beta = (g == lowerBound)? g + 1 : g;

            //selectedMove = searchWithMemory_.search(lastMove, beta - 1, beta, parent);
            //g = selectedMove.getInheritedValue();
            selectedMove = searchWithMemory_.search(lastMove, -beta + 1, -beta, parent);
            g = -selectedMove.getInheritedValue();

            if (g < beta)  upperBound = g;
            else           lowerBound = g;

            System.out.println("lowerBound =" + lowerBound + " upperBound=" + upperBound  + " beta=" + beta);
        } while (lowerBound < upperBound);
        return selectedMove;
    }


    public final long getNumMovesConsidered()
    {
        return searchWithMemory_.getNumMovesConsidered();
    }

    public final int getPercentDone()
    {
        return  searchWithMemory_.getPercentDone();
    }

    /**
     * Set an optional ui component that will update when the search tree is modified.
     * @param listener listener
     */
    public void setGameTreeEventListener(GameTreeViewable listener) {
         searchWithMemory_.setGameTreeEventListener(listener);
    }

    public void pause()
    {
       searchWithMemory_.pause();
    }

    public final boolean isPaused()
    {
        return searchWithMemory_.isPaused();
    }

    public void continueProcessing()
    {
       searchWithMemory_.continueProcessing();
    }
}