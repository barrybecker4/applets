package com.becker.game.twoplayer.go.board;

import com.becker.game.twoplayer.go.board.analysis.neighbor.NeighborAnalyzer;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Assert certain things are true about the board.
 * Helpful for debugging.
 * 
 * @author Barry Becker
 */
public class BoardValidator {

    private GoBoard board_;

    public BoardValidator(GoBoard board) {
        board_ = board;
    }

    /**
     * Confirm no empty strings, stones in valid groups, all stones in unique groups, and all stones in groups claimed.
     * @param pos position to check
     */
    public void consistencyCheck(GoBoardPosition pos) {
        board_.getGroups().confirmNoEmptyStrings();
        confirmStonesInValidGroups();
        board_.getGroups().confirmAllStonesInUniqueGroups();
        try {
            confirmAllStonesInGroupsClaimed(board_.getGroups());
        } catch (AssertionError e) {
            System.err.println("The move was :" + pos);
            throw e;
        }
    }

    /**
     * 
     * @param seed stone to start checking from .
     * @param list list of stones to verify no duplicates in.
     */
    void confirmNoDupes( GoBoardPosition seed, List list )
    {
        Object[] stoneArray = list.toArray();

        for ( int i = 0; i < stoneArray.length; i++ ) {
            GoBoardPosition st = (GoBoardPosition) stoneArray[i];
            // make sure that this stone is not a dupe of another in the list
            for ( int j = i + 1; j < stoneArray.length; j++ ) {
                assert (!st.equals(stoneArray[j])): "found a dupe=" + st + " in " + list + "]n the seed = " + seed ;
            }
        }
    }

    /**
     * 
     * @param stones
     */
    public void confirmUnvisited( List<GoBoardPosition> stones )
    {
        for (GoBoardPosition pos : stones) {
            assert !pos.isVisited() : pos + " in " + stones + " was visited";
        }
    }

    
    /**
     * verify that all the stones on the board are in the boards member list of groups.
     */
    public void confirmStonesInValidGroups()
    {
        Set groups = board_.getGroups();
        for ( int i = 1; i <= board_.getNumRows(); i++ )  {
            for ( int j = 1; j <= board_.getNumCols(); j++ ) {
                GoBoardPosition space = (GoBoardPosition) board_.getPosition( i, j );
                if ( space.isOccupied() )
                    confirmStoneInValidGroup( space, groups );
            }
        }
    }

    /**
     * @param stone verify that this stone has a valid string and a group in the board's member list.
     */
    private static void confirmStoneInValidGroup( GoBoardPosition stone, final Set groups )
    {
        GoString str = stone.getString();
        assert ( str!=null) : stone + " does not belong to any string!" ;
        GoGroup g = str.getGroup();
        boolean valid = false;
        Iterator gIt = groups.iterator();
        GoGroup g1;
        while ( !valid && gIt.hasNext() ) {
            g1 = (GoGroup) gIt.next();
            valid = g.equals(g1);
        }
        if ( !valid ) {
            BoardDebugUtil.debugPrintGroups( 0, "Confirm stones in valid groups failed. The groups are:",
                    g.isOwnedByPlayer1(), !g.isOwnedByPlayer1(), groups);
            assert false :
                   "Error: This " + stone + " does not belong to a valid group: " +
                    g + " \nThe valid groups are:" + groups;
        }
    }

    /**
     * verify that all the stones are marked unvisited.
     */
    public void confirmAllUnvisited()
    {
        GoBoardPosition stone = areAllUnvisited();
        if (stone != null)
           assert false : stone + " is marked visited";
    }


    /**
     * verify that all the stones are marked unvisited.
     * @return positiont hat is still marked visited.
     */
    private GoBoardPosition areAllUnvisited()
    {
        for ( int i = 1; i <= board_.getNumRows(); i++ ) {
            for ( int j = 1; j <= board_.getNumCols(); j++ ) {
                GoBoardPosition stone = (GoBoardPosition) board_.getPosition( i, j );
                if (stone.isVisited())
                    return stone;
            }
        }
        return null;
    }


    /**
     * For every stone in every group specified in groups, verify that the group determined from using that stone as a seed
     * matches the group that is claims by ancestry.
     * (expensive to check)
     * @param groups we will check each stone in each of these groups.
     */
    public void confirmAllStonesInGroupsClaimed(Set<GoGroup> groups)
    {
        NeighborAnalyzer na = new NeighborAnalyzer(board_);
        for (GoGroup parentGroup : groups) {  // for each group on the board
            GoBoardPositionSet parentGroupStones = parentGroup.getStones();
            for (GoBoardPosition stone : parentGroupStones) {  // for each stone in that group
                // compute the group from this stone and confirm it matches the parent group
                List<GoBoardPosition> g = na.findGroupFromInitialPosition(stone);
                // perhaps we should do something more than check the size.
                if (g.size() != parentGroupStones.size()) {
                    BoardDebugUtil.debugPrintGroups(0,
                            "Confirm stones in groups they Claim failed. \nGroups are:\n", true, true, groups);
                    StringBuilder bldr = new StringBuilder();
                    bldr.append(board_.toString());
                    bldr.append("\n");
                    bldr.append("\n\nIt seems that using different seeds yields different groups:");
                    for (GoBoardPosition stone1 : parentGroupStones) {  // for each stone in that group
                         List<GoBoardPosition> gg = na.findGroupFromInitialPosition(stone);
                        bldr.append(BoardDebugUtil.debugPrintListText(0, "\nSEED STONE = "+ stone1, gg));
                    }
                    System.out.println(bldr.toString());
                    System.out.flush();
                    //assert false :
                    System.out.println(
                            BoardDebugUtil.debugPrintListText(0, "Calculated Group (seeded by " + stone + "):", g)
                                + "\n is not equal to the expected parent group:\n" + parentGroup);
                    System.out.flush();
                }
            }
        }
    }

    static void confirmNoStringsWithEmpties(Set<GoGroup> groups)
    {
        for (GoGroup g : groups)  {
            for (GoString string : g.getMembers()) {
                assert (!string.areAnyBlank()): "There is a string with unoccupied positions: " + string;
            }
        }
    }

    /**
     *  confirm that all the strings in a group have nobi connections.
     */
    static void confirmGroupsHaveValidStrings(Set<GoGroup> groups, GoBoard board)
    {
        for (GoGroup group : groups) {
            confirmValidStrings(group, board);
        }
    }

    /**
     * @param largerGroup
     * @param smallerGroup
     * @return true if larger group contains smaller group.
     */
    static boolean confirmStoneListContains(List<GoBoardPosition> largerGroup, List<GoBoardPosition> smallerGroup)
    {
        for (GoBoardPosition smallPos : smallerGroup) {
            boolean found = false;
            Iterator largeIt = largerGroup.iterator();
            while (largeIt.hasNext() && !found) {
                GoBoardPosition largePos = (GoBoardPosition) largeIt.next();
                if (largePos.getRow() == smallPos.getRow() && largePos.getCol() == smallPos.getCol())
                    found = true;
            }
            if (!found)
                return false;
        }
        return true;
    }
    
    
    /**
     * go through the groups strings and verify that they are valid (have all nobi connections)
     */
    private static void confirmValidStrings(GoGroup group, GoBoard b )
    {
        for (GoString string : group.getMembers()) {
            string.confirmValid(b);
        }
    }

    public static void confirmNoNullMembers(GoGroup group)
    {
        Iterator it = group.getStones().iterator();
        boolean failed = false;
        while (it.hasNext()) {
            GoBoardPosition s = (GoBoardPosition)it.next();
            if (s.getPiece()==null) failed = true;
        }
        if (failed) {
            assert false : "Group contains an empty position: "+group.toString();
        }
    }

}
