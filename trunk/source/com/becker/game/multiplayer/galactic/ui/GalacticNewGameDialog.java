package com.becker.game.multiplayer.galactic.ui;

import com.becker.game.common.*;
import com.becker.game.common.ui.*;
import com.becker.game.multiplayer.galactic.*;
import com.becker.ui.GradientButton;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;

public class GalacticNewGameDialog extends NewGameDialog implements ActionListener, ListSelectionListener
{

    private GradientButton addButton_;
    private GradientButton removeButton_;

    // list of players that will be admirals in the game.
    private GalacticPlayerTable galacticPlayerTable_;

    public GalacticNewGameDialog( JFrame parent, GameBoardViewer viewer )
    {
        super( parent, viewer );
        initUI();
    }


    public String getTitle()
    {
        return GameContext.getLabel("GALACTIC_OPTIONS");
    }

    protected void buildMainOptionsPanel(JPanel mainOptionsPanel)
    {
        mainOptionsPanel.add( playerPanel_ );
        mainOptionsPanel.add( boardParamPanel_ );
        if ( customPanel_ != null )
            mainOptionsPanel.add( customPanel_ );
    }

    /**
     * Lets you initialize all the admirals. Some subset of the admirals may be robots and not human.
     * @return a table of players
     */
    protected JPanel createPlayerPanel()
    {
        JPanel p = new JPanel();
        p.setLayout( new BorderLayout() );
        p.setBorder(
                BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(),
                                                  GameContext.getLabel("PLAYERS") ) );

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout( new BorderLayout() );
        JLabel titleLabel = new JLabel(GameContext.getLabel("PLAYERS"));
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout( new BorderLayout() );
        addButton_ = new GradientButton(GameContext.getLabel("ADD"));
        addButton_.setToolTipText( GameContext.getLabel("ADD_TIP") );
        addButton_.addActionListener(this);
        removeButton_ = new GradientButton(GameContext.getLabel("REMOVE"));
        removeButton_.setToolTipText( GameContext.getLabel("REMOVE_PLAYER_TIP") );
        removeButton_.addActionListener(this);
        removeButton_.setEnabled(false);
        buttonsPanel.add(addButton_, BorderLayout.WEST);
        buttonsPanel.add(removeButton_, BorderLayout.EAST);

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(buttonsPanel, BorderLayout.EAST);
        p.add(headerPanel, BorderLayout.NORTH);

        GalacticController c = (GalacticController)controller_;

        galacticPlayerTable_ = new GalacticPlayerTable(c.getPlayers());
        galacticPlayerTable_.addListSelectionListener(this);

        p.add(new JScrollPane(galacticPlayerTable_.getTable()), BorderLayout.CENTER);
        p.setPreferredSize(new Dimension(500,300));
        return p;
    }

    /**
     * the ok button has been pressed, indicating the desire to start the game with
     * the configuration specified.
     */
    protected void ok()
    {
        GalacticController c = (GalacticController)controller_;
        c.setPlayers(galacticPlayerTable_.getPlayers());
        super.ok();
    }

    public void actionPerformed( ActionEvent e )
    {
        super.actionPerformed(e);
        Object source = e.getSource();

        if ( source == addButton_ ) {
            galacticPlayerTable_.addRow();
        }
        else if ( source == removeButton_ ) {
            galacticPlayerTable_.removeSelectedRows();
        }
    }

    /**
     * called when rows are selected/deselected in the player table
     * @param event
     */
    public void valueChanged(ListSelectionEvent event)
    {
        removeButton_.setEnabled(galacticPlayerTable_.getTable().getSelectedRowCount()>0);
    }
}

