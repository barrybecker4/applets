/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.common.ui.viewer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *  Do nothing by default for all these. Subclasses must override some of them.
 *
 *  @author Barry Becker
 */
public class ViewerMouseListener implements MouseListener, MouseMotionListener {

    protected GameBoardViewer viewer_;

    /**
     * Constructor.
     */
    public ViewerMouseListener(GameBoardViewer viewer) {
        viewer_ =  viewer;
    }

    protected GameBoardRenderer getRenderer() {
        return viewer_.getBoardRenderer();
    }

    public void reset() {}
    /**
     * make the human move and show it on the screen,
     * then depending on the options, the computer may move.
     */
    public void mouseClicked( MouseEvent e ) {}
    public void mousePressed( MouseEvent e ) {}
    public void mouseReleased( MouseEvent e ) {}
    public void mouseEntered( MouseEvent e ) {}
    public void mouseExited( MouseEvent e ) {}

    // implement MouseMotionListener interface
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
}