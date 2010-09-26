package com.becker.game.twoplayer.blockade;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *  The BlockadeWall describes the physical marker at a location on the board.
 *  Its either a vertical or horizontal wall.
 *  Not that there is no player ownership for walls (that is why we do not extend GamePiece).
 *  Both players have to abide by them equally regardless of who placed them.
 *
 * @see BlockadeBoard
 * @author Barry Becker
 */
public class BlockadeWall
{
    /** whether the wall is VERTICAL or HORIZONTAL */
    private boolean isVertical_;

    /** the BlockadeBoardPosition that contain the wall (on the south or east faces depending on the orientation). */
    private LinkedHashSet<BlockadeBoardPosition> positions_;

    /**
     * constructor
     */
    public BlockadeWall( Set<BlockadeBoardPosition> positions)
    {
        Iterator<BlockadeBoardPosition> it = positions.iterator();
        init(it.next(), it.next());
    }
     
    /**
     * Create a new wall between p1 and p2 and vertical is isVertical is true.
     */
    public BlockadeWall(BlockadeBoardPosition p1, BlockadeBoardPosition p2)
    {
         init(p1, p2);
    }

    private void init(BlockadeBoardPosition p1, BlockadeBoardPosition p2) {
         LinkedHashSet<BlockadeBoardPosition> hsPositions = new LinkedHashSet<BlockadeBoardPosition>( 2 );
         hsPositions.add( p1 );
         hsPositions.add( p2 );
         isVertical_ = p1.getCol() == p2.getCol();
         positions_ = hsPositions;
    }    
        
    /**
     * @param wall  the wall to compare to.
     * @return  true if values are equal.
     */
    @Override
    public boolean equals( Object wall )
    {
         BlockadeWall comparisonWall = (BlockadeWall) wall;
         if (this.isVertical_ != comparisonWall.isVertical_ ||
                 !this.getFirstPosition().getLocation().equals(comparisonWall.getFirstPosition().getLocation())) {
             return false;
         }
         // for (BlockadeBoardPosition pos: positions_) {
         //     if (!comparisonWall.getPositions().contains(pos))
         //         return false;
         // }
         return true;
    }
    
    @Override
    public int hashCode() {
         int hashcode = 0;
         for (BlockadeBoardPosition pos: positions_) {
             hashcode += pos.hashCode();
         }
         return hashcode;
    }

    /**
     *
     * @return true if this is a vertically oriented wall.
     */
    public boolean isVertical()
    {
        return isVertical_;
    }

    /**
     * @return  the positions bordered by this wall.
     */
    public Set<BlockadeBoardPosition> getPositions()
    {
        return positions_;
    }
    
    /**
     * @return either the top/north or the left/west board position 
     *  depending on whether this is a vertical or horizotnal wall, respectively.
     */
    public BlockadeBoardPosition getFirstPosition() {
        Iterator<BlockadeBoardPosition> it = positions_.iterator();
        BlockadeBoardPosition pos1 = it.next();
        BlockadeBoardPosition pos2 = it.next();
        if (isVertical_) {
            return (pos1.getRow() < pos2.getRow())? pos1 : pos2;
        }
        else {
            return (pos1.getCol() < pos2.getCol())? pos1 : pos2;
        }
    }
    

    /**
     *   create a deep copy of the position.
     */
    public BlockadeWall copy()
    {
        return new BlockadeWall(positions_);
    }

    @Override
    public String toString()
    {
        // we may also want to include the position that the wall is at.
        StringBuffer buf = new StringBuffer("wall: "+(isVertical()?"V":"H"));
        Iterator<BlockadeBoardPosition> it = positions_.iterator();
        while (it.hasNext()) {
            BlockadeBoardPosition pos = it.next();
            buf.append(pos.toString()+(isVertical()?"eastBlocked ":"southBlocked "));
        }
        return buf.toString();
    }
}



