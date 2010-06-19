package com.becker.game.twoplayer.go.board.analysis.eye.metadata;

import com.becker.game.twoplayer.go.board.GoEye;

/**
 * Enum for the different possible Eye shapes.
 * See http://www.ai.univ-paris8.fr/~cazenave/eyeLabelling.pdf
 * @see GoEye
 *
 * We define the Neighbour Classification of  an eye as a number
 * of  digits sorted from low to high, where every intersection in the eye space is associated
 * to a digit that indicates the number of neighbors (adjacent intersections)
 * to that intersection that belong to the eye space.
 *
 * For example, here are all the possible pentomino classifications (independent of symmetry).
 *
 * E11222: XXXXX      XXXX      XXX      XX     XX    X       X
 *                       X        XX      X      X    X      XX
 * E11123:  XX        X       X          XX      XX   XXX   XX
 *         XX        XX       X
 *          X         X      XXX
 * E11114:       X    X
 *              XXX
 *               X
 * E12223:  XX
 *          XXX
 *
 *
 * @author Barry Becker
 */
public enum EyeType
{
    /** False eye always have the potential to become no eyes */
    FalseEye(0) {
        @Override
        public EyeInformation getMetaData(String name) { return new FalseEyeInformation(); }
    },

    E1(1)  {
        @Override
        public EyeInformation getMetaData(String name) { return new E1Subtype(); }
    },
    E2(2) {
        @Override
        public EyeInformation getMetaData(String name) { return new E2Subtype(); }
    },
    E3(3) {
        @Override
        public EyeInformation getMetaData(String name) { return new E3Subtype(); }
    },
    E4(4) {
        @Override
        public EyeInformation getMetaData(String name) { return new E4Subtype(name); }
    },
    E5(5) {
        @Override
        public EyeInformation getMetaData(String name) { return new E5Subtype(name); }
    },
    E6(6) {
        @Override
        public EyeInformation getMetaData(String name) { return new E6Subtype(name); }
    },
    E7(7) {
        @Override
        public EyeInformation getMetaData(String name) { return new E7Subtype(name); }
    },

    /** Usually 2 or more eyes, but may be none or one in some rare cases. */
    TerritorialEye(8) {
        @Override
        public EyeInformation getMetaData(String name) { return new TerritorialEyeInformation(); }
    };

    private byte size;


    /**
     * constructor
     */
    EyeType(int eyeSize) {
        this.size = (byte)eyeSize;
    }

    /**
     * @return the number of spaces in they eye (maybe be filled with some enemy stones).
     */
    public byte getSize()    {
        return size;
    }

    /**
     *
     * @return true if the shape has the life property
     */
    public abstract EyeInformation getMetaData(String name);
}