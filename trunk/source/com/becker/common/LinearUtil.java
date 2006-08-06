package com.becker.common;

import javax.vecmath.*;

/**
 * This class implements a number of static utility functions that are useful for math.
 * Mostly linear algebra matrix solvers and the like.
 *
 * @author Barry Becker
 */
public final class LinearUtil
{
    private LinearUtil() {};

    /**
     * matrix conjugate-Gradient solver for Ax=b
     * @param matrix the matrix of linear coefficients
     * @param b the right ahnd side
     * @param initialGuess the initial guess for the solution x, x0
     * @param eps the tolerable error (eg .0000001)
     */
    public static GVector conjugateGradientSolve( GMatrix matrix, GVector b, GVector initialGuess, double eps )
    {
        GVector x = new GVector( initialGuess );
        GVector tempv = new GVector( initialGuess );
        tempv.mul( matrix, initialGuess );
        GVector bb = new GVector( b );
        bb.sub( tempv );
        GVector r = new GVector( bb );
        GVector p = new GVector( r );
        GVector xnew = new GVector( p );
        GVector rnew = new GVector( p );
        GVector pnew = new GVector( p );
        GVector matrixMultp = new GVector( p );
        GMatrix matrixInverse = new GMatrix( matrix );
        matrixInverse.invert();
        double error, norm;
        int iteration = 0;
        //return initialGuess;
        do {
            matrixMultp.mul( matrix, p );
            double lambda = (r.dot( p ) / p.dot( matrixMultp ));
            xnew.scaleAdd( lambda, p, x );
            rnew.scaleAdd( -lambda, matrixMultp, r );
            double alpha = -(rnew.dot( matrixMultp ) / p.dot( matrixMultp ));
            pnew.scaleAdd( alpha, p, rnew );
            p.set( pnew );
            r.set( rnew );
            //System.out.println("the residual = "+r.toString());
            x.set( xnew );
            //error = Math.abs(r.dot(r)); // wrong way to compute norm
            rnew.mul( r, matrixInverse );
            norm = rnew.dot( r );
            error = norm * norm;
            //System.out.println("xi = "+x.toString());
            iteration++;
            System.out.println( "the error for iteration " + iteration + " is : " + error );
        } while ( error > eps && iteration < 8 );

        if ( error > eps || Double.isNaN( error ) || Double.isInfinite( error ) )  // something went wrong
            return initialGuess;

        return xnew;
    }

    public static void printMatrix( GMatrix matrix )
    {
        for ( int i = 0; i < matrix.getNumRow(); i++ ) {
            for ( int j = 0; j < matrix.getNumCol(); j++ ) {
                double a = matrix.getElement( i, j );
                if ( a == 0 )
                    System.out.print( "  0  " );
                else
                    System.out.print( Util.formatNumber( a ) + ' ' );
            }
            System.out.println( "" );
        }
    }

    /**
     * @return the distance between two points
     */
    public static double distance(Vector2d p1, Vector2d p2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

