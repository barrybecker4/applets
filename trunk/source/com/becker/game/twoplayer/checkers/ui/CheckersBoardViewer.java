package com.becker.game.twoplayer.checkers.ui;

import com.becker.common.Location;
import com.becker.game.common.BoardPosition;
import com.becker.game.common.GameContext;
import com.becker.game.common.GameController;
import com.becker.game.common.ui.GameBoardRenderer;
import com.becker.game.common.ui.ViewerMouseListener;
import com.becker.game.twoplayer.checkers.CheckersController;
import com.becker.game.twoplayer.common.ui.AbstractTwoPlayerBoardViewer;

import java.awt.event.MouseEvent;

/**
 *  This class takes a CheckersController as input and displays the
 *  Current state of the Checkers Game. The CheckersController contains a CheckersBoard
 *  which describes this state.
 *
 *  @author Barry Becker
 */
public class CheckersBoardViewer extends AbstractTwoPlayerBoardViewer {

    /**
     *  Construct the viewer
     */
    public CheckersBoardViewer() {
    }


    @Override
    protected GameController createController()
    {
        return new CheckersController();
    }

    @Override
    protected GameBoardRenderer getBoardRenderer() {
        return CheckersBoardRenderer.getRenderer();
    }

    @Override
    protected ViewerMouseListener createViewerMouseListener() {
        return new CheckersViewerMouseListener(this);
    }

    /**
     * @return the tooltip for the panel given a mouse event
     */
    @Override
    public String getToolTipText( MouseEvent e )
    {
        Location loc = getBoardRenderer().createLocation(e);
        StringBuffer sb = new StringBuffer( "<html><font=-3>" );

        BoardPosition space = controller_.getBoard().getPosition( loc );
        if ( space != null && space.isOccupied() && GameContext.getDebugMode() > 0 ) {
            sb.append( loc );
            sb.append("<br>");
            sb.append(space.toString());
        }
        sb.append( "</font></html>" );
        return sb.toString();
    }

}
