/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.blockade.analysis;

import com.becker.common.geometry.Location;
import com.becker.game.common.GameContext;
import com.becker.game.common.board.BoardPosition;
import com.becker.game.twoplayer.blockade.BlockadeBoard;
import com.becker.game.twoplayer.blockade.BlockadeBoardPosition;
import com.becker.game.twoplayer.blockade.BlockadeMove;
import com.becker.game.twoplayer.blockade.Direction;

import java.util.LinkedList;
import java.util.List;

/**
 * Finds list of all possible next moves given a current position..
 *
 * @author Barry Becker
 */
class PossibleMoveAnalyzer {

    private BlockadeBoard board;

    private BlockadeBoardPosition pos;
    private Location fromLocation;

    /** true if opposing player is player1; false if player2. */
    private boolean opponentPlayer1;

    private List<BlockadeMove> possibleMoveList;

    private BlockadeBoardPosition westPos;
    private BlockadeBoardPosition eastPos;
    private BlockadeBoardPosition northPos;
    private BlockadeBoardPosition southPos;


    /**
     * Constructor.
     * @param position we are moving from
     * @param op1 true if opposing player is player1; false if player2.
     */
    public PossibleMoveAnalyzer(BlockadeBoard board, BoardPosition position, boolean op1) {
        this.board = board;
        this.opponentPlayer1 = op1;
        this.pos = (BlockadeBoardPosition)position;
        fromLocation = pos.getLocation();
        possibleMoveList = new LinkedList<BlockadeMove>();

        westPos = pos.getNeighbor(Direction.WEST, board);
        eastPos = pos.getNeighbor(Direction.EAST, board);
        northPos = pos.getNeighbor(Direction.NORTH, board);
        southPos = pos.getNeighbor(Direction.SOUTH, board);
    }

    /**
     * Blockade pieces can move 1 or 2 spaces in any direction.
     * However, only in rare cases would you ever want to move only 1 space.
     * For example, move 1 space to land on a home base, or in preparation to jump an oppponent piece.
     * They may jump over opponent pieces that are in the way (but they do not capture it).
     * The wall is ignored for the purposes of this method.
     *     Moves are only allowed if the candidate position is unoccupied (unless a home base) and if
     * it has not been visited already. The visited part is only significant when we are doing a traversal
     * such as when we are finding the shortest paths to home bases.
     * <pre>
     *       #     There are at most 12 moves from this position
     *     #*#    (some of course may be blocked by walls)
     *   #*O*#    The most common being marked with #'s.
     *     #*#
     *       #
     * </pre>
     *
     * We only add the one space moves if
     *   1. jumping 2 spaces in that direction would land on an opponent pawn,
     *   2. or moving one space moves lands on an opponent home base.
     *
     * @return a list of legal piece movements
     */
    public List<BlockadeMove> getPossibleMoveList()
    {
        possibleMoveList.clear();

        boolean eastOpen = !pos.isEastBlocked() && eastPos != null;                 // E
        addIf1HopNeeded(eastOpen, eastPos, 0, 1);

        boolean southOpen = !pos.isSouthBlocked() && southPos != null;              // S
        addIf1HopNeeded(southOpen, southPos, 1, 0);

        boolean westOpen = checkSouthOptions(eastOpen, southOpen);
        checkWest(westOpen);
        checkNorthOptions(eastOpen, westOpen);
        checkEast(eastOpen);

        return possibleMoveList;
    }


    private boolean checkSouthOptions(boolean eastOpen, boolean southOpen) {
        if (southPos != null ) {
             Location toLocation = fromLocation.incrementOnCopy(2, 0);
             addIf2HopLegal(southOpen, southPos.isSouthBlocked(), toLocation);                  // SS

             BlockadeBoardPosition southEastPos = pos.getNeighbor(Direction.SOUTH_EAST, board);
             addIfDiagonalLegal(southEastPos, eastOpen && !eastPos.isSouthBlocked(),
                                southOpen && !southPos.isEastBlocked());                        // SE
        }

        boolean westOpen = false;
        if (westPos != null)  {
            BlockadeBoardPosition southWestPos = pos.getNeighbor(Direction.SOUTH_WEST, board);
            westOpen = (!westPos.isEastBlocked());                                                  // W
            addIf1HopNeeded(westOpen, westPos, 0, -1);

            addIfDiagonalLegal(southWestPos, westOpen && !westPos.isSouthBlocked(),
                               southOpen && !southWestPos.isEastBlocked());                        // SW
        }
        return westOpen;
    }

    private void checkWest(boolean westOpen) {
        BlockadeBoardPosition westWestPos = pos.getNeighbor(Direction.WEST_WEST, board);
        if (westWestPos != null) {
           Location toLocation = fromLocation.incrementOnCopy(0, -2);
           addIf2HopLegal(westOpen, westWestPos.isEastBlocked(), toLocation);                    // WW
        }
    }

    private void checkNorthOptions(boolean eastOpen, boolean westOpen) {
        boolean northOpen = false;
        if (northPos != null) {
            BlockadeBoardPosition northEastPos = pos.getNeighbor(Direction.NORTH_EAST, board);
            northOpen = (!northPos.isSouthBlocked()) ;                                              // N
            addIf1HopNeeded(northOpen, northPos, -1, 0);
            addIfDiagonalLegal(northEastPos, eastOpen && !northEastPos.isSouthBlocked(),
                                northOpen && !northPos.isEastBlocked());                          // NE
        }

        BlockadeBoardPosition northNorthPos = pos.getNeighbor(Direction.NORTH_NORTH, board);
        if (northNorthPos != null) {
            Location toLocation = fromLocation.incrementOnCopy(-2, 0);
            addIf2HopLegal(northOpen, northNorthPos.isSouthBlocked(), toLocation);                   // NN
        }

        BlockadeBoardPosition northWestPos = pos.getNeighbor(Direction.NORTH_WEST, board);
        if (northWestPos != null) {
            addIfDiagonalLegal(northWestPos, westOpen && !northWestPos.isSouthBlocked(),
                              northOpen && !northWestPos.isEastBlocked());                      // NW
        }
    }

    private void checkEast(boolean eastOpen) {
        if (eastPos != null) {
            Location toLocation = fromLocation.incrementOnCopy(0, 2);
            addIf2HopLegal(eastOpen, eastPos.isEastBlocked(), toLocation);                       // EE
        }
    }


    /**
     * Check for needed 1 space moves (4 cases).
     * A one space move is needed if one of 2 conditions arise:
     *   1. jumping 2 spaces in that direction would land on an opponent pawn,
     *   2. or moving one space moves lands on an opponent home base.
     */
     private void addIf1HopNeeded(boolean directionOpen, BlockadeBoardPosition dirPosition,
                                  int rowOffset, int colOffset) {

         int fromRow = fromLocation.getRow();
         int fromCol = fromLocation.getCol();
         BlockadeBoardPosition dirDirPosition =
                 (BlockadeBoardPosition) board.getPosition(fromRow + 2 * rowOffset, fromCol + 2 * colOffset);
         // if either the players own pawn or that of the opponent is blocking the path, then true
         boolean pawnBlockingPath =
                 (dirDirPosition!=null && dirDirPosition.getPiece()!=null );
         if (directionOpen && !dirPosition.isVisited() &&
                  (pawnBlockingPath || dirPosition.isHomeBase(opponentPlayer1))) {
              possibleMoveList.add(
                      BlockadeMove.createMove(new Location(fromRow, fromCol),
                                              new Location(fromRow + rowOffset, fromCol + colOffset),
                                              0, pos.getPiece(), null));
               GameContext.log(2, "ADDED 1 HOP" + dirPosition);
          }
     }
     
     /**
      * Check for 2 space moves (4 cases).
      */
     private void addIf2HopLegal(boolean directionOpen, boolean blocked,
                                 Location toLocation) {
         
         BlockadeBoardPosition dirDirPosition =  (BlockadeBoardPosition) board.getPosition(toLocation);
         if (directionOpen && (dirDirPosition != null) && !blocked
              && (dirDirPosition.isUnoccupied() || dirDirPosition.isHomeBase(opponentPlayer1)) && !dirDirPosition.isVisited()) { //DD
               possibleMoveList.add(
                       BlockadeMove.createMove(fromLocation, toLocation, 0, pos.getPiece(), null));
         }
     }
     
     /**
      * Check for diagonal moves (4 cases).
      * In some rare cases we may add 1 space moves if the diagonal is occupied by another pawn.
      */
     private void addIfDiagonalLegal(BlockadeBoardPosition diagonalPos,
                                     boolean horizontalPathOpen, boolean verticalPathOpen) {
          if (diagonalPos == null)
              return;

          // check the 2 alternative paths to this diagonal position to see if either are clear.
          if ((horizontalPathOpen || verticalPathOpen)
               &&  (diagonalPos.isUnoccupied() || diagonalPos.isHomeBase(opponentPlayer1) && !diagonalPos.isVisited())) {  //Diag
                possibleMoveList.add(
                   BlockadeMove.createMove(fromLocation, diagonalPos.getLocation(), 0, pos.getPiece(), null));
          }
          else if (diagonalPos.isOccupied()) {
              // if the diagonal position that we want to move to is occupied, we try to add the 1 space moves

              BlockadeBoardPosition horzPos = (BlockadeBoardPosition) board.getPosition(fromLocation.getRow(), diagonalPos.getCol());
              BlockadeBoardPosition vertPos = (BlockadeBoardPosition) board.getPosition(diagonalPos.getRow(), fromLocation.getCol());

              if (horizontalPathOpen && horzPos.isUnoccupied() && !horzPos.isVisited()) {
                   possibleMoveList.add(
                          BlockadeMove.createMove(fromLocation, horzPos.getLocation(), 0, pos.getPiece(), null));
              } 
              if (verticalPathOpen && vertPos.isUnoccupied() && !vertPos.isVisited()) {
                   possibleMoveList.add(
                          BlockadeMove.createMove(fromLocation, vertPos.getLocation(), 0, pos.getPiece(), null));
              }
          }
     }
}
