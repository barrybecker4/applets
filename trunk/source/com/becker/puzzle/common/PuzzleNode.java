package com.becker.puzzle.common;
import java.util.*;

import net.jcip.annotations.*;

/**
 * PuzzleNode
 * <p/>
 * Link node for the puzzle solving framework.
 * Contains a puzzle position (immutable state) and a move (transition that got us to that position from the previous state).
 *
 * @author Brian Goetz and Tim Peierls
 */
@Immutable
public class PuzzleNode<P, M> {
    final P position;
    final M move;
    PuzzleNode<P, M> previous;

    public PuzzleNode(P pos, M move, PuzzleNode<P, M> prev) {
        this.position = pos;
        this.move = move;
        this.previous = prev;
    }

    List<M> asMoveList() {
        List<M> solution = new LinkedList<M>();
        for (PuzzleNode<P, M> n = this; n.move != null; n = n.previous) {
            solution.add(0, n.move);
        }
        return solution;
    }
}
