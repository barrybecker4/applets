/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.multiplayer.common.ui;

import com.becker.game.multiplayer.common.MultiGameController;
import com.becker.game.multiplayer.common.MultiGamePlayer;
import com.becker.ui.dialogs.OptionsDialog;

import javax.swing.*;
import java.awt.*;

/**
 * Allow the user to specify a poker action
 * @author Barry Becker
 */
public abstract class ActionDialog extends OptionsDialog {

    protected MultiGamePlayer player_;
    protected MultiGameController controller_;

    /**
     * constructor - create the tree dialog.
     * @param gc pokerController
     */
    protected ActionDialog(MultiGameController gc, Component parent) {
        controller_ = gc;
        player_ = controller_.getCurrentPlayer();
        Point p = parent.getLocationOnScreen();
        // offset the dlg so the board is visible as a reference
        setLocation((int)(p.getX() + 0.7*getParent().getWidth()),
                                 (int)(p.getY() + getParent().getHeight()/3.0));
        showContent();
    }

    /**
     * ui initialization of the tree control.
     */
    @Override
    protected JComponent createDialogContent() {
        setResizable( true );
        JPanel mainPanel = new JPanel();
        mainPanel =  new JPanel();
        mainPanel.setLayout( new BorderLayout() );
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        JPanel personalInfoPanel = createPersonalInfoPanel();
        JPanel buttonsPanel = createButtonsPanel();

        JPanel instructions = createInstructionsPanel();

        mainPanel.add(personalInfoPanel , BorderLayout.NORTH);
        mainPanel.add(instructions, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    protected abstract JPanel createPersonalInfoPanel();
    

    private JPanel createInstructionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(5,5,5,5)));
        PlayerLabel playerLabel_ = new PlayerLabel();
        playerLabel_.setPlayer(player_);

        JPanel gameSpecificInstructions = createGameInstructionsPanel();
        
        //panel.setPreferredSize(new Dimension(400, 100));
        panel.add(playerLabel_, BorderLayout.NORTH);
        panel.add(gameSpecificInstructions, BorderLayout.CENTER);
              
        return panel;
    }

    protected abstract JPanel createGameInstructionsPanel();
    

    /**
     *  create the OK/Cancel buttons that go at the bottom.
     */
    @Override
    protected abstract JPanel createButtonsPanel();

}

