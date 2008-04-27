package com.becker.game.multiplayer.set.ui;


import com.becker.game.common.*;
import com.becker.game.common.ui.*;
import com.becker.game.multiplayer.set.*;
import com.becker.common.*;

import com.becker.game.multiplayer.common.MultiGameController;
import com.becker.game.multiplayer.common.online.SurrogatePlayer;
import com.becker.game.multiplayer.common.ui.MultiGameViewer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.List;

/**
 *  Shows the current cards in the Set Game in a canvas.
 *
 * @author Barry Becker
 */
public final class SetGameViewer extends MultiGameViewer
                                 implements MouseMotionListener
{

    private Card currentlyHighlightedCard_ = null;


    // Constructor.
    SetGameViewer()
    {
        NumberFormat formatter_=new DecimalFormat();
        formatter_.setGroupingUsed(true);
        formatter_.setMaximumFractionDigits(0);

        this.addMouseMotionListener(this);
    }


    public void startNewGame() {
        controller_.reset();
    }

    /**
     *
     * @return the game specific controller for this viewer.
     */
    protected MultiGameController createController() {
        return new SetController();
    }

    
    /**
     * make the computer move and show it on the screen.
     *
     * @param player computer player to move
     * @return done return true if the game is over after moving
     */
    public boolean doComputerMove(Player player)
    {
        assert false : " no computer player for set yet. coming soon!";
        return false;
    }
    
    /**
     * make the computer move and show it on the screen.
     *
     * @param player computer player to move
     * @return done return true if the game is over after moving
     */
    public boolean doSurrogateMove(SurrogatePlayer player)
    {
        assert false : " no online pla for set yet. coming soon!";
        return false;
    }

    public int getCanvasWidth() {
        return getWidth() - 2 * CardRenderer.LEFT_MARGIN;
    }

    public int getNumColumns() {
        float rat = (float) getCanvasWidth() / (getHeight() - 2 * CardRenderer.TOP_MARGIN);

        int numColumns = 20;
        if (rat < 0.05) {
            numColumns = 1;
        } else if (rat < 0.15) {
            numColumns = 2;
        } else if (rat < 0.3) {
            numColumns = 3;
        } else if (rat < 0.6) {
            numColumns = 4;
        } else if (rat < 0.9) {
            numColumns = 5;
        } else if (rat < 1.2) {
            numColumns = 6;
        } else if (rat < 2.0) {
            numColumns = 7;
        } else if (rat < 3.4) {
            numColumns = 10;
        }
        return numColumns;
    }

    private Dimension calcCardDimension(int numCols) {
        int cardWidth = getCanvasWidth() / numCols;
        return new Dimension(cardWidth, (int) (cardWidth * CardRenderer.CARD_HEIGHT_RAT));
    }


    protected void drawLastMoveMarker(Graphics2D graphics2d) {}

    /**
     * This renders the current state of the puzzle to the screen.
     * Render each card in the deck.
     */
    protected void paintComponent( Graphics g )
    {
        int i;

        super.paintComponents( g );
        // erase what's there and redraw.
        SetController c = (SetController)controller_;

        g.clearRect( 0, 0, getWidth(), getHeight() );
        g.setColor( BACKGROUND_COLOR );
        g.fillRect( 0, 0, getWidth(), getHeight() );

        int numCols = getNumColumns();

        Dimension cardDim = calcCardDimension(numCols);
        int cardWidth = (int) cardDim.getWidth();
        int cardHeight = (int) cardDim.getHeight();

        for (i = 0; i<c.getNumCardsShowing(); i++ ) {
            int row = i / numCols;
            int col = i % numCols;
            int colPos = col * cardWidth + CardRenderer.LEFT_MARGIN;
            int rowPos = row * cardHeight + CardRenderer.TOP_MARGIN;
            CardRenderer.render((Graphics2D) g, c.getDeck().get(i),
                                new Location(colPos, rowPos), cardWidth, cardHeight, false);
        }
    }


    public String getGameOverMessage() {
        SetPlayer winner =  ((SetController)controller_).determineWinner();
        return "the game is over. The winner is " + winner.getName() + " with " + winner.getNumSetsFound() + "sets";
    }

    /**
     * @return  the card that the mouse is currently over (at x, y coords)
     */
    private Card findCardOver(int x, int y) {
        int numCols = getNumColumns();
        SetController c = (SetController)controller_;

        Dimension cardDim = calcCardDimension(numCols);
        int cardWidth = (int) cardDim.getWidth();
        int cardHeight = (int) cardDim.getHeight();

        int selectedIndex = -1;
        for (int i = 0; i<c.getNumCardsShowing(); i++ ) {
            int row = i / numCols;
            int col = i % numCols;
            int colPos = col * cardWidth + CardRenderer.LEFT_MARGIN;
            int rowPos = row * cardHeight + CardRenderer.TOP_MARGIN;
            if (   x > colPos && x <= colPos + cardDim.getWidth()
                && y > rowPos && y <= rowPos + cardDim.getHeight()) {
                selectedIndex = i;
                break;
            }
        }
        if (selectedIndex == -1) {
            return null;
        }
        return c.getDeck().get(selectedIndex);
    }

    public void mouseMoved(MouseEvent e) {
        Card card = findCardOver(e.getX(), e.getY());

        boolean changed = card != currentlyHighlightedCard_;

        if (changed) {

            if (currentlyHighlightedCard_ != null) {
                currentlyHighlightedCard_.setHighlighted(false);
            }
            if (card != null) {
                currentlyHighlightedCard_ = card;
                currentlyHighlightedCard_.setHighlighted(true);
            } else {
                currentlyHighlightedCard_ = null;
            }

            this.repaint();
        }
    }

    public List<Card> getSelectedCards() {

        List<Card> selected = new ArrayList<Card>();
        SetController c = (SetController)controller_;

        for (int i = 0; i<c.getNumCardsShowing(); i++ ) {
            Card card = c.getDeck().get(i);
            if (card.isSelected()) {
                selected.add(card);
            }
        }
        return selected;
    }

    private static void deselectCards(List<Card> cards) {

         for (int i = 0; i < cards.size(); i++) {
             cards.get(i).setSelected(false);
         }
    }
    
    /**
     * draw a grid of some sort if there is one.
     * none by default.
     */
    protected void drawGrid(Graphics2D g2, int startPos, int rightEdgePos, int bottomEdgePos, int start,
                            int nrows1, int ncols1, int gridOffset) {}


    public void mouseDragged(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {

        SetController c = (SetController)controller_;

        if (c.getCurrentPlayer() == null) {
            JOptionPane.showMessageDialog(this, "Before you can select a set, you must specify a player on the right.");
            return;
        }
        Card card = findCardOver(e.getX(), e.getY());
        if (card != null) {
           card.toggleSelect();
           this.repaint();
        }
        // if there are 3 cards selected. Check to see if it constitutes a set.
        // if it does, show a message to that effect, unselect them, delete them and add 3 more from the deck.
        // if not, then show a message, and deselect them.
        List<Card> selectedCards = getSelectedCards();
        if (selectedCards.size() == 3) {

            SetPlayer p = ((SetPlayer)c.getCurrentPlayer());
            if (Card.isSet(selectedCards)) {
                JOptionPane.showMessageDialog(this, "Congratulations, you found a set!");

                p.incrementNumSetsFound();
                c.removeCards(selectedCards);
                c.addCards(3);

            } else {
                JOptionPane.showMessageDialog(this, "NO! that is not a set.");
                p.decrementNumSetsFound();
                c.gameChanged();
            }
            deselectCards(selectedCards);
            c.setCurrentPlayer(null);
            this.repaint();
        }
    }

}

