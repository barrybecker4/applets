package com.becker.ui;

import javax.swing.*;

/**
 * Base class for programs that you want to be
 * run as applications or resizable applets.
 *
 * @author Barry Becker  Date: Dec 2008
 */
public abstract class ApplicationApplet extends JApplet
{
    protected ResizableAppletPanel resizablePanel_;

    static {
        System.out.println("ApplicationApplet static init." );
        GUIUtil.setStandAlone((GUIUtil.getBasicService() != null));
    }
    /**
     * Construct the application.
     */
    public ApplicationApplet() {
        GUIUtil.setCustomLookAndFeel();
    }

    /**
     * initialize. Called by the browser.
     */
    public void init() {

        resizablePanel_ =
                new ResizableAppletPanel(createMainPanel());

        getContentPane().add(resizablePanel_);
    }

    /**
     * create and initialize the puzzle
     * (init required for applet)
     */
    protected abstract JPanel createMainPanel();

    /**
     * This method allow javascript to resize the applet from the browser.
     */
    public void setSize( int width, int height )
    {
        resizablePanel_.setSize( width, height );
    }


    /**
     * called by the browser after init(), if running as an applet
     */
    public void start() {}

}

