package com.becker.game.twoplayer.go.board.move;

import com.becker.game.common.GameContext;
import com.becker.game.common.board.Board;
import com.becker.game.common.board.BoardPosition;
import com.becker.game.common.board.CaptureList;
import com.becker.game.twoplayer.go.board.GoBoard;
import com.becker.game.twoplayer.go.board.analysis.neighbor.NeighborAnalyzer;
import com.becker.game.twoplayer.go.board.elements.*;

import java.util.LinkedList;
import java.util.List;

/**
 *  This class represents a linked list of captured pieces.
 *  It provides convenience methods for removing and restoring those
 *  pieces to a game board.
 *
 *  @author Barry Becker
 */
public class GoCaptureList extends CaptureList {

    public GoCaptureList() {
    }

    /**
     * copy constructor
     */
    public GoCaptureList(CaptureList captureList) {

        super(captureList);
    }

    /**
     * @return a deep copy of the capture list.
     */
    @Override
    public GoCaptureList copy()  {

        return new GoCaptureList(this);
    }

    /**
     * we need to add copies so that when the original stones on the board are
     * changed we don't change the captures
     * @param set set of go stones to add to the capture list. If null, then none will be added.
     * @return true if set is not null or 0 sized.
     */
    public boolean addCaptures(GoBoardPositionSet set) {

        if ( set == null )  {
            return false;
        }

        for (GoBoardPosition capture : set) {
            // make sure none of the captures are blanks
            assert capture.isOccupied();
            add(capture.copy());
        }
        return (!set.isEmpty());
    }

    /**
     * remove the captured pieces from the board.
     */
    @Override
    public void removeFromBoard( Board board ) {
        GoBoard b = (GoBoard) board;
        for (BoardPosition c : this) {
            GoBoardPosition capStone = (GoBoardPosition) c;
            GoBoardPosition stoneOnBoard =
                (GoBoardPosition) b.getPosition(capStone.getLocation());
            stoneOnBoard.clear(b);
        }

        adjustStringLiberties(b);
    }

    /**
     * restore the captured pieces on the board.
     */
    @Override
    public void restoreOnBoard( Board board )  {
        GoBoard b = (GoBoard) board;
        super.modifyCaptures( b, false );

        GameContext.log(3, "GoMove: restoring these captures: " + this);

        GoBoardPositionLists strings = getRestoredStringList(b);  // XXX should remove
        adjustStringLiberties(b);

        // XXX should remove next lines
        IGoGroup group = getRestoredGroup(strings, b);

        assert ( group!=null): "no group was formed when restoring "
                + this + " the list of strings was "+strings;
        b.getGroups().add( group );
    }


    /**
     * update the liberties of the surrounding strings.
     */
    public void adjustStringLiberties(GoBoard b) {
        for (BoardPosition capture : this) {
            GoBoardPosition captured = (GoBoardPosition) capture;
            GoBoardPosition newLiberty = (GoBoardPosition) b.getPosition(captured.getLocation());
            b.adjustLiberties(newLiberty);
        }
    }

    /**
     * There may have been more than one string in the captureList
     * @return list of strings that were restored ont he board.
     */
    private GoBoardPositionLists getRestoredStringList( GoBoard b ) {

        GoBoardPositionList restoredList = getRestoredList(b);
        GoBoardPositionLists strings = new GoBoardPositionLists();
        NeighborAnalyzer nbrAnalyzer = new NeighborAnalyzer(b);
        for (GoBoardPosition s : restoredList) {
            if (!s.isVisited()) {
                GoBoardPositionList string1 = nbrAnalyzer.findStringFromInitialPosition(s, false);
                strings.add(string1);
            }
        }
        return strings;
    }

    /**
     * @return list of captured stones that were restored on the board.
     */
    private GoBoardPositionList getRestoredList(GoBoard b) {

        GoBoardPositionList restoredList = new GoBoardPositionList();
        for (BoardPosition pos : this ) {
            GoBoardPosition capStone = (GoBoardPosition) pos;
            GoBoardPosition stoneOnBoard =
                    (GoBoardPosition) b.getPosition( capStone.getRow(), capStone.getCol() );
            stoneOnBoard.setVisited(false);    // make sure in virgin unvisited state

            // --adjustLiberties(stoneOnBoard, board);
            restoredList.add( stoneOnBoard );
        }
        return restoredList;
    }

    /**
     * @return the group that was restored when the captured stones were replaced on the board.
     */
    private IGoGroup getRestoredGroup(List<GoBoardPositionList> strings, GoBoard b) {
        // ?? form new group, or check group nbrs to see if we can add to an existing one.
        boolean firstString = true;
        IGoGroup group = null;
        for  (GoBoardPositionList stringList : strings) {
            IGoString string = new GoString( stringList, b );
            if ( firstString ) {
                group = new GoGroup( string );
                firstString = false;
            }
            else {
                group.addMember(string);
            }
            string.setVisited(false);
        }
        return group;
    }
}

