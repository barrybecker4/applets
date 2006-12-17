package com.becker.puzzle.maze;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 *  Program to automatically generate a Maze.
 *  Motivation: Get my son, Brian, to excel at Kumon by trying these mazes with a pencil.
 *  this is the global space containing all the cells, walls, and particles.
 *  Assumes an M*N grid of cells.
 *  X axis increases to the left.
 *  Y axis increases downwards to be consistent with java graphics.
 *
 *  @author Barry Becker
 */
public class MazeGenerator
{

    private MazeModel maze_;
    private MazePanel panel_;

    // put the stop point at the maximum search depth.
    private static int maxDepth_ = 0;


    public MazeGenerator(MazePanel panel)
    {
        this(panel.getMaze(), panel);
    }

    private MazeGenerator(MazeModel maze, MazePanel panel)
    {
        maze_ = maze;
        panel_ = panel;
    }

    /**
     * generate the maze
     */
    public void generate()
    {
        generate(Direction.FORWARD.getProbability(),
                 Direction.LEFT.getProbability(),
                 Direction.RIGHT.getProbability() );
    }

    /**
     * generate the maze.
     */
    public void generate(double forwardProb, double leftProb, double rightProb )
    {
        // the second argument is a dummy direction
        maxDepth_ = 0;
        Direction.FORWARD.setProbability(forwardProb);
        Direction.LEFT.setProbability(leftProb);
        Direction.RIGHT.setProbability(rightProb);

        search();
        panel_.repaint();
    }

    /**
     * do a depth first search (without recursion) of the grid space to determine the graph.
     * I used to use a recursive algorithm but it was slower and would give stack overflow
     * exceptions even for moderately sized mazes.
     */
    public void search()
    {
        List stack = new LinkedList();

        Point currentPosition = maze_.getStartPosition();
        MazeCell currentCell = maze_.getCell(currentPosition);

        // push the initial moves
        MazeModel.pushMoves( currentPosition, new Point( 1, 0 ), 1, stack );
        Point dir;
        int depth;

        while ( !stack.isEmpty() ) {
            boolean moved = false;

            do {
                GenState state = (GenState) stack.remove(0);  // pop

                currentPosition = state.getPosition();
                dir = state.getDirection();
                depth = state.getDepth();

                if ( depth > maxDepth_ ) {
                    maxDepth_ = depth;
                    maze_.setStopPosition(currentPosition);
                }
                if ( depth > currentCell.depth )
                    currentCell.depth = depth;

                currentCell = maze_.getCell(currentPosition);
                Point nextPosition = currentCell.getNextPosition(currentPosition, dir);

                MazeCell nextCell = maze_.getCell(nextPosition);

                if ( !nextCell.visited ) {
                    moved = true;
                    nextCell.visited = true;
                    currentPosition = nextPosition;
                }
                else {
                    // add a wall
                    if ( dir.x == 1 ) // east
                        currentCell.eastWall = true;
                    else if ( dir.y == 1 ) // south
                        currentCell.southWall = true;
                    else if ( dir.x == -1 )  // west
                        nextCell.eastWall = true;
                    else if ( dir.y == -1 )  // north
                        nextCell.southWall = true;
                }
            } while ( !moved && !stack.isEmpty() );

            // this can be really slow if you do a refresh everytime
            if (Math.random() < 4.0/(Math.pow(panel_.getAnimationSpeed(), 2) + 1)) {
                panel_.paintAll();
            }

            // now at a new location
            if ( moved )
                MazeModel.pushMoves( currentPosition, dir, ++depth, stack );
        }
    }

}
