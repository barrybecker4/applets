package com.becker.puzzle.sudoku.model;

import com.becker.common.math.MathUtil;
import com.becker.puzzle.sudoku.data.TestData;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.xml.bind.SchemaOutputResolver;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Barry Becker Date: Jul 3, 2006
 */
public class TestBoard extends TestCase {

    /** instance under test */
    Board board;


    @Override
    public void setUp() {
        MathUtil.RANDOM.setSeed(1);
    }

    public void testBoardConstruction() {
        Board board = new Board(3);

        Board expectedBoard = new Board(new int[][] {
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0},
            {0,0,0, 0,0,0, 0,0,0}
        });
        assertEquals("Unexpected board constructed", expectedBoard, board);
    }

    public void testFindCellCandidatesForAll() {

        board = new Board(TestData.SIMPLE_4);
        Candidates[][] expCands = {
                {new Candidates(1, 2, 3), null,                      new Candidates(1, 3),   new Candidates(1, 3)},
                {new Candidates(1, 3),    new Candidates(1),         null,                   new Candidates(1, 3, 4)},
                {null,                    null,                      new Candidates(1),        new Candidates(1, 2)},
                {new Candidates(1, 2),    new Candidates(1, 2),      new Candidates(1, 3, 4),   new Candidates(1, 2, 3, 4)}
        };

        boolean valid = true;
        for (int i=0; i<board.getEdgeLength(); i++) {
            for (int j=0; j<board.getEdgeLength(); j++) {
                Candidates cands = board.getCandidates(i, j);
                if (expCands[i][j] != cands) valid = false;
            }
        }
        if (!valid) {
            System.out.println("baord = " + board);
        }
        for (int i=0; i<board.getEdgeLength(); i++) {
            for (int j=0; j<board.getEdgeLength(); j++) {
                Candidates cands = board.getCandidates(i, j);
                Assert.assertEquals( "Did find correct candidates for cell row=" + i + " j="+ j,
                    expCands[i][j], cands);
            }
        }
    }

    public void testFindShuffledCellCandidates2() {

        board = new Board(TestData.SIMPLE_4);

        ValuesList cands = ValuesList.getShuffledCandidates(board.getCell(0).getCandidates());
        checkCandidates(Arrays.asList(2, 3, 1), cands);
    }

    public void testFindShuffledCellCandidates3() {

        board = new Board(TestData.SIMPLE_9);

        ValuesList cands = ValuesList.getShuffledCandidates(board.getCell(0).getCandidates());
        checkCandidates(Arrays.asList(3, 5), cands);

        cands = ValuesList.getShuffledCandidates(board.getCell(1).getCandidates());
        checkCandidates(Arrays.asList(4, 5, 3, 1), cands);

        cands = ValuesList.getShuffledCandidates(board.getCell(2).getCandidates());
        List<Integer> expList = Collections.emptyList();
        checkCandidates(expList, cands);
    }

    private void checkCandidates(List<Integer> expCands, ValuesList actCands) {
        Assert.assertEquals("Did find correct candidates",
                expCands, actCands);
    }

    public void testNotSolved() {
        board = new Board(TestData.SIMPLE_4);
        assertFalse("Unexpectedly solved", board.solved());
    }


    public void testSolved() {
        board = new Board(TestData.SIMPLE_4_SOLVED);
        assertTrue("Unexpectedly not solved", board.solved());
    }

    /**
     * @return all the junit test cases to run (in this class).
     */
    public static Test suite() {
        return new TestSuite(TestBoard.class);
    }
}
