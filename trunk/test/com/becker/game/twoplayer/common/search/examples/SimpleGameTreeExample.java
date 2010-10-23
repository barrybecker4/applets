package com.becker.game.twoplayer.common.search.examples;

import com.becker.game.common.GamePiece;
import com.becker.game.common.Move;
import com.becker.game.common.MoveList;
import com.becker.game.twoplayer.common.TwoPlayerMove;
import com.becker.game.twoplayer.common.search.TwoPlayerMoveStub;

import java.util.Arrays;


/**
 * A simple game tree for testing search strategies.
 * It looks something like this
 *                 ____   []  _____
 *                /                \
 *            [-8]                  [-2]
 *         /      \               /      \
 *     [-1]       [7]           [8]       [2]
 *    /   \      /   \         /  \       /  \
 *  [-5] [-4]   [-6] [-2]    [-7] [-8]  [-4] [-4]
 *
 * @author Barry Becker
 */
public class SimpleGameTreeExample extends AbstractGameTreeExample  {


    public SimpleGameTreeExample() {

        initialMove = new TwoPlayerMoveStub(6, true, null);

        // first ply
        TwoPlayerMoveStub move0 = new TwoPlayerMoveStub(-8, false, initialMove);
        TwoPlayerMoveStub move1 = new TwoPlayerMoveStub(-2, false, initialMove);

        // second ply
        TwoPlayerMoveStub move00 = new TwoPlayerMoveStub(-1, true, move0);
        TwoPlayerMoveStub move01 = new TwoPlayerMoveStub(7, true, move0);

        TwoPlayerMoveStub move10 = new TwoPlayerMoveStub(8, true, move1);
        TwoPlayerMoveStub move11 = new TwoPlayerMoveStub(2, true, move1);

        // third ply   (the leaves. These values get inherited)
        TwoPlayerMoveStub move000 = new TwoPlayerMoveStub(-5, false, move00);
        TwoPlayerMoveStub move001 = new TwoPlayerMoveStub(-4, false, move00);

        TwoPlayerMoveStub move010 = new TwoPlayerMoveStub(-6, false, move01);
        TwoPlayerMoveStub move011 = new TwoPlayerMoveStub(-2, false, move01);

        TwoPlayerMoveStub move100 = new TwoPlayerMoveStub(-7, false, move10);
        TwoPlayerMoveStub move101 = new TwoPlayerMoveStub(-8, false, move10);

        TwoPlayerMoveStub move110 = new TwoPlayerMoveStub(-4, false, move11);
        TwoPlayerMoveStub move111 = new TwoPlayerMoveStub(-4, false, move11);


        initialMove.setChildren(createList(move0, move1));

        move0.setChildren(createList(move00, move01));
        move1.setChildren(createList(move10, move11));
        
        move00.setChildren(createList(move000, move001));
        move01.setChildren(createList(move010, move011));

        move10.setChildren(createList(move100, move101));
        move11.setChildren(createList(move110, move111));
    }
}
