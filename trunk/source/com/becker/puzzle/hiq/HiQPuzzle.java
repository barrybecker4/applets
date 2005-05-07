package com.becker.puzzle.hiq;

import com.becker.ui.GUIUtil;

import javax.swing.*;
import java.util.*;

/**
 *  Red Puzzle
 *  This program solves a 9 piece puzzle that has nubs on all 4 sides.
 *  Its virtually impossible to solve by hand because of all the possible permutations.
 *  This program can usually solve it by trying between 10,000 and 50,000 combinations.
 */
public final class HiQPuzzle extends JApplet
{

    private static JFrame baseFrame_ = null;

    // global counter;
    private static int numIterations_ = 0;

    private Solution solution_;

    // create the pieces and add them to a list
    private ArrayList pegs_;

    //Construct the application
    public HiQPuzzle()
    {
        try {
            GUIUtil.setCustomLookAndFeel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create and initialize the puzzle
     * (init required for applet)
     */
    public final void init()
    {
        numIterations_ = 0;
        solution_ = new Solution();
        pegs_ = new ArrayList();

        this.getContentPane().add( solution_ );

        // these are the Pegs.
        pegs_.add( new Peg( 'S', 'H', 'S', 'D', false, false, true, true, 1 ) );
        pegs_.add( new Peg( 'H', 'S', 'S', 'C', true, true, false, false, 2 ) );
        pegs_.add( new Peg( 'H', 'D', 'D', 'H', true, true, false, false, 3 ) );
        pegs_.add( new Peg( 'H', 'D', 'C', 'C', true, true, false, false, 4 ) );
        pegs_.add( new Peg( 'C', 'H', 'S', 'H', true, true, false, false, 5 ) );
        pegs_.add( new Peg( 'C', 'H', 'D', 'C', true, true, false, false, 6 ) );
        pegs_.add( new Peg( 'S', 'D', 'H', 'D', true, true, false, false, 7 ) );
        pegs_.add( new Peg( 'D', 'C', 'C', 'D', true, true, false, false, 8 ) );
        pegs_.add( new Peg( 'S', 'S', 'H', 'C', true, true, false, false, 9 ) );

        // shuffle the pieces so we get difference solutions -
        // or at least different approaches to the solution if there is only one.
        Collections.shuffle( pegs_ );

        solution_.setDoubleBuffered( true );
        this.setVisible( true );
        solution_.repaint();
    }

    /**
     * start solving the puzzle.
     * called by the browser after init(), if running as an applet
     */
    public final void start()
    {
        // this does all the heavy work of solving it
        boolean solved = solvePuzzle( solution_, pegs_ );

        if ( solved )
            System.out.println( "The final solution is shown. the number of iterations was:" + numIterations_ );
        else
            System.out.println( "This puzzle is not solvable!" ); // guaranteed not to happen
        solution_.repaint();
    }

    /**
     * stop and cleanup.
     */
    public final void stop()
    {
    }

    public static int getNumIterations()
    {
        return numIterations_;
    }

    private static boolean solvePuzzle( Solution solution, List pieces )
    {
        boolean solved = false;
        int numPiecesRemaining = pieces.size();

        if ( numPiecesRemaining == 0 )
            return true;

        int k = 0;
        while ( !solved && (k < numPiecesRemaining) ) {
            Peg p = (Peg) pieces.get( k );
            if ( solution.fits( p ) ) {
                solution.push( p );
                pieces.remove( p );
                solved = solvePuzzle( solution, pieces );
                if ( !solved ) {
                    // then we still need to try the remaining rotations for
                    // that first piece
                    p = solution.pop();
                    if ( solution.fits( p ) ) { // checks remaining rotations
                        solution.push( p );
                        solved = solvePuzzle( solution, pieces );
                    }
                    if ( !solved ) {
                        // if still not solved we need to back track
                        p = solution.pop();
                        p.reset(); // restore to unrotated state
                        pieces.add( k, p );  // put it back where it came from
                        solution.paintAll( solution.getGraphics() );
                    }
                }
            }

            k++;
            numIterations_++;
        }
        solution.invalidate();
        solution.revalidate();
        // if we get here and solved is not true, we did not find a solution
        return solved;
    }


    //------ Main method --------------------------------------------------------
    /**
     *use this to run as an application instead of an applet
     */
    public static void main( String[] args )
    {

        HiQPuzzle applet = new HiQPuzzle();

        // this will call applet.init() and start() methods instead of the browser
        baseFrame_ = GUIUtil.showApplet( applet, "Red Puzzle Solver" );

    }
}
