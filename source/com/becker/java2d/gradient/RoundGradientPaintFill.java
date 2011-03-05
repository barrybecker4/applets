package com.becker.java2d.gradient;

import com.becker.ui.application.ApplicationFrame;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;

public class RoundGradientPaintFill
        extends ApplicationFrame
{
    public static void main( String[] args )
    {
        RoundGradientPaintFill f = new RoundGradientPaintFill();
        f.setTitle( "RoundGradientPaintFill v1.0" );
        f.setSize( 200, 200 );
        f.center();
        f.setVisible( true );
    }

    public void paint( Graphics g )
    {
        Graphics2D g2 = (Graphics2D) g;
        RoundRectangle2D r = new RoundRectangle2D.Float( 25, 35, 150, 150, 25,
                25 );
        RoundGradientPaint rgp = new RoundGradientPaint( 75, 75, Color.magenta,
                new Point2D.Double( 0, 85 ), Color.blue );
        g2.setPaint( rgp );
        g2.fill( r );
    }
}