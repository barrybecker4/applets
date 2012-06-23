// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.puzzle.tantrix.solver.path.permuting;

import com.becker.common.math.MathUtil;
import com.becker.puzzle.tantrix.model.*;
import com.becker.puzzle.tantrix.solver.path.PathType;
import com.becker.puzzle.tantrix.solver.path.TantrixPath;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collections;

import static com.becker.puzzle.tantrix.solver.path.permuting.PathTstUtil.*;

/**
 * @author Barry Becker
 */
public class TilesOfTypeIndicesTest extends TestCase {

    /** instance under test */
    private TilesOfTypeIndices indices;

    @Override
    public void setUp() {
        MathUtil.RANDOM.setSeed(0);
    }

    public void testTIGHTIndicesIn3TileLoop() {

        indices = new TilesOfTypeIndices(PathType.TIGHT_CURVE, LOOP_PATH);

        assertEquals("Unexpected ",
                Arrays.asList(0, 1, 2), indices);
    }


    public void testWIDEIndicesIn3TileLoop() {

        indices = new TilesOfTypeIndices(PathType.WIDE_CURVE, LOOP_PATH);

        assertEquals("Unexpected ",
                Collections.emptyList(), indices);
    }

    public void testTIGHTIndicesIn4TileNonLoopPath() {

        indices = new TilesOfTypeIndices(PathType.TIGHT_CURVE, NON_LOOP_PATH4);

        assertEquals("Unexpected ",
                Arrays.asList(1, 3), indices);
    }

    public void testWIDEIndicesIn4TileNonLoopPath() {

        indices = new TilesOfTypeIndices(PathType.WIDE_CURVE, NON_LOOP_PATH4);

        assertEquals("Unexpected ",
                Arrays.asList(0, 2), indices);
    }


    private static TantrixPath createPath(TilePlacement placement1, TilePlacement placement2, TilePlacement placement3) {
        return  new TantrixPath(new TilePlacementList(placement1, placement2, placement3), PathColor.YELLOW);
    }
}
