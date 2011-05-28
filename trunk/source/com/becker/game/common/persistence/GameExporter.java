package com.becker.game.common.persistence;

import com.becker.common.geometry.Location;
import com.becker.game.common.Move;
import com.becker.game.common.board.IBoard;
import com.becker.game.common.ui.SgfFileFilter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


/**
 * Export the state of a game to a file.
 *
 * @author Barry Becker
 */
public abstract class GameExporter {

    protected IBoard board_;


    protected GameExporter(IBoard board) {
        board_ = board;
    }

    /**
     * save the current state of the game to a file.
     * You must override if you want it to work.
     */
    public abstract void saveToFile( String fileName, AssertionError ae);

    /**
     * Convert a moew to SGF format
     * @param move the move to format
     * @return the sgf (smart game format) representation for the move.
     */
    protected abstract String getSgfForMove(Move move);

    protected Writer createWriter(String fileName) throws IOException {
       return new FileWriter( ensureSuffix(fileName) );
    }

    /**
     * @param name name to ensure safety of
     * @return name with the sgf suffix added if the name did not have it.
     */
    protected String ensureSuffix(String name) {

        String suffix = "." + SgfFileFilter.SGF_EXTENSION;
        if (!name.endsWith(suffix)) {
            return name + suffix;
        }
        return name;
    }
    
    /**
     * append the board position to the buffer in the form [<c><r>]
     * Where c and r are the column and row letters whose range depends on the game.
     */
    protected void serializePosition(Location pos, StringBuilder buf) {
        buf.append( '[' );
        buf.append( (char) ('a' + pos.getCol() - 1) );
        buf.append( (char) ('a' + pos.getRow() - 1) );
        buf.append( ']' );
    }
}
