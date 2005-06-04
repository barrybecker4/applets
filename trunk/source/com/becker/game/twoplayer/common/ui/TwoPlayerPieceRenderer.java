package com.becker.game.twoplayer.common.ui;

import com.becker.game.common.*;
import com.becker.game.common.ui.GamePieceRenderer;
import com.becker.game.common.ui.GameBoardViewer;
import com.becker.game.twoplayer.common.TwoPlayerMove;
import com.becker.java2d.RoundGradientPaint;
import com.becker.common.Util;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

/**
 * a singleton class that takes a game piece and renders it for the TwoPlayerBoardViewer.
 * We use a separate piece rendering class to avoid having ui in the piece class itself.
 * This allows us to more cleanly separate the client pieces from the server.
 *
 * @see TwoPlayerBoardViewer
 * @author Barry Becker
 */
public class TwoPlayerPieceRenderer extends GamePieceRenderer
{

    // there must be one of these for each derived class too.
    private static GamePieceRenderer renderer_ = null;


    private static final Color DEFAULT_PLAYER1_COLOR = new Color( 230, 100, 255);
    private static final Color DEFAULT_PLAYER2_COLOR = new Color( 100, 220, 255);

    private static final Color PLAYER1_TEXT_COLOR = new Color( 255, 250, 255 );
    private static final Color PLAYER2_TEXT_COLOR = new Color( 0, 50, 30 );

    /**
     * private constructor because this class is a singleton.
     * Use getPieceRenderer instead
     */
    protected TwoPlayerPieceRenderer()
    {}


    public static GamePieceRenderer getRenderer()
    {
        if (renderer_ == null)
            renderer_ = new TwoPlayerPieceRenderer();
        return renderer_;
    }


    /**
     *  determines what color the player1 pieces should be
     *  ignored if using icons to represent the pieces.
     */
    public Color getPlayer1Color()
    {
        return DEFAULT_PLAYER1_COLOR;
    }

    /**
     *  determines what color the player2 pieces should be
     *  ignored if using icons to represent the pieces.
     */
    public Color getPlayer2Color()
    {
        return DEFAULT_PLAYER2_COLOR;
    }


    /**
     * @return the game piece render color.
     */
    protected Color getPieceColor(GamePiece piece)
    {
        return getPieceColor(piece.isOwnedByPlayer1(), piece.getTransparency());       
    }
    
       /**
     * @return the game piece render color.
     */
    private Color getPieceColor(boolean player1, int transparency)
    {
        Color playerColor = null;
        Color c = null;
        if ( player1 ) {
            playerColor = getPlayer1Color();
            c = new Color( playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(),
                    255 - transparency );
        }
        else {
            playerColor = getPlayer2Color();
            c = new Color( playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(),
                    255 - transparency  );
        }
        return c;
    }

    /**
     * @return color for annotation text (if any).
     */
    protected Color getTextColor(GamePiece piece)
    {
        Color textColor = PLAYER2_TEXT_COLOR;
        if ( piece.isOwnedByPlayer1() ) {
            textColor = PLAYER1_TEXT_COLOR;
        }
        return textColor;
    }



    private static final Color URGENT_COLOR = new Color(245, 10, 0);

    /**
     * show the next moves in a special way.
     */ 
    public void renderNextMove( Graphics2D g2, TwoPlayerMove move, int cellSize, Board b) {

        if (move.piece != null)  {
            g2.setColor(getPieceColor(move.piece));
        
            BoardPosition position = b.getPosition(move.getToRow(), move.getToCol());
            int pieceSize = (int)(.5* getPieceSize(cellSize, move.piece));
            Point pos = getPosition(position, cellSize, pieceSize);
        
            g2.fillOval( pos.x, pos.y, pieceSize, pieceSize );
            g2.setColor(move.urgent ? URGENT_COLOR : this.getTextColor(move.piece));
            g2.drawString(""+Math.round(move.value), pos.x - 5 , pos.y + 2);
        } else {
            System.out.println("piece for next move is null: "+move);
        }
    }
}

