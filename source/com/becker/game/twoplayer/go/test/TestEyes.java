package com.becker.game.twoplayer.go.test;

import com.becker.game.twoplayer.go.board.GoEye;
import com.becker.game.twoplayer.go.board.GoGroup;
import com.becker.game.common.*;
import com.becker.game.twoplayer.go.*;
import com.becker.game.twoplayer.go.board.GoBoard;
import junit.framework.*;

import java.util.*;


/**
 * @author Barry Becker
 */
public class TestEyes extends GoTestCase {

    private static final String PATH_PREFIX = "eyes/";

    // simple eye tests
    public void testEyes1() {
        EyeCounts blackEyes = new EyeCounts(0, 2, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_eyes1", blackEyes, whiteEyes);
    }

    public void testEyes2() {
        EyeCounts blackEyes = new EyeCounts(0, 2, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 1, 0);
        checkEyes("problem_eyes2", blackEyes, whiteEyes);
    }

    public void testFalseEye1() {
        EyeCounts blackEyes = new EyeCounts(1, 1, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_eyes3", blackEyes, whiteEyes);
    }

    public void testCornerLife4() {
        EyeCounts blackEyes = new EyeCounts(0, 2, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 0);
        checkEyes("problem_eyes4", blackEyes, whiteEyes);
    }
    
     public void testCornerLife4a() {
        EyeCounts blackEyes = new EyeCounts(0, 2, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 0);
        checkEyes("problem_eyes4a", blackEyes, whiteEyes);
    }

     public void testEyes5() {
        EyeCounts blackEyes = new EyeCounts(0, 1, 0, 1);
        EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 0);
        checkEyes("problem_eyes5", blackEyes, whiteEyes);
    }


    public void testFalsesOnEdge() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(3, 0, 0, 0);
        checkEyes("problem_falseeyes_on_edge", blackEyes, whiteEyes);
    }

    public void testStoneInEye1() {
        EyeCounts blackEyes = new EyeCounts(0, 1, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_stone_in_eye1", blackEyes, whiteEyes);
    }

    public void testStoneInEye2() {
        EyeCounts blackEyes = new EyeCounts(0, 1, 0, 1);
        EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 0);
        checkEyes("problem_stone_in_eye2", blackEyes, whiteEyes);
    }

    public void testStoneInEye3() {
          EyeCounts blackEyes = new EyeCounts(0, 1, 0, 0);
          EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 0);
          checkEyes("problem_stone_in_eye3", blackEyes, whiteEyes);
    }

    /*
    public void testStoneInEye4() {
          EyeCounts blackEyes = new EyeCounts(0, 1, 0, 1);
          EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 0);
          checkEyes("problem_stone_in_eye4", blackEyes, whiteEyes);
   } */



    ////////////////// test the different big eye shapes /////////////////
    /**
     * ***
     */
    public void testBigEye1() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 0);
        checkEyes("problem_bigeye1", blackEyes, whiteEyes);
    }

    /**
     *   *
     *   **
     */
    public void testBigEye2() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_bigeye2", blackEyes, whiteEyes);
    }

    /**
     *  **
     *  **
     */
    public void testBigEye3() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_bigeye3", blackEyes, whiteEyes);
    }

    /**
     *   ***
     *    **
     */
    public void testBigEye4() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_bigeye4", blackEyes, whiteEyes);
    }

    /**
     *   *
     *  ***
     *   **
     */
    public void testBigEye5() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_bigeye5", blackEyes, whiteEyes);
    }

    /**
     *  **
     *  ***
     *   **
     */
    public void testBigEye6() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_bigeye6", blackEyes, whiteEyes);
    }

    /**
     *  ***
     *   *
     */
    public void testBigEye7() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_bigeye7", blackEyes, whiteEyes);
    }

    /**
     *   *
     *  ***
     *   *
     */
    public void testBigEye8() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_bigeye8", blackEyes, whiteEyes);
    }

    ////////////////// test the different big eye shapes in the corner /////////////////
    /**
     * ***
     */
    public void testCornerBigEye1() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_cornerbigeye1", blackEyes, whiteEyes);
    }

    /**
     *   *
     *   **
     */
    public void testCornerBigEye2() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_cornerbigeye2", blackEyes, whiteEyes);
    }

    /**
     *  **
     *  **
     */
    public void testCornerBigEye3() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_cornerbigeye3", blackEyes, whiteEyes);
    }

    /**
     *   ***
     *    **
     */
    public void testCornerBigEye4() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_cornerbigeye4", blackEyes, whiteEyes);
    }

    /**
     *   *
     *  ***
     *   **
     */
    public void testCornerBigEye5() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_cornerbigeye5", blackEyes, whiteEyes);
    }

    /**
     *  **
     *  ***
     *   **
     */
    public void testCornerBigEye6() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_cornerbigeye6", blackEyes, whiteEyes);
    }

    /**
     *  ***
     *   *
     */
    public void testCornerBigEye7() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_cornerbigeye7", blackEyes, whiteEyes);
    }

    /**
     *   *
     *  ***
     *   *
     */
    public void testCornerBigEye8() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 1, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_cornerbigeye8", blackEyes, whiteEyes);
    }

    ///////////////// check for territorial eyes

    /**
      *  ****
      *  ****
      *  ****
      *  ****
      */
     public void testTerritoryEye() {
         EyeCounts blackEyes = new EyeCounts(0, 1, 0, 0);
         EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 1);
         checkEyes("problem_terreye5", blackEyes, whiteEyes);
     }


    /**
     *   ****
     */
    public void testTerritoryEye1() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 0, 1);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_terreye1", blackEyes, whiteEyes);
    }

    /**
     *  ***
     *  *
     */
    public void testTerritoryEye2() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 0, 1);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_terreye2", blackEyes, whiteEyes);
    }

    /**
     *  ***
     *  ***
     */
    public void testTerritoryEye3() {
        EyeCounts blackEyes = new EyeCounts(0, 0, 0, 1);
        EyeCounts whiteEyes = new EyeCounts(0, 1, 0, 0);
        checkEyes("problem_terreye3", blackEyes, whiteEyes);
    }

    /**
     *  ***
     *  ***
     *  ***
     */
    public void testTerritoryEye4() {
        EyeCounts blackEyes = new EyeCounts(0, 1, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 1);
        checkEyes("problem_terreye4", blackEyes, whiteEyes);
    }

    /**
     *  ****
     *  ****
     *  ****
     *  ****
     */
    public void testTerritoryEye5() {
        EyeCounts blackEyes = new EyeCounts(0, 1, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 1);
        checkEyes("problem_terreye5", blackEyes, whiteEyes);
    }

    ///////////////// check for territorial eyes in the corner

    /**
     *   ****
     */
    public void testCornerTerritoryEye1() {
        EyeCounts blackEyes = new EyeCounts(0, 1, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 1);
        checkEyes("problem_cornerterreye1", blackEyes, whiteEyes);
    }

    /**
     *  ***
     *  *
     */
    public void testCornerTerritoryEye2() {
        EyeCounts blackEyes = new EyeCounts(0, 1, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 1);
        checkEyes("problem_cornerterreye2", blackEyes, whiteEyes);
    }

    /**
     *  ***
     *  ***
     */
    public void testCornerTerritoryEye3() {
        EyeCounts blackEyes = new EyeCounts(0, 1, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 1);
        checkEyes("problem_cornerterreye3", blackEyes, whiteEyes);
    }

    /**
     *  ***
     *  ***
     *  ***
     */
    public void testCornerTerritoryEye4() {
        EyeCounts blackEyes = new EyeCounts(0, 1, 0, 0);
        EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 1);
        checkEyes("problem_cornerterreye4", blackEyes, whiteEyes);
    }

    /**
       *  ****
       *  ****
       *  ****
       *  ****
       */
      public void testCornerTerritoryEye5() {
          EyeCounts blackEyes = new EyeCounts(0, 1, 0, 0);
          EyeCounts whiteEyes = new EyeCounts(0, 0, 0, 1);
          checkEyes("problem_cornerterreye5", blackEyes, whiteEyes);
      }



    //----------------------------------------------------------------------------------------------
    /**
     *
     * @param eyesProblemFile
     * @param expectedBlackEyes number of black eyes in the group.
     * @param expectedWhiteEyes number of white eyes in the group.
     */
    private void checkEyes(String eyesProblemFile,
                           EyeCounts expectedBlackEyes, EyeCounts expectedWhiteEyes) {

        System.out.println("finding eyes for "+eyesProblemFile+" ...");
        //GameContext.log(0, "finding eyes for "+eyesProblemFile+" ...");
        restore(PATH_PREFIX + eyesProblemFile);

        GoBoard board = (GoBoard)controller_.getBoard();
        //Set groups = ((GoBoard) controller_.getBoard()).getGroups();

        // consider the 2 biggest groups
        //Assert.assertTrue("There were not two groups. Instead there were :"+groups.size(), groups.size() == 2);

        GoGroup biggestBlackGroup = getBiggestGroup(true);
        GoGroup biggestWhiteGroup = getBiggestGroup(false);

        EyeCounts eyeCounts = getEyeCounts(biggestBlackGroup.getEyes(board));
        Assert.assertTrue("Actual Black Eye counts were \n"+eyeCounts+" but was expecting \n"+ expectedBlackEyes,
                              eyeCounts.equals(expectedBlackEyes));
        eyeCounts = getEyeCounts(biggestWhiteGroup.getEyes(board));
        Assert.assertTrue("Actual White Eye counts were \n"+eyeCounts+" but was expecting \n"+ expectedWhiteEyes,
                              eyeCounts.equals(expectedWhiteEyes));
    }


    public static Test suite() {
        return new TestSuite(TestEyes.class);
    }

    private EyeCounts getEyeCounts(Set eyes)  {
        EyeCounts counts = new EyeCounts();

        for (Object e : eyes) {

            GoEye eye = (GoEye) e;
            switch (eye.getEyeType()) {
                case FALSE_EYE:
                    counts.numFalseEyes++;
                    break;
                case TRUE_EYE:
                    counts.numTrueEyes++;
                    break;
                case BIG_EYE:
                    counts.numBigEyes++;
                    break;
                case TERRITORIAL_EYE:
                    counts.numTerritorialEyes ++;
                    break;
                default:
                    assert false: "bad eye type:" + eye.getEyeType() ;
            }
        }
        return counts;
    }


    private static class EyeCounts {
        protected int numFalseEyes;
        protected int numTrueEyes;
        protected int numBigEyes;
        protected int numTerritorialEyes;

        public EyeCounts() {}

        public EyeCounts(int numFalse, int numTrue, int numBig, int numTerritorial) {
            numFalseEyes = numFalse;
            numTrueEyes = numTrue;
            numBigEyes = numBig;
            numTerritorialEyes = numTerritorial;
        }

        @Override
        public boolean equals(Object ocounts) {
            EyeCounts counts = (EyeCounts)ocounts;
            return (counts.numFalseEyes == numFalseEyes
                    && counts.numTrueEyes == numTrueEyes
                    && counts.numBigEyes == numBigEyes
                    && counts.numTerritorialEyes == numTerritorialEyes);
        }

        @Override
        public int hashCode() {
            return 1000000*numTerritorialEyes + 10000 * numBigEyes + 100 * numTrueEyes + numFalseEyes;
        }

        
        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer('\n');
            String nl = "\r\n";
            buf.append(" False Eyes: "+numFalseEyes+nl);
            buf.append(" True Eyes: "+numTrueEyes+nl);
            buf.append(" Big Eyes  : "+numBigEyes+nl);
            buf.append(" Territorial: "+numTerritorialEyes+nl);
            return buf.toString();
        }
    }
}