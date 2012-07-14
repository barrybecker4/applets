// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.puzzle.tantrix.model;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.common.math.MathUtil;
import com.barrybecker4.puzzle.tantrix.solver.path.TantrixPath;
import junit.framework.TestCase;

import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.TILES;
import static com.barrybecker4.puzzle.tantrix.TantrixTstUtil.place3UnsolvedTiles;

/**
 * @author Barry Becker
 */
public class RandomPathGeneratorTest extends TestCase {

    /** instance under test */
    private RandomPathGenerator pathGenerator;

    @Override
    public void setUp() {
        MathUtil.RANDOM.setSeed(0);
    }

    public void test3TilesPathGen() {
        pathGenerator = new RandomPathGenerator(place3UnsolvedTiles());
        TantrixPath rPath = pathGenerator.generateRandomPath();

        System.out.println("rpath=" + rPath);
        assertEquals("Unexpected length for randomly generated path.", 3, rPath.size());

        TilePlacementList tiles =
                new TilePlacementList(
                        new TilePlacement(TILES.getTile(2), new Location(22, 20), Rotation.ANGLE_0),
                        new TilePlacement(TILES.getTile(1), new Location(21, 21), Rotation.ANGLE_0),
                        new TilePlacement(TILES.getTile(3), new Location(22, 21), Rotation.ANGLE_180));
        TantrixPath expectedPath = new TantrixPath(tiles, PathColor.YELLOW);

        assertEquals("Unexpected path.", expectedPath, rPath);
    }

}