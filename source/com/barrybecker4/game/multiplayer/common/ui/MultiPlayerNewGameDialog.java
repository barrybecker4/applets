/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.multiplayer.common.ui;

import com.barrybecker4.game.common.GameContext;
import com.barrybecker4.game.common.GameViewable;
import com.barrybecker4.game.common.ui.dialogs.NewGameDialog;
import com.barrybecker4.game.multiplayer.common.MultiGameOptions;
import com.barrybecker4.ui.components.GradientButton;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

/**
 * Manager players for new local game.
 *
 * @author Barry Becker
 */
public abstract class MultiPlayerNewGameDialog
                extends NewGameDialog
                implements ActionListener, ListSelectionListener {

    // add / remove players
    private GradientButton addButton_;
    private GradientButton removeButton_;

    /** list of players in the local game. */
    private PlayerTable playerTable_;

    /**
     * Constructor.
     */
    protected MultiPlayerNewGameDialog( Component parent, GameViewable viewer) {
        super( parent, viewer);
        this.setResizable(true);
    }

    /**
     * Lets you initialize all the players. Some subset of the players may be robots and not human.
     * @return a table of players
     */
    @Override
    protected JPanel createPlayerAssignmentPanel() {
        JPanel playerPanel = new JPanel(new BorderLayout());
        playerPanel.setBorder(
                BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(),
                                                  "Add Players for a new local game") );

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(GameContext.getLabel("PLAYERS"));
        JPanel buttonsPanel = new JPanel(new BorderLayout());

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
        playerPanel.add(headerPanel, BorderLayout.NORTH);

        playerTable_ = createPlayerTable();
        playerTable_.addListSelectionListener(this);

        playerPanel.add(new JScrollPane(playerTable_.getTable()), BorderLayout.CENTER);
        playerPanel.setPreferredSize(new Dimension(500,300));
        return playerPanel;
    }


    /**
     * @return  shows the list of local playes that will play this local game
     */
    protected abstract PlayerTable createPlayerTable();

    /**
     * panel which allows changing board specific properties.
     */
    @Override
    protected JPanel createBoardParamPanel() {
        return null;
    }

    /**
     * the ok button has been pressed, indicating the desire to start the game with
     * the configuration specified.
     */
    @Override
    protected void ok() {
        Component selectedTab = tabbedPanel_.getSelectedComponent();
        if (selectedTab == playLocalPanel_)  {
            controller_.setPlayers(playerTable_.getPlayers());
        }
        super.ok();
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        super.actionPerformed(e);
        Object source = e.getSource();

        if ( source == addButton_ ) {
            playerTable_.addRow();
        }
        else if ( source == removeButton_ ) {
            playerTable_.removeSelectedRows();
        }
        MultiGameOptions options = (MultiGameOptions) controller_.getOptions();
        addButton_.setEnabled(playerTable_.getModel().getRowCount() < options.getMaxNumPlayers());

    }

    /**
     * Called when rows are selected/deselected in the player table.
     * @param event
     */
    public void valueChanged(ListSelectionEvent event) {
        MultiGameOptions options = (MultiGameOptions) controller_.getOptions();
        boolean enabled = playerTable_.getTable().getSelectedRowCount() > 0
                          && playerTable_.getModel().getRowCount() > options.getMinNumPlayers();
        removeButton_.setEnabled(enabled);
    }
}

