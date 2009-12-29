package com.becker.game.twoplayer.chess.ui;

import com.becker.game.twoplayer.chess.ChessPiece;
import com.becker.game.twoplayer.common.ui.TwoPlayerPieceRenderer;
import com.becker.game.common.BoardPosition;
import com.becker.game.common.GameContext;
import com.becker.game.common.Board;
import com.becker.ui.GUIUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Singleton class that takes a chess piece and renders it for the ChessBoardViewer.
 * @see ChessBoardViewer
 * @author Barry Becker
 */
public class ChessPieceRenderer  extends TwoPlayerPieceRenderer
{
    private static TwoPlayerPieceRenderer renderer_ = null;

    private static final Color DEFAULT_PLAYER1_COLOR = new Color( 30, 170, 10); //40, 190, 90 );
    private static final Color DEFAULT_PLAYER2_COLOR = new Color( 170, 0, 180); // 210, 60, 140 );

    // instead of rendering we can just show image icons which look even better.
    // @@ should we instead maintain an array of images indexed by type and player?
    private static ImageIcon[] pawnImage_ = new ImageIcon[2];
    private static ImageIcon[] rookImage_ = new ImageIcon[2];
    private static ImageIcon[] bishopImage_ = new ImageIcon[2];
    private static ImageIcon[] knightImage_ = new ImageIcon[2];
    private static ImageIcon[] queenImage_ = new ImageIcon[2];
    private static ImageIcon[] kingImage_ = new ImageIcon[2];

    private static final String IMAGE_DIR = GameContext.GAME_ROOT+"twoplayer/chess/ui/images/";
    static {
        // gets the images from resources or the filesystem depending if we are running as an applet or application respectively.
        pawnImage_[0] = GUIUtil.getIcon(IMAGE_DIR+"pawn1.png");
        pawnImage_[1] = GUIUtil.getIcon(IMAGE_DIR+"pawn2.png");

        rookImage_[0] = GUIUtil.getIcon(IMAGE_DIR+"rook1.png");
        rookImage_[1] = GUIUtil.getIcon(IMAGE_DIR+"rook2.png");

        knightImage_[0] = GUIUtil.getIcon(IMAGE_DIR+"knight1.png");
        knightImage_[1] = GUIUtil.getIcon(IMAGE_DIR+"knight2.png");

        bishopImage_[0] = GUIUtil.getIcon(IMAGE_DIR+"bishop1.png");
        bishopImage_[1] = GUIUtil.getIcon(IMAGE_DIR+"bishop2.png");

        queenImage_[0] = GUIUtil.getIcon(IMAGE_DIR+"queen1.png");
        queenImage_[1] = GUIUtil.getIcon(IMAGE_DIR+"queen2.png");

        kingImage_[0] = GUIUtil.getIcon(IMAGE_DIR+"king1.png");
        kingImage_[1] = GUIUtil.getIcon(IMAGE_DIR+"king2.png");
    }

    /**
     * protected constructor because this class is a singleton.
     * Use getRenderer instead
     */
    protected ChessPieceRenderer()
    {}

    public static TwoPlayerPieceRenderer getRenderer()
    {
        if (renderer_ == null)
            renderer_ = new ChessPieceRenderer();
        return renderer_;
    }

    /**
     *  determines what color the player1 pieces should be
     *  ignored if using icons to represent the pieces.
     */
    @Override
    public Color getPlayer1Color()
    {
        return DEFAULT_PLAYER1_COLOR;
    }

    /**
     *  determines what color the player2 pieces should be
     *  ignored if using icons to represent the pieces.
     */
    @Override
    public Color getPlayer2Color()
    {
        return DEFAULT_PLAYER2_COLOR;
    }

    /**
     * this draws the actual chess piece
     */
    @Override
    public void render( Graphics2D g2, BoardPosition position, int cellSize, int margin, Board b)
    {
        ChessPiece piece = (ChessPiece)position.getPiece();
        if (piece==null)
            return; // nothing to render
        int p = (piece.isOwnedByPlayer1()? 0:1);
        int pieceSize = getPieceSize(cellSize, piece);
        Point pos = getPosition(position, cellSize, pieceSize, margin);
        switch (piece.getType()) {
            case ChessPiece.PAWN :
                g2.drawImage(pawnImage_[p].getImage(), pos.x, pos.y, pieceSize, pieceSize, null); break;
            case ChessPiece.ROOK :
                g2.drawImage(rookImage_[p].getImage(), pos.x, pos.y, pieceSize, pieceSize, null); break;
            case ChessPiece.KNIGHT :
                g2.drawImage(knightImage_[p].getImage(), pos.x, pos.y, pieceSize, pieceSize, null); break;
            case ChessPiece.BISHOP :
                g2.drawImage(bishopImage_[p].getImage(), pos.x, pos.y, pieceSize, pieceSize, null); break;
            case ChessPiece.QUEEN :
                g2.drawImage(queenImage_[p].getImage(), pos.x, pos.y, pieceSize, pieceSize, null); break;
            case ChessPiece.KING :
                g2.drawImage(kingImage_[p].getImage(), pos.x, pos.y, pieceSize, pieceSize, null); break;
            default:
                assert false:("bad chess piece type: "+piece.getType());
        }
        //if (this.isFirstTimeMoved())
        //    g.drawRect(xpos, ypos, pieceSize/2, pieceSize/2);
    }
}
