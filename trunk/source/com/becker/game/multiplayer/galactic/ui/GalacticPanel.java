package com.becker.game.multiplayer.galactic.ui;

import com.becker.game.common.*;
import com.becker.game.common.ui.*;

import javax.swing.*;

/**
 *  This class defines the main UI for the Galactic Empire game applet.
 *  It can be run as an applet or application.
 *
 *  @author Barry Becker
 */
public class GalacticPanel extends GamePanel
{

    /**
     *  Construct the panel.
     */
    public GalacticPanel()
    {}


    @Override
    public String getTitle()
    {
        return  GameContext.getLabel("GALACTIC_TITLE");
    }


    @Override
    protected GameBoardViewer createBoardViewer()
    {
        return new GalaxyViewer();
    }

    @Override
    protected NewGameDialog createNewGameDialog( JFrame parent, GameViewable viewer )
    {
        return new GalacticNewGameDialog( parent, viewer );
    }

    @Override
    protected GameOptionsDialog createOptionsDialog( JFrame parent, GameController controller )
    {
        return new GalacticOptionsDialog( parent, controller );
    }

    @Override
    protected GameInfoPanel createInfoPanel(GameController controller)
    {

        return new GalacticInfoPanel( controller);
    }

    // Display the help dialog to give instructions
    @Override
    protected void showHelpDialog()
    {
        String name = getTitle();
        String comments = GameContext.getLabel("GALACTIC_TITLE");
        String overview = GameContext.getLabel("GALACTIC_OVERVIEW");
        showHelpDialog( name, comments, overview );
    }

}



