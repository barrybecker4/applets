/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.twoplayer.common.persistence;

import ca.dj.jigo.sgf.SGFException;
import ca.dj.jigo.sgf.SGFGame;
import ca.dj.jigo.sgf.SGFLoader;
import ca.dj.jigo.sgf.tokens.InfoToken;
import ca.dj.jigo.sgf.tokens.PlacementToken;
import ca.dj.jigo.sgf.tokens.SGFToken;
import ca.dj.jigo.sgf.tokens.TextToken;
import com.barrybecker4.game.common.GameContext;
import com.barrybecker4.game.common.Move;
import com.barrybecker4.game.common.MoveList;
import com.barrybecker4.game.common.board.GamePiece;
import com.barrybecker4.game.common.persistence.GameImporter;
import com.barrybecker4.game.twoplayer.common.TwoPlayerController;
import com.barrybecker4.game.twoplayer.common.TwoPlayerMove;
import com.barrybecker4.game.twoplayer.common.persistence.tokens.Player1MoveToken;
import com.barrybecker4.game.twoplayer.common.persistence.tokens.Player1NameToken;
import com.barrybecker4.game.twoplayer.common.persistence.tokens.Player2NameToken;
import com.barrybecker4.game.twoplayer.common.persistence.tokens.Size2Token;
import com.barrybecker4.game.twoplayer.common.persistence.tokens.TwoPlayerMoveToken;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Imports the state of a two player game from a file.
 *
 * @author Barry Becker
 */
public class TwoPlayerGameImporter extends GameImporter {

    public TwoPlayerGameImporter(TwoPlayerController controller) {
        super(controller);
    }

    /**
     * Take what is in the specified file and show it in the viewer.
     * @param fileName file to load
     */
    @Override
    public void restoreFromFile( String fileName ) {

        try {
            FileInputStream iStream = new FileInputStream( fileName );
            GameContext.log( 2, "opening " + fileName );

            SGFLoader gameLoader = createLoader();
            SGFGame game = gameLoader.load( iStream );
            restoreGame( game );

        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog( null,
                                           "file " + fileName + " was not found." + fnfe.getMessage() );
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog( null,
                                           "IOException occurrred while reading " +
                                           fileName + " :" + ioe.getMessage() );
        } catch (SGFException sgfe) {
            JOptionPane.showMessageDialog( null,
                                           "file " + fileName + " had an SGF error while loading: " +
                                           sgfe.getMessage() );
            sgfe.printStackTrace();
        }
    }

    @Override
    protected SGFLoader createLoader() {
        return new TwoPlayerSGFLoader();
    }

    /**
     * Initialize the board based on the SGF game.
     */
    @Override
    protected void parseSGFGameInfo( SGFGame game) {

        TwoPlayerController gc = (TwoPlayerController) controller_;

        Enumeration e = game.getInfoTokens();
        int numRows = 15; // default unless specified
        int numCols = 12; // default unless specified
        while (e.hasMoreElements()) {
            InfoToken token = (InfoToken) e.nextElement();
            if (token instanceof Size2Token) {
                Size2Token sizeToken = (Size2Token)token;
                GameContext.log(2, "info token columns =" + sizeToken.getNumColumns() +" rows=" + sizeToken.getNumRows());
                numRows = sizeToken.getNumRows();
                numCols = sizeToken.getNumColumns();
            }
            else if (token instanceof Player2NameToken) {
                Player2NameToken nameToken = (Player2NameToken) token;
                gc.getPlayers().getPlayer2().setName(nameToken.getName());
            }
            else if (token instanceof Player1NameToken) {
                Player1NameToken nameToken = (Player1NameToken) token;
                gc.getPlayers().getPlayer1().setName(nameToken.getName());
            }
        }
        gc.getBoard().setSize(numRows, numCols);
    }

    /**
     *
     */
    @Override
    protected boolean processToken(SGFToken token, MoveList moveList) {

        boolean found = false;
        if (token instanceof PlacementToken ) {
            moveList.add( createMoveFromToken( token ) );
            found = true;
        }
        else if (token instanceof TextToken ) {
            TextToken textToken = (TextToken) token;
            GameContext.log(1, "text="+textToken.getText());
        } else {
            GameContext.log(1, "\nignoring token "+token.getClass().getName());
        }
        return found;
    }


    /**
     * Create a move from the two player move Token
     */
    @Override
    protected Move createMoveFromToken( SGFToken token)
    {
        TwoPlayerMoveToken mvToken = (TwoPlayerMoveToken) token;
        boolean player1 = token instanceof Player1MoveToken;

        return TwoPlayerMove.createMove(mvToken.getToY(), mvToken.getToX(), 0,  new GamePiece(player1));
    }
}
