package com.barrybecker4.java2d.examples.transform;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class TransformersTransrotate
        extends Transformers {

    public static void main( String[] args ) {
        Transformers t = new TransformersTransrotate();
        Frame f = t.getFrame();
        f.setVisible( true );
    }

    @Override
    public AffineTransform getTransform() {
        AffineTransform at = new AffineTransform();
        at.setToTranslation( 100, 0 );
        at.rotate( Math.PI / 6 );
        return at;
    }
}