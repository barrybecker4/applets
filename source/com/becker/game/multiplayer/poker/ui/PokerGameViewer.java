package com.becker.game.multiplayer.poker.ui;

import ca.dj.jigo.sgf.tokens.MoveToken;
import com.becker.game.common.*;
import com.becker.game.common.ui.GameBoardViewer;
import com.becker.game.common.ui.GameChangedEvent;
import com.becker.game.common.Move;
import com.becker.game.multiplayer.poker.*;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.*;

/**
 *  Takes a PokerController as input and displays the
 *  current state of the Poker Game. The PokerController contains a PokerTable object
 *  which describes this state.
 *
 *  @author Barry Becker
 */
public class PokerGameViewer extends GameBoardViewer
{

    private static final Color GRID_COLOR = Color.GRAY;
    private static final Color TABLE_COLOR = new Color(190, 160, 110);
    private boolean winnerDialogShown_ = false;

    //Construct the application
    public PokerGameViewer()
    {
        pieceRenderer_ = PokerRenderer.getRenderer();
    }

    protected PokerController createController()
    {
        return new PokerController();
    }

    protected int getDefaultCellSize()
    {
        return 16;
    }

    protected Color getDefaultGridColor()
    {
        return GRID_COLOR;
    }

    /**
     * start over with a new game using the current options.
     */
    public final void startNewGame()
    {
        reset();
        winnerDialogShown_ = false;
        this.sendGameChangedEvent(null);  // get the info panel to refresh with 1st players name

        if (!controller_.getFirstPlayer().isHuman())
            controller_.computerMovesFirst();
    }

    /**
     * whether or not to draw the pieces on cell centers or vertices (like go or pente, but not like checkers).
     */
    protected boolean offsetGrid()
    {
        return true;
    }

    protected void drawLastMoveMarker(Graphics2D g2)
    {}


    /**
     * This will create a move from an SGF token
     */
    protected Move createMoveFromToken( MoveToken token, int moveNum )
    {
        GameContext.log(0, "not implemented yet" );
        return null;
    }

    public void mousePressed( MouseEvent e )
    {
        //Location loc = createLocation(e, getCellSize());
        //Galaxy board = (Galaxy) controller_.getBoard();
        // nothing to do here really for this kind of game
    }



     /**
      * display a dialog at the end of the game showing who won and other relevant
      * game specific information.
      */
     protected void showWinnerDialog()
     {
         //TallyDialog tallyDialog = new TallyDialog(parent_, (PokerController)controller_);
         //tallyDialog.showDialog();

         String message = getGameOverMessage();
         JOptionPane.showMessageDialog( this, message, GameContext.getLabel("GAME_OVER"),
                   JOptionPane.INFORMATION_MESSAGE );
     }


    /**
     * @return   the message to display at the completion of the game.
     */
    protected String getGameOverMessage()
    {
        return "Game Over";
    }


    /**
     * make the computer move and show it on the screen.
     *
     * @param player computer player to move
     * @return done return true if the game is over after moving
     */
    public boolean doComputerMove(Player player)
    {
        assert(!player.isHuman());
        PokerRobotPlayer robot = (PokerRobotPlayer)player;
        PokerController pc = (PokerController) controller_;

        switch (robot.getAction()) {
            case FOLD : robot.fold();
                break;
            case CALL : 
                int callAmount =  pc.getCurrentMaxContribution() - robot.getContribution();
                if (callAmount > 0)
                    robot.contributeToPot(pc, callAmount);
                break;
            case RAISE : robot.contributeToPot(pc, robot.getRaise());
                break;
        }

        /*
        // records the result on the board.
        Move lastMove = gc.getLastMove();
        PokerTurn gmove = PokerTurn.createMove((lastMove==null)? 0 : lastMove.moveNumber + 1);
        gc.makeMove(gmove);
        */
        this.refresh();

        pc.advanceToNextPlayer();

        return false;
    }

    /**
     * Implements the GameChangedListener interface.
     * Called when the game has changed in some way
     * @param evt
     */
    public void gameChanged(GameChangedEvent evt)
    {
        if (controller_.done() && !winnerDialogShown_)  {
            winnerDialogShown_ = true;
            this.showWinnerDialog();
        }
        else if (!winnerDialogShown_) {
             super.gameChanged(evt);
        }
    }


    /**
     * This will run all the battle simulations needed to calculate the result and put it in the new move.
     * Simulations may actually be a reinforcements instead of a battle.
     * @param lastMove the move to show (but now record)
     */
    public PokerRound createMove(Move lastMove)
    {
        PokerRound gmove = PokerRound.createMove((lastMove==null)? 0 : lastMove.moveNumber+1);

        // for each order of each player, apply it for one year
        // if there are battles, show them in the battle dialog and record the result in the move.
        Player[] players = controller_.getPlayers();

        /*
        for (int i=0; i< players.length; i++) {
            List orders = ((PokerPlayer)players[i]).getOrders();
            Iterator orderIt = orders.iterator();
            while (orderIt.hasNext()) {
                Order order = (Order)orderIt.next();
                // have we reached our destination?
                // if so show and record the battle, and then remove the order from the list.
                // If not adjust the distance remaining.
                order.incrementYear();
                if (order.hasArrived()) {

                    Planet destPlanet = order.getDestination();
                    BattleSimulation battle = new BattleSimulation(order, destPlanet);
                    gmove.addSimulation(battle);

                    //  show battle dialog if not all computers playing
                    if (!controller_.allPlayersComputer()) {

                        BattleDialog bDlg = new BattleDialog(parent_, battle, this);
                        //bDlg.setLocationRelativeTo(this);

                        Point p = this.getParent().getLocationOnScreen();
                        // offset the dlg so the Galaxy grid is visible as a reference.
                        bDlg.setLocation((int)(p.getX()+getParent().getWidth()), (int)(p.getY()+.6*getParent().getHeight()));
                        bDlg.setModal(true);
                        bDlg.setVisible(true);
                    }

                    destPlanet.setOwner( battle.getOwnerAfterAttack());
                    destPlanet.setNumShips( battle.getNumShipsAfterAttack() );

                    // remove this order as it has arrived.
                    orderIt.remove();
                }
            }
        }
        */
        return gmove;
    }

    public void highlightPlayer(Player player, boolean hightlighted)
    {
        // player.setHighlighted(hightlighted);
        this.refresh();
    }



    protected void drawBackground(Graphics g, int startPos, int rightEdgePos, int bottomEdgePos )
    {
        g.setColor( backgroundColor_ );
        int width = this.getWidth();
        int height = this.getHeight();
        g.setColor(TABLE_COLOR);
        g.fillOval((int)(.05*width), (int)(0.05*height), (int)(.9*width), (int)(0.9*height));
    }

    /**
     * no grid in poker
     */
    protected void drawGrid(Graphics2D g2, int startPos, int rightEdgePos, int bottomEdgePos, int start,
                            int nrows1, int ncols1, int gridOffset) {
    }


    private static final float OFFSET = .25f;

    /**
     * Draw the pieces and possibly other game markers for both players.
     */
    protected void drawMarkers( int nrows, int ncols, Graphics2D g2 )
    {
        // draw the pot in the middle
        Location loc = new Location(getBoard().getNumRows()/2, getBoard().getNumCols()/2);
        ((PokerRenderer)pieceRenderer_).renderChips(g2, loc, ((PokerController)controller_).getPotValue(),
                                                    this.getCellSize());


        // now draw the players and their stuff (face, anme, chips, cards, etc)
        super.drawMarkers(nrows, ncols, g2);
    }

    /**
     * @return the tooltip for the panel given a mouse event
     */
    public String getToolTipText( MouseEvent e )
    {
        Location loc = createLocation(e, getCellSize());
        StringBuffer sb = new StringBuffer( "<html><font=-3>" );

        BoardPosition space = controller_.getBoard().getPosition( loc );
        if ( space != null && space.isOccupied() && GameContext.getDebugMode() >= 0 ) {
            //sb.append(((Planet)space.getPiece()).toHtml());
            sb.append("<br>");
            sb.append( loc );
        }
        sb.append( "</font></html>" );
        return sb.toString();
    }

}
