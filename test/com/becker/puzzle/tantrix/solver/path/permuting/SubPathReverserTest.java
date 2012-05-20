// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.puzzle.tantrix.solver.path.permuting;

import com.becker.puzzle.tantrix.model.PathColor;
import com.becker.puzzle.tantrix.model.Rotation;
import com.becker.puzzle.tantrix.model.TilePlacement;
import com.becker.puzzle.tantrix.model.TilePlacementList;
import com.becker.puzzle.tantrix.solver.path.TantrixPath;

import static com.becker.puzzle.tantrix.TantrixTstUtil.TILES;
import static com.becker.puzzle.tantrix.TantrixTstUtil.loc;

/**
 * @author Barry Becker
 */
public class SubPathReverserTest extends SubPathMutatorBase {


    @Override
    protected SubPathMutator createMutator(TilePlacement placement, PathColor primaryColor) {
        return new SubPathReverser(placement, primaryColor);
    }


    @Override
    protected void verifyMutated1TilePath(TantrixPath resultPath) {
        assertEquals("Unexpected result for " + mutator,
                1, resultPath.size());
        TilePlacement first = new TilePlacement(TILES.getTile(2), loc(2, 1), Rotation.ANGLE_60);
        TilePlacementList expList = new TilePlacementList(first);
        assertEquals("Unexpected reversal.", expList, resultPath.getTilePlacements());
    }

    @Override
    protected void verifyMutated2TilePath(TantrixPath resultPath) {
        assertEquals("unexpected size.", 2, resultPath.size());

        TilePlacement first = new TilePlacement(TILES.getTile(3), loc(2, 1), Rotation.ANGLE_180);
        TilePlacement second = new TilePlacement(TILES.getTile(2), loc(1, 2), Rotation.ANGLE_180);

        TilePlacementList expList = new TilePlacementList(first, second);

        assertEquals("Unexpected reversal.", expList, resultPath.getTilePlacements());
    }
}