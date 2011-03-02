package com.becker.game.twoplayer.go.board.update;

import com.becker.game.twoplayer.go.board.move.GoMove;
import com.becker.game.twoplayer.go.board.GoProfiler;
import com.becker.game.twoplayer.go.board.BoardValidator;
import com.becker.game.twoplayer.go.board.GoBoard;
import com.becker.game.twoplayer.go.board.analysis.neighbor.NeighborAnalyzer;
import com.becker.game.twoplayer.go.board.elements.*;

/**
 * Base class for classes responsible for updating a go board after making or undoing a move.
 *
 * @author Barry Becker
 */
public abstract class PostChangeUpdater {

    GoBoard board_;
    CaptureCounts captureCounter_;
    NeighborAnalyzer nbrAnalyzer_;
    BoardValidator validator_;
    GoProfiler profiler_;

    /**
     * Update the board information data after a change has been made (like an add or a remove of a stone)
     * @param board board that changed.
     * @param captureCounter captures added or removed during the change
     */
    PostChangeUpdater(GoBoard board, CaptureCounts captureCounter) {
        board_ = board;
        captureCounter_ = captureCounter;
        profiler_ = GoProfiler.getInstance();
        nbrAnalyzer_ = new NeighborAnalyzer(board);
        validator_ = new BoardValidator(board);
    }

    /**
     * Update the strings and groups on the board after a move change (move or remove).
     * @param move the move that was just made or undone.
     */
    public abstract void update( GoMove move );

    GoBoard getBoard() {
        return board_;
    }

    GoGroupSet getAllGroups() {
        return board_.getGroups();
    }

    /**
     * The structure of the groups can change after a move.
     * First remove all the groups, and then find them again.
     */
    protected void recreateGroupsAfterChange() {

        GoGroupSet groups = new GoGroupSet();

        addFluxGroups(groups);

        board_.setGroups(groups);
        board_.unvisitAll();
    }

    /**
     * The structure of the groups can change after a move.
     * First remove all the groups that neighbor the position that changed, then rediscover them.
     * @param pos the board position that just changed.
     *
    protected void recreateGroupsAfterChange(GoBoardPosition pos) {

        GoGroupSet groups = new GoGroupSet(board_.getGroups());
        GoBoardPositionSet groupNbrs = nbrAnalyzer_.findGroupNeighbors(pos, true, false);

        groups.removeGroupForStone(pos);
        for (GoBoardPosition nbr : groupNbrs)  {
            groups.removeGroupForStone(nbr);
        }
        for (GoGroup group : groups)  {
            group.setVisited(true);
        }

        addFluxGroups(groups);

        board_.setGroups(groups);
        board_.unvisitAll();
    } */

    /**
     * Add back the groups that are in flux due to the changed position.
     */
    private void addFluxGroups(GoGroupSet groups) {
        for ( int i = 1; i <= getBoard().getNumRows(); i++ )  {
           for ( int j = 1; j <= getBoard().getNumCols(); j++ ) {
               GoBoardPosition seed = (GoBoardPosition)getBoard().getPosition(i, j);
               if (seed.isOccupied() && !seed.isVisited()) {
                   GoBoardPositionList newGroup = nbrAnalyzer_.findGroupFromInitialPosition(seed, false);
                   GoGroup g = new GoGroup(newGroup);
                   groups.add(g);
               }
           }
        }
    }

    /**
     * remove groups that have no stones in them.
     */
    void cleanupGroups() {
        GoGroupSet newGroups = new GoGroupSet();

        for (IGoGroup group: getAllGroups()) {

            if ( group.getNumStones() > 0 )  {
                newGroups.add(group);
            }
        }
        board_.setGroups(newGroups);
    }
}