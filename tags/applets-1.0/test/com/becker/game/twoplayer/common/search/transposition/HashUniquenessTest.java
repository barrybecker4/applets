/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.common.search.transposition;

import com.becker.common.geometry.Location;
import com.becker.game.common.Move;
import com.becker.game.common.MoveList;
import com.becker.game.common.board.GamePiece;
import com.becker.game.twoplayer.common.TwoPlayerMove;
import com.becker.game.twoplayer.common.cache.ScoreCache;
import com.becker.game.twoplayer.common.cache.ScoreEntry;
import com.becker.game.twoplayer.tictactoe.TicTacToeBoard;
import junit.framework.TestCase;


/**
 * Various uniqueness tests for hash keys.
 * @author Barry Becker
 */
public class HashUniquenessTest extends TestCase {

    private ZobristHash hash;
    private TicTacToeBoard board;
    private ScoreCache scoreCache;

    private int rowMax;
    private int colMax;

    @Override
    public void setUp() {
        board = new TicTacToeBoard();
        scoreCache = new ScoreCache();
        hash = createZobristHash();
        rowMax = 3;
        colMax = 3;
    }

   /**
    * We will play every possible tic tac toe move and verify that the zobrist hashes generated
    * for unique board positions are unique.
    * In theory, it is possible that there are collisions, but i n practice it should be exceedingly rare.
    * To simplify the number of total moves we will just consider the 2 spaces in the upper left of the ttt board.
    */
    public void testNoCacheStatisticsTwoByOneBoard() {

        rowMax = 1;
        colMax = 2;
        playAllMoves(0);
        verifyScoreCacheStats(0, 4);
    }

    /**
     * Same as above but now just run for the 3 spaces in the top row.
     */
    public void testNoCacheStatisticsThreeByOneBoard() {

        rowMax = 1;
        colMax = 3;
        playAllMoves(0);
        verifyScoreCacheStats(3, 12);
    }


    /**
     * We will play every possible move on a 2x2 board and verify that the zobrist hashes generated for unique board positions are unique.
     * To simplify the number of total moves we will just consider the 4 spaces in the upper left of the ttt board.
     */
    public void testNoCacheStatisticsTwoByTwo() {

        rowMax = 2;
        colMax = 2;
        playAllMoves(0);
        verifyScoreCacheStats(30, 34);
    }

    /**
     * We will play every possible tic tac toe move and verify that the zobrist hashes generated for unique board positions are unique.
     * In theory it is possible that there are collisions, but i n practice it should be exceedingly rare.
     */
    public void testNoCacheStatisticsEverything() {

        rowMax = 3;
        colMax = 3;
        playAllMoves(0);
        verifyScoreCacheStats(980364, 6045);
    }

    private void verifyScoreCacheStats(int expHits, int expMisses) {
        System.out.println("scoreCache =" + scoreCache.toString());
        assertEquals("Unexpected number of cache entries", expMisses, scoreCache.numEntries());
        assertEquals("Unexpected number of cache hits", expHits, scoreCache.getCacheHits());
        assertEquals("Unexpected number of cache misses", expMisses, scoreCache.getCacheMisses());
    }

    /** Play all possible tic tac toe moves */
    private void playAllMoves(int depth) {

        boolean player1ToMove = depth % 2 == 0;
        MoveList openMoves = findOpenMoves(player1ToMove);

        for (Move m : openMoves) {
            TwoPlayerMove move = (TwoPlayerMove) m;
            board.makeMove(move);

            applyMoveToHash(move.getToLocation());
            HashKey key = hash.getKey();
            ScoreEntry cachedScore = scoreCache.get(key);

            int score = score(board);

            if (cachedScore != null) {
                if (score != cachedScore.getScore()) {
                    assertTrue("Depth ("+depth+") must be 2 or greater before dupes will occur.", depth >= 2);
                    ZobristHash tempHash = createZobristHash();
                    System.out.println("Dupe found: key "//+ key
                        + "\nwas found in cache. The cached value was " + cachedScore
                        + "\nand we computed worth="+ score + " for board=" + board
                        + "\n expected hash is " + tempHash.getKey());
                }
            }  else {
                scoreCache.put(key, new ScoreEntry(score, board.toString()));
            }


            playAllMoves(depth + 1);

             // removes it from hash. must be done before next line.
            applyMoveToHash(move.getToLocation());
            board.undoMove();
        }
    }

    private MoveList findOpenMoves(boolean player1) {

        MoveList moves = new MoveList();
        for (int i=1; i<=rowMax; i++) {
            for (int j=1; j<=colMax; j++) {
                if (board.getPosition(i, j).isUnoccupied())  {
                    moves.add(TwoPlayerMove.createMove(i, j, 0, new GamePiece(player1)));
                }
            }
        }
        return moves;
    }

    private int score(TicTacToeBoard b) {

        double score = 0;
        boolean player1 = ((TwoPlayerMove)b.getMoveList().getLastMove()).isPlayer1();
        for (int i=1; i<=rowMax; i++) {
            for (int j=1; j<=colMax; j++) {
                if (board.getPosition(i, j).isOccupied())  {
                    score += ((i-1)*3 + j -1) * (player1 ? 1 : 10);
                }
            }
        }
        return (int)score;
    }


    private void applyMoveToHash(Location move) {
        int stateIndex = board.getStateIndex(board.getPosition(move));
        hash.applyMove(move, stateIndex);
    }

    private ZobristHash createZobristHash() {
        return new ZobristHash(board, 0, true);
    }
}
