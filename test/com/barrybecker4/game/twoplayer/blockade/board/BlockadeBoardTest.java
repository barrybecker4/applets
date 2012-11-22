/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.twoplayer.blockade.board;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.game.common.GameContext;
import com.barrybecker4.game.common.board.BoardPosition;
import com.barrybecker4.game.common.board.GamePiece;
import com.barrybecker4.game.twoplayer.blockade.BlockadeTestCase;
import com.barrybecker4.game.twoplayer.blockade.board.*;
import com.barrybecker4.game.twoplayer.blockade.board.move.BlockadeMove;
import com.barrybecker4.game.twoplayer.blockade.board.move.wall.BlockadeWall;
import com.barrybecker4.game.twoplayer.blockade.board.path.Path;
import com.barrybecker4.game.twoplayer.blockade.board.path.PathLengths;
import com.barrybecker4.game.twoplayer.blockade.board.path.PathList;
import com.barrybecker4.game.twoplayer.blockade.board.path.PlayerPathLengths;
import junit.framework.Assert;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * @author Barry Becker
 */
public class BlockadeBoardTest extends BlockadeTestCase {

    public void testPositionStates() {

        BlockadeBoardPosition p = new BlockadeBoardPosition(1, 1);

        Assert.assertTrue("no piece or walls", p.getStateIndex() == 0);
        p.setPiece(new GamePiece(true));

        LinkedHashSet<BlockadeBoardPosition> wallPositions =
                new LinkedHashSet<BlockadeBoardPosition>(2);

        Assert.assertTrue("p1 piece and no walls", p.getStateIndex() == 1);

        wallPositions.add(new BlockadeBoardPosition(10, 11));
        wallPositions.add(new BlockadeBoardPosition(10, 12));
        p.setEastWall(new BlockadeWall(wallPositions));
        Assert.assertTrue("p1 piece and east wall", p.getStateIndex() == 3);

        wallPositions.clear();
        wallPositions.add(new BlockadeBoardPosition(10, 12));
        wallPositions.add(new BlockadeBoardPosition(11, 12));
        p.setSouthWall(new BlockadeWall(wallPositions));
        Assert.assertTrue("p1 piece and both walls", p.getStateIndex() == 7);

        p.setPiece(new GamePiece(false));
        Assert.assertTrue("p2 piece and both walls", p.getStateIndex() == 11);
    }

    /**
     * Expected results for possible next move list.
     * For certain locations we expect other than the default.
     * We make a hashMap for these special locations
     * map from location to expected number of moves.
     */
    private static final Map<Location, Integer> P1_NUM_MAP = new HashMap<Location, Integer>() {
        {
             put(new Location(3, 4), 9);
             put(new Location(3, 4), 9);
             put(new Location(3, 8), 9);
             put(new Location(4, 3), 9);
             put(new Location(4, 5), 9);
             put(new Location(4, 9), 9);
             put(new Location(5, 4), 9);
             put(new Location(5, 3), 9);
             put(new Location(5, 7), 6);
             put(new Location(5, 8), 7);
             put(new Location(6, 7), 6);
             put(new Location(6, 8), 6);
             put(new Location(5, 5), 9);
             put(new Location(7, 3), 9);
             put(new Location(7, 5), 9);
             put(new Location(7, 8), 7);
             put(new Location(7, 9), 9);
             put(new Location(8, 3), 9);
             put(new Location(8, 4), 7);
             put(new Location(8, 5), 7);
             put(new Location(8, 6), 6);
             put(new Location(8, 7), 7);
             put(new Location(8, 8), 7);
             put(new Location(8, 9), 9);
             put(new Location(9, 4), 6);
             put(new Location(9, 5), 5);
             put(new Location(9, 6), 6);
             put(new Location(9, 7), 5);
             put(new Location(9, 8), 6);
             put(new Location(10, 3), 8);
             put(new Location(10, 4), 6);
             put(new Location(10, 5), 6);
             put(new Location(10, 7), 7);
             put(new Location(10, 8), 6);
             put(new Location(10, 9), 9);
             put(new Location(10, 10), 7);
             put(new Location(11, 5), 7);
             put(new Location(11, 7), 6);
             put(new Location(11, 8), 6);
             put(new Location(12, 7), 7);
             put(new Location(12, 8), 7);
             put(new Location(13, 10), 6);
         }
    };


    /**
     * Test the list of candidate next moves.
     */
    public void testPossibleMoveList() {

         restore("whitebox/moveList1");
         BlockadeBoard board = (BlockadeBoard)controller_.getBoard();

         // for each position on the board. determine the possible move list for each player
         int numRows = board.getNumRows();
         int numCols = board.getNumCols();
         for ( int row = 1; row <= numRows; row++ ) {
             for ( int col = 1; col <= numCols; col++ ) {
                  BoardPosition position = board.getPosition(row, col);
                  List list1 = board.getPossibleMoveList(position, false);
                  //List list2 = board.getPossibleMoveList( position, true);

                  if (board.isOnEdge(position)) {

                      if (board.isInCorner(position)) {
                           // if in corner, we expect 3 moves
                          verifyMoves(position, list1,  3, P1_NUM_MAP);
                      }
                      else if (row == 2 || col == 2 || (row == numRows-1) || (col == numCols-1)) {
                          // if on edge and one space to corner, we expect 4 moves
                          verifyMoves(position, list1, 4, P1_NUM_MAP);
                      }
                      else {
                           // if the pos is at the edge we expect 6 moves
                          verifyMoves(position, list1,  5, P1_NUM_MAP);
                      }
                  }
                  else if (row == 2 || col == 2 || (row == numRows-1) || (col == numCols-1)) {
                      if (row == col || (row == 2 && col == numCols-1) || (col == 2 && row == numRows-1)) {
                           // if one space out from order we expect 6 moves
                          verifyMoves(position, list1, 6, P1_NUM_MAP);
                      }
                      else {
                           // if the pos is one space from the edge (but not in corner) we expect 7 moves,
                           verifyMoves(position, list1, 7, P1_NUM_MAP);
                      }
                  }
                  else {
                      verifyMoves(position, list1, 8, P1_NUM_MAP);
                  }
              }
         }
    }

    /**
     *
     */
    private static void verifyMoves(BoardPosition position, List player1Moves,  int expectedNumMoves, Map<Location, Integer> p1NumMap) {

        if (p1NumMap.containsKey(position.getLocation())) {
             expectedNumMoves = p1NumMap.get(position.getLocation());
        }
        //if (player1Moves.size() != expectedNumMoves) {
        //    GameContext.log(1, "Expected "+expectedNumMoves+" moves for player1, but got "+player1Moves.size() +":" + player1Moves);
        //}
        Assert.assertTrue("Expected "+expectedNumMoves+" moves for player1, but got "+player1Moves.size() +":" + player1Moves, player1Moves.size() == expectedNumMoves);
    }

    private static final BlockadeMove[][] moves1 =  {
        {
            createMove(6,4, 8,4, new GamePiece(false)),
            createMove(8,4, 10,4,  null),
            createMove(10,4,  11,5, null),
            createMove(11,5,  11,7, null),
            createMove(11,7,  11,8, null),
        },
        {
            createMove(6,4,  8,4,  new GamePiece(false)),
            createMove(8,4,  10,4, null),
            createMove(10,4,  10,2, null),
            createMove(10,2,  11,3, null),
            createMove(11,3,  11,4, null),
        },
        {
            createMove(8,8,  10,8,  new GamePiece(false)),
            createMove(10,8,  11,9, null),
            createMove(11,9,  11,8, null),
        },
        {
            createMove(8,8,  10,8, new GamePiece(false)),
            createMove(10,8,  10,6, null),
            createMove(10,6,  12,6, null),
            createMove(12,6,  13,5, null),
            createMove(13,5,  12,4, null),
            createMove(12,4,  11,4, null),
        },
    };


    private static final BlockadeMove[][] moves2 =  {
        {
            createMove(8,3,  6,3, new GamePiece(true)),
            createMove(6,3,  5,4, null),
            createMove(5,4,  4,4, null),
        },
        {
            createMove(8,3,  6,3, new GamePiece(true)),
            createMove(6,3,  5,4, null),
            createMove(5,4,  4,4, null),
            createMove(4,4,  4,6, null),
            createMove(4,6,  4,8, null),
        },
        {
            createMove(9,8,  8,9, new GamePiece(true)),
            createMove(8,9,  6,9, null),
            createMove(6,9,  4,9, null),
            createMove(4,9,  4,8, null),
        },
        {
            createMove(9,8,  7,8, new GamePiece(true)),
            createMove(7,8,  7,6, null),
            createMove(7,6,  7,4, null),
            createMove(7,4,  5,4, null),
            createMove(5,4,  4,4, null),
        }
    };

    private static BlockadeMove createMove(int r1, int c1, int r2, int c2, GamePiece piece) {
        return new BlockadeMove(new Location(r1, c1), new Location(r2, c2), 0, piece, null);
    }

    private static final Path[] EXPECTED_P1_PATHS = {
       new Path(moves1[0]), new Path(moves1[1]), new Path(moves1[2]), new Path(moves1[3])
    };

    private static final Path[] EXPECTED_P2_PATHS = {
       new Path(moves2[0]), new Path(moves2[1]), new Path(moves2[2]), new Path(moves2[3])
    };


    /**
     * Test that we can accurately determine all the opponent shortest paths.
     */
    public void testFindOpponentShortestPaths() {
         restore("whitebox/shortestPaths1");
         BlockadeBoard board = (BlockadeBoard)controller_.getBoard();

         PathList p1Paths = board.findAllOpponentShortestPaths(true);
         PathList p2Paths = board.findAllOpponentShortestPaths(false);

         // use to get expected results.
         //printPathCreator(p1Paths);
         //printPathCreator(p2Paths);

         // first check that we have the right overall number of paths
         Assert.assertEquals("Unexpected number of paths", 4, p1Paths.size());
         Assert.assertEquals("Unexpected number of paths", 4, p2Paths.size());

         // now check that we have exactly the expected paths
         String p1PathDiff = pathListDiff(EXPECTED_P1_PATHS, p1Paths);
         String p2PathDiff = pathListDiff(EXPECTED_P2_PATHS, p2Paths);
         Assert.assertTrue("p1Path difference=\n" + p1PathDiff,  p1PathDiff.length() == 0);
         Assert.assertTrue("p2Path difference=\n" + p2PathDiff,  p2PathDiff.length() == 0);
    }

    private void printPathCreator(PathList paths) {
        System.out.println("{");
        for (Path p : paths) {
            System.out.println("{");
            int len = p.getLength();
            for (int i=0; i<len; i++) {
                BlockadeMove move = p.get(i);
                System.out.println("    " + move.getConstructorString());
                //if (i < len -1)
                //    System.out.println(", ");
            }
            System.out.println("},");
        }
        System.out.println("}");
    }

    /**
     * @return "" if no difference or the list of differences.
     * (there will be *** after those that do not match)
     */
    private String pathListDiff(Path[] expectedPaths, PathList actualPaths) {
        boolean pathListsDifferent = false;
        StringBuilder diffs= new StringBuilder("Paths compared: \n");

        for (int i = 0; i < expectedPaths.length; i++) {
            Path ep = expectedPaths[i];
            Path ap = actualPaths.get(i);

            String diffMarker = ep.equals(ap) ? "" : "***";
            diffs.append("expected:  ").append(ep).append("actual  :  ").append(ap);
            if (!ep.equals(ap)) {
                diffs.append("   ").append(diffMarker).append(" \n");
                pathListsDifferent = true;
            }
        }
        return pathListsDifferent ? diffs.toString() : "";
    }


    public void testShortestPathLength() {
         restore("whitebox/noMoves2");
         BlockadeBoard board = (BlockadeBoard)controller_.getBoard();
         BlockadeMove lastMove = (BlockadeMove) controller_.getMoveList().getLastMove();

         PlayerPathLengths pLengths = board.findPlayerPathLengths(lastMove);
         GameContext.log(1, pLengths.toString());

         PathLengths expectedP1Lengths = new PathLengths(4, 6, 12);
         PathLengths expectedP2Lengths = new PathLengths(3, 3, 10);

         PathLengths actualP1Lengths = pLengths.getPathLengthsForPlayer(true);
         PathLengths actualP2Lengths = pLengths.getPathLengthsForPlayer(false);

         Assert.assertTrue("Unexpected Player1 Path lengths - " + actualP1Lengths, expectedP1Lengths.equals(actualP1Lengths));
         Assert.assertTrue("Unexpected Player2 Path lengths - " + actualP2Lengths, expectedP2Lengths.equals(actualP2Lengths));
    }

     public void testShortestPaths2() {

         restore("whitebox/noMoves2");
         BlockadeBoard board = (BlockadeBoard)controller_.getBoard();

         GamePiece piece1 = new GamePiece(true); // player 1
         GamePiece piece2 = new GamePiece(false);  // player 2
         BlockadeWall wall1 = new BlockadeWall((BlockadeBoardPosition) board.getPosition(8, 10), (BlockadeBoardPosition) board.getPosition(9, 10));
         BlockadeWall wall2 = new BlockadeWall((BlockadeBoardPosition) board.getPosition(12, 6), (BlockadeBoardPosition) board.getPosition(12, 7));

         BlockadeMove move1 = BlockadeMove.createMove(new Location(8, 11), new Location(6, 11), 1 /*0.1*/, piece2, wall2);
         BlockadeMove move2 = BlockadeMove.createMove(new Location(12, 6), new Location(10, 6), 1 /*0.1*/, piece1, wall1);

         controller_.makeMove(move1);
         controller_.makeMove(move2);

         PlayerPathLengths pLengths = board.findPlayerPathLengths(move2);
         GameContext.log(1, pLengths.toString());

         PathLengths expectedP1Lengths = new PathLengths(7, 9, 12);
         PathLengths expectedP2Lengths = new PathLengths(4, 6, 10);

         PathLengths actualP1Lengths = pLengths.getPathLengthsForPlayer(true);
         PathLengths actualP2Lengths = pLengths.getPathLengthsForPlayer(false);

         Assert.assertTrue("Unexpected Player1 Path lengths - " + actualP1Lengths,  expectedP1Lengths.equals(actualP1Lengths) );
         Assert.assertTrue("Unexpected Player2 Path lengths - " + actualP2Lengths, expectedP2Lengths.equals(actualP2Lengths) );
    }


     private static final int[] EXPECTED_PATHS_LENGTHS = { 0,  11, 12};

     public void testFindShortestPaths() {
         restore("whitebox/shortestPathsCheck");
         BlockadeBoard board = (BlockadeBoard)controller_.getBoard();
         //BlockadeMove lastMove = (BlockadeMove) controller_.getMoveList().getLast();

         BlockadeBoardPosition pos1 = (BlockadeBoardPosition) board.getPosition(2, 2);
         BlockadeBoardPosition pos2 = (BlockadeBoardPosition) board.getPosition(5, 2);

         PathList pLengths1 = board.findShortestPaths(pos1);
         PathList pLengths2 = board.findShortestPaths(pos2);
         //GameContext.log(2, "paths for "+pos1+ " are = "+ pLengths1);
         //GameContext.log(2, "pLengths2 = "+ pLengths2);

         int size = EXPECTED_PATHS_LENGTHS.length;
         int[] lengths = new int[size];
         Assert.assertTrue("Unexpected number of pLengths1="+ pLengths1.size(), pLengths1.size() == 1);
         Assert.assertTrue("Unexpected number of pLengths2="+ pLengths2.size(),  pLengths2.size() == 2);

         lengths[0] = pLengths1.get(0).getLength();
         lengths[1] = pLengths2.get(0).getLength();
         lengths[2] = pLengths2.get(1).getLength();

         for (int i=0; i<size; i++) {
             Assert.assertTrue("Expected len "+  EXPECTED_PATHS_LENGTHS[i] + " but got  "+ lengths[i] ,  lengths[i] == EXPECTED_PATHS_LENGTHS[i]);
         }
    }
}
