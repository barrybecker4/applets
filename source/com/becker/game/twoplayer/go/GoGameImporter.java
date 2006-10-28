package com.becker.game.twoplayer.go;

import com.becker.game.common.*;
import ca.dj.jigo.sgf.tokens.*;
import ca.dj.jigo.sgf.*;

import javax.swing.*;
import java.util.*;
import java.io.*;

/**
 * Imports the stat of a Go game from a file.
 *
 * @author Barry Becker Date: Oct 28, 2006
 */
public class GoGameImporter extends GameImporter {

    public GoGameImporter(GoController controller) {
        super(controller);
    }

    public void restoreFromFile( String fileName ) {

        try {
            FileInputStream iStream = new FileInputStream( fileName );
            GameContext.log( 2, "opening " + fileName );
            SGFGame game = SGFLoader.load( iStream );
            restoreGame( game );
        } catch (FileNotFoundException fnfe) {
            JOptionPane.showMessageDialog( null, "file " + fileName + " was not found." + fnfe.getMessage() );
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog( null, "IOException occurrred while reading " + fileName + " :" + ioe.getMessage() );
        } catch (SGFException sgfe) {
            JOptionPane.showMessageDialog( null, "file " + fileName + " had an SGF error while loading: " + sgfe.getMessage() );
            sgfe.printStackTrace();
        }
    }

    protected void parseSGFGameInfo( SGFGame game) {

        GoController gc = (GoController) controller_;

        Enumeration e = game.getInfoTokens();
        int size = 13; // default unless specified
        while (e.hasMoreElements()) {
            InfoToken token = (InfoToken) e.nextElement();
            if (token instanceof SizeToken) {
                SizeToken sizeToken = (SizeToken)token;
                size = sizeToken.getSize();
            }
            else if (token instanceof KomiToken) {
                KomiToken komiToken = (KomiToken) token;
                gc.setKomi(komiToken.getKomi());
            }
            else if (token instanceof HandicapToken) {
                //HandicapToken handicapToken = (HandicapToken) token;
                // so we don't guess wrong on where the handicap positions are
                // we will rely on their being an AB command to specifically tell where the handicap stones are
                //GameContext.log(2,"***handicap ="+handicapToken.getHandicap());
                //this.setHandicap(handicapToken.getHandicap());
            }
            else if (token instanceof WhiteNameToken) {
                WhiteNameToken nameToken = (WhiteNameToken) token;
                gc.getPlayer2().setName(nameToken.getName());
            }
            else if (token instanceof BlackNameToken) {
                BlackNameToken nameToken = (BlackNameToken) token;
                gc.getPlayer1().setName(nameToken.getName());
            }
            else if (token instanceof KomiToken) {
                KomiToken komiToken = (KomiToken) token;
                gc.setKomi(komiToken.getKomi());
            }
            else if (token instanceof RuleSetToken) {
                //RuleSetToken ruleToken = (RuleSetToken) token;
                //this.setRuleSet(ruleToken.getKomi());
            }
        }
        gc.getBoard().setSize(size, size);
    }


    protected boolean processToken(SGFToken token, List moveList) {

        boolean found = false;
        if (token instanceof MoveToken ) {
            moveList.add( createMoveFromToken( (MoveToken) token ) );
            found = true;
        }
        else if (token instanceof AddBlackToken ) {
            addMoves((PlacementListToken)token, moveList);
            found = true;
        }
        else if (token instanceof AddWhiteToken ) {
            addMoves((PlacementListToken)token, moveList);
            found = true;
        }
        else if (token instanceof CharsetToken ) {
            //CharsetToken charsetToken = (CharsetToken) token;
            //System.out.println("charset="+charsetToken.getCharset());
        }
        else if (token instanceof OverTimeToken ) {
            //OverTimeToken charsetToken = (OverTimeToken) token;
            //System.out.println("charset="+charsetToken.getCharset());
        }
        else if (token instanceof TextToken ) {
            TextToken textToken = (TextToken) token;
            System.out.println("text="+textToken.getText());
        } else {
            System.out.println("\nignoring token "+token.getClass().getName());
        }
        return found;
    }

    /**
     * add a sequence of moves all at once.
     * Such as placing handicaps when reading from an sgf file.
     * @param token
     */
    private static void addMoves(PlacementListToken token, List moveList) {
        Enumeration points = token.getPoints();
        boolean player1 = token instanceof AddBlackToken;

        while (points.hasMoreElements()) {
            Point point = (Point)points.nextElement();
            moveList.add( new GoMove( point.y, point.x, 0, new GoStone(player1)));
        }
    }


    protected Move createMoveFromToken( MoveToken token)
    {
        if (token.isPass()) {
            return GoMove.createPassMove(0, !token.isWhite());
        }
        return new GoMove( token.getY(), token.getX(), 0, new GoStone(!token.isWhite()));
    }

}
