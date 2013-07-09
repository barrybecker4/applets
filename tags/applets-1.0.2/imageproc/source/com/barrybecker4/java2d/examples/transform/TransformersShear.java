package com.barrybecker4.java2d.examples.transform;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class TransformersShear
        extends Transformers {

    public static void main( String[] args ) {
        Transformers t = new TransformersShear();
        Frame f = t.getFrame();
        f.setVisible( true );
    }

    @Override
    public AffineTransform getTransform()  {
        AffineTransform at = AffineTransform.getTranslateInstance( 150, 0 );
        at.shear( -.5, 0 );
        return at;
    }
}