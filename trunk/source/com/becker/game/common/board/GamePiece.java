package com.becker.game.common.board;

import java.io.Serializable;


/**
 *  the BoardPosition describes the physical marker at a location on the board.
 *  It may be empty if there is no piece there.
 *
 * @see Board
 * @author Barry Becker
 */
public class GamePiece implements Serializable {

    private static final long serialVersionUID = 1;

    /** Subclasses should add more types if needed. */
    public static final char REGULAR_PIECE = 'x';

    /** true if this is player1's piece. */
    protected boolean ownedByPlayer1_;

    /** the type of piece to draw */
    protected char type_;

    /**
     * For some pieces we may wish to represent them
     * more transparently (255 = total transparent; 0= totally opaque)
     */
    private short transparency_;

    /**
     * a string associated with the piece to give additional information.
     * For example you can use this to show a number (0-99)
     */
    private String annotation_;

    /**
     * default constructor
     */
    protected GamePiece() {
        ownedByPlayer1_ = false;
        type_ = REGULAR_PIECE;
        transparency_ = 0;
    }

    /**
     * constructor   (assumes a regular piece)
     * @param player1 if owned by player1
     */
    public GamePiece( boolean player1)  {
        ownedByPlayer1_ = player1;
        type_ = REGULAR_PIECE;
        transparency_ = 0;
    }

    /**
     * constructor
     * @param player1 if owned by player1
     * @param type there may be different types of pieces (for example in chess there are many; checkers has 2)
     */
    protected GamePiece( boolean player1, char type) {
        ownedByPlayer1_ = player1;
        type_ = type;
        transparency_ = 0;
    }

    /**
     * Copy constructor
     */
    protected GamePiece(GamePiece piece) {
        this(piece.isOwnedByPlayer1());
        this.type_ = piece.getType();
        transparency_ = piece.transparency_;
        annotation_ = piece.annotation_;
    }
    /**
     * @return create a deep copy
     */
    public GamePiece copy() {
        return new GamePiece(this);
    }

    public final void setType( char type ) {
        type_ = type;
    }

    public final char getType() {
        return type_;
    }

    public final boolean isOwnedByPlayer1() {
        return ownedByPlayer1_;
    }

    public final void setTransparency( short trans ) {
        transparency_ = trans;
    }

    public final short getTransparency() {
        return transparency_;
    }

     /**
      * @param annotation   number or word to show next to the game piece.
      */
     public final void setAnnotation( String annotation )  {
         annotation_ = annotation;
     }

     /**
      * @return  number or word to show next to the game piece.
      */
     public String getAnnotation() {
         return annotation_;
     }

    /**
     * @return Possibly give more detail that you would get with just toString().
     */
    public String getDescription() {
        return toString();
    }

    /**
     * @return a string representation of the board position
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if ( ownedByPlayer1_ )
            sb.append( " X" );
        else
            sb.append( " O" );
        return sb.toString();
    }
}

