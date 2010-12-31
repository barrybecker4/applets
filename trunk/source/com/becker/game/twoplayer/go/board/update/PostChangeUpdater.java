package com.becker.game.twoplayer.go.board.update;

import com.becker.game.common.board.CaptureList;
import com.becker.game.twoplayer.go.GoMove;
import com.becker.game.twoplayer.go.GoProfiler;
import com.becker.game.twoplayer.go.board.BoardValidator;
import com.becker.game.twoplayer.go.board.GoBoard;
import com.becker.game.twoplayer.go.board.analysis.neighbor.NeighborAnalyzer;
import com.becker.game.twoplayer.go.board.elements.*;

import java.util.List;

/**
 * Base class for classes responsible for updating a go board after making or undoing a move.
 *
 * @author Barry Becker
 */
public abstract class PostChangeUpdater {

    GoBoard board_;
    Captures captures_;
    NeighborAnalyzer nbrAnalyzer_;
    BoardValidator validator_;


    /**
     * Update the board information data after a change has been made (like an add or a remove of a stone)
     * @param board board that changed.
     * @param captures captures added or removed during the change
     */
    PostChangeUpdater(GoBoard board, Captures captures) {
        board_ = board;
        captures_ = captures;
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
     * First remove all the current groups then rediscover them.
     */
    protected void recreateGroupsAfterChange() {

        GoProfiler profiler = GoProfiler.getInstance();
        profiler.startRecreateGroupsAfterMove();
        GoGroupSet groups = new GoGroupSet();

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
        profiler.stopRecreateGroupsAfterMove();
        board_.setGroups(groups);
    }


    /**
     * update the liberties of the surrounding strings
     * @param captureList the liberties of the stones in this list will be adjusted.
     */
    void adjustStringLiberties(CaptureList captureList) {
        for (Object capture : captureList) {
            GoBoardPosition captured = (GoBoardPosition) capture;
            GoBoardPosition newLiberty = (GoBoardPosition) board_.getPosition(captured.getRow(), captured.getCol());
            adjustLiberties(newLiberty);
        }
    }

    /**
     * Adjust the liberties on the strings (both black and white) that we touch.
     * @param liberty  - either occupied or not depending on if we are placing the stone or removing it.
     */
    void adjustLiberties(GoBoardPosition liberty) {

         NeighborAnalyzer na = new NeighborAnalyzer(board_);
         GoStringSet stringNbrs = na.findStringNeighbors( liberty );
         for (GoString sn : stringNbrs) {
             sn.changedLiberty(liberty);
         }
    }

    /**
     * remove groups that have no stones in them.
     */
    void cleanupGroups()
    {
        GoGroupSet newGroups = new GoGroupSet();

        for (GoGroup group: getAllGroups()) {

            if ( group.getNumStones() > 0 )  {
                newGroups.add(group);
            }
        }
        board_.setGroups(newGroups);
    }
}