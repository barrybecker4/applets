/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.twoplayer.blockade.board;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.game.common.board.BoardPosition;
import com.barrybecker4.game.common.board.GamePiece;
import com.barrybecker4.game.twoplayer.blockade.board.move.wall.BlockadeWall;
import com.barrybecker4.game.twoplayer.blockade.board.path.Path;
import com.barrybecker4.game.twoplayer.blockade.board.path.PathList;


/**
 * The BlockadeBoardPosition describes the physical markers at a location on the board.
 * It can be empty or occupied. If occupied, then one of the BlockadePieces is there and it has an owner.
 * BlockadeBoardPositions may have BlockadeWalls present when unoccupied.
 * @@ split out PathCache class
 *
 * @see BlockadeBoard
 * @author Barry Becker
 */
public final class BlockadeBoardPosition extends BoardPosition {

    /** the walls to find if the north or west sides are blocked we must examine the north and west bordering positions respectively. */
    private BlockadeWall southWall_ = null;
    private BlockadeWall eastWall_ = null;

    /** This is a temporary state that is used for some traversal operations. */
    private boolean visited_;

    private boolean isPlayer1Home_ = false;
    private boolean isPlayer2Home_ = false;

    /** Cache the most recent shortest paths to opponent homes so we do not have to keep recomputing them. */
    private PathList cachedPaths_ = null;


    /**
     * create a new blockade position.
     * @param loc location.
     * @param piece the piece at this position if there is one (use null if no stone).
     */
    public BlockadeBoardPosition( Location loc, GamePiece piece, BlockadeWall southWall, BlockadeWall eastWall,
                                  boolean isP1Home, boolean isP2Home) {
        super( loc, piece );
        visited_ = false;
        southWall_ = southWall;
        eastWall_ = eastWall;
        isPlayer1Home_ = isP1Home;
        isPlayer2Home_ = isP2Home;
    }

    /**
     * Copy constructor
     */
    public BlockadeBoardPosition(BlockadeBoardPosition pos) {
        this(pos.getLocation(), pos.getPiece(), pos.southWall_, pos.eastWall_,
             pos.isPlayer1Home_, pos.isPlayer2Home_);
    }

    /**
     * create a new blockade position.
     * Simple version of the above constructor for when we just want a generic position based on the row and column.
     * @param row location.
     * @param col location.
     */
    public BlockadeBoardPosition( int row, int col) {
        this( new Location(row, col), null, null, null, false, false);
    }

    /**
     * Reuse previously computed shortest paths if they are still valid.
     * Caching can cause a subtle problems were it is invalid, so I turned it off.
     * @param wall the most recently placed wall
     * @return list of shortest paths.
     */
    public PathList findShortestPaths(BlockadeBoard board, BlockadeWall wall) {

        PathList paths = cachedPaths_;
        // Why didn't caching work like I hoped?
        // Seems that walls have a more subtle influence on the path than I thought.
        paths = board.findShortestPaths(this);

        /* possibly remove
        if (isPathCacheBroken(wall, board)) {
            paths = board.findShortestPaths(this);
            cachedPaths_ = paths;
        }  else {
            paths = board.findShortestPaths(this);
            for (int i = 0; i < paths.size(); i++) {
                Path p = paths.get(i);
                Path cp = cachedPaths_.get(i);
                assert (p.equals(cp)) : p +" was not equal to "+cp +" wall placed was "+ wall;
            }
        }    */

        return paths;
    }

    /**
     * make it show an empty board position.
     */
    @Override
    public void clear() {
        super.clear();
        cachedPaths_ = null;
    }

    /**
     * The cache is broken if there the wall blocks one of our cached paths.
     */
    public boolean isPathCacheBroken(BlockadeWall wall, BlockadeBoard board) {
        // If there was no wall placed last move, the paths must still be valid.
        if (wall == null) {
            return false;
        }
        // if nothing cached, we need to create the cache.
        if (cachedPaths_ == null) {
            return true;
        }
        // broken if any of the paths to opponent home is blocked by the recent wall.
        for (Path path : cachedPaths_) {
            if (path.isBlockedByWall(wall, board)) {
                return true;
            }
        }
        return false;
    }

    /**
     * create a deep copy of this position.
     */
    @Override
    public BoardPosition copy() {
        return new BlockadeBoardPosition(this);
    }

    /**
     * @param wall the wall to set south of this position.
     */
    public void setSouthWall( BlockadeWall wall ) {
        southWall_ = wall;
    }

    /**
     * @return the south wall, if any.
     */
    public BlockadeWall getSouthWall() {
        return southWall_;
    }

    /**
     * @param wall the wall to set east of this position.
     */
    public void setEastWall( BlockadeWall wall ) {
        eastWall_ = wall;
    }

    /**
     * @return  the east wall, if any.
     */
    public BlockadeWall getEastWall() {
        return eastWall_;
    }


    public void setVisited( boolean visited ) {
        visited_ = visited;
    }

    public boolean isVisited() {
        return visited_;
    }

    /**
     * @return  true if the path from this cell is blocked to the south.
     */
    public boolean isSouthBlocked() {
        return (southWall_ != null);
    }

    /**
     *
     * @return  true if the path from this cell is blocked to the east.
     */
    public boolean isEastBlocked() {
        return (eastWall_ != null);
    }

    /**
     * @return  true if the path from this cell is open to the south.
     */
    public boolean isSouthOpen() {
        return (southWall_ == null);
    }

    /**
     * @return  true if the path from this cell is open to the east.
     */
    public boolean isEastOpen() {
        return (eastWall_ == null);
    }

    /**
     * @return true if this position is a home base.
     */
    public boolean isHomeBase() {
        return (isPlayer1Home_ || isPlayer2Home_);
    }

    /**
     * @return true if this position is a home base for the specified player.
     */
    public boolean isHomeBase(boolean player1) {
        return (player1? isPlayer1Home_: isPlayer2Home_);
    }

    public BlockadeBoardPosition getNeighbor(Direction d, BlockadeBoard board) {
        int row = getRow();
        int col = getCol();
        Location offset = d.getOffset();
        return (BlockadeBoardPosition) board.getPosition(row + offset.getRow(), col + offset.getCol());
    }

    /**
     * There are 12 unique states for a position. 4 ways the walls can be arranged around the position.
     * @return state index
     */
    public int getStateIndex() {
        int ownerState = (isOccupied()? (getPiece().isOwnedByPlayer1()? 2:3) : 1);
        return ownerState * (1 + ((southWall_==null? 0:2) + (eastWall_==null? 0:1))) - 1;
    }
}



