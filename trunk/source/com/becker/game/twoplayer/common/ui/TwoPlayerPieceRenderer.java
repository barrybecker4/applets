package com.becker.game.twoplayer.common.ui;

import com.becker.game.common.*;
import com.becker.game.common.ui.*;
import com.becker.game.twoplayer.common.*;

import java.awt.*;
import java.text.*;

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

    /** there must be one of these for each derived class too. */
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
        Color playerColor = piece.isOwnedByPlayer1() ? getPlayer1Color() : getPlayer2Color();
         return  new Color( playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue(),
                           255 - piece.getTransparency() );
    }


    /**
     * @return color for annotation text (if any).
     */
    @Override
    protected Color getTextColor(GamePiece piece)
    {
        Color textColor = PLAYER2_TEXT_COLOR;
        if ( piece.isOwnedByPlayer1() ) {
            textColor = PLAYER1_TEXT_COLOR;
        }
        return textColor;
    }


    private static final Color URGENT_COLOR = new Color(245, 10, 0);
    private static final DecimalFormat format_ = new DecimalFormat("###,###.#");
    private static final double NEXT_MOVE_SIZE_FRAC = 0.2;

    /**
     * show the next moves in a special way.
     */
    public void renderNextMove( Graphics2D g2, TwoPlayerMove move, int cellSize, int margin, Board b) {

        if (move.getPiece() != null)  {
            g2.setColor(getPieceColor(move.getPiece()));

            BoardPosition position = b.getPosition(move.getToRow(), move.getToCol());
            int pieceSize = (int)(NEXT_MOVE_SIZE_FRAC * getPieceSize(cellSize, move.getPiece()));
            Point pos = getPosition(position, cellSize, pieceSize, margin);
            g2.setFont(BASE_FONT);
            g2.fillOval( pos.x, pos.y, pieceSize, pieceSize );
            g2.setColor(move.isUrgent() ? URGENT_COLOR : Color.DARK_GRAY);
            g2.drawString(""+format_.format(move.getValue()), pos.x - 3 , pos.y + 2);
        } else {
            GameContext.log(2, "piece for next move is null: "+move);
        }
    }
}

