/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.twoplayer.common.ui.dialogs;

import com.barrybecker4.common.util.FileUtil;
import com.barrybecker4.game.common.GameContext;
import com.barrybecker4.game.common.GameViewModel;
import com.barrybecker4.game.common.board.IRectangularBoard;
import com.barrybecker4.game.common.player.PlayerList;
import com.barrybecker4.game.common.ui.dialogs.NewGameDialog;
import com.barrybecker4.game.common.ui.panel.GridBoardParamPanel;
import com.barrybecker4.game.twoplayer.common.TwoPlayerController;
import com.barrybecker4.ui.file.FileChooserUtil;
import com.barrybecker4.ui.file.TextFileFilter;
import com.barrybecker4.ui.util.GUIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Use this modal dialog to initialize the required game parameters that
 * are needed in order to start play in a 2 player game.
 *
 * @author Barry Becker
 */
public class TwoPlayerNewGameDialog extends NewGameDialog
                                    implements ActionListener {

    private JCheckBox optimizationCheckbox_;
    private PlayerAssignmentPanel playersPanel_;

    /**
     * constructor
     */
    protected TwoPlayerNewGameDialog(Component parent, GameViewModel viewer ) {
        super(parent, viewer );
    }

    protected TwoPlayerController get2PlayerController() {
        return (TwoPlayerController) controller_;
    }

    /**
     * @return panel for the local player
     */
    @Override
    protected JPanel createNewLocalGamePanel() {
        JPanel playLocalPanel = new JPanel();
        playLocalPanel.setLayout(new BoxLayout(playLocalPanel, BoxLayout.Y_AXIS ) );

        playersPanel_ = createPlayerAssignmentPanel();
        JPanel optimizationPanel = createOptimizationPanel();
        JPanel boardParamPanel = createBoardParamPanel();
        JPanel customPanel = createCustomPanel();

        playLocalPanel.add(playersPanel_ );
        playLocalPanel.add( optimizationPanel );
        playLocalPanel.add( boardParamPanel );
        if ( customPanel != null )
            playLocalPanel.add( customPanel );
        return playLocalPanel;
    }

    /**
     * panel which allows changing board specific properties.
     */
    @Override
    protected GridBoardParamPanel createBoardParamPanel() {
        IRectangularBoard b = (IRectangularBoard) board_;
        return new GridBoardParamPanel(b.getNumRows(), b.getNumCols(), createCustomBoardConfigPanel());
    }


    private JPanel createOptimizationPanel() {
        JPanel p = new JPanel();
        p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
        p.setBorder(
                BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(),
                                                  GameContext.getLabel("OPTIMIZATION_TITLE") ) );
        optimizationCheckbox_ = new JCheckBox( GameContext.getLabel("OPTIMIZATION"), false );
        p.setToolTipText(GameContext.getLabel("OPTIMIZATION_TIP"));
        optimizationCheckbox_.addActionListener(this);
        p.add( optimizationCheckbox_ , BorderLayout.CENTER);
        p.add( new JPanel());

        return p;
    }

    @Override
    protected PlayerAssignmentPanel createPlayerAssignmentPanel() {
        return new PlayerAssignmentPanel(get2PlayerController(), parent_);
    }

    @Override
    protected void ok() {
        TwoPlayerController c = get2PlayerController();
        IRectangularBoard board = (IRectangularBoard) board_;

        if (board != null && gridParamPanel_!= null) {
            board.setSize(gridParamPanel_.getRowSize(), gridParamPanel_.getColSize());
        }

        PlayerList players = c.getPlayers();
        if (optimizationCheckbox_.isSelected())
        {
            players.getPlayer1().setHuman(false);
            players.getPlayer2().setHuman(false);
            c.getTwoPlayerOptions().setAutoOptimize(true);
        }
        else {
            playersPanel_.ok();
        }
        if (board != null && gridParamPanel_ != null) {
            board.setSize( gridParamPanel_.getRowSize(), gridParamPanel_.getColSize() );
        }
        super.ok();
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        Object source = e.getSource();

        if ( source == startButton_ ) {
            ok();
        }
        else if ( source == cancelButton) {
            cancel();
        }
        else if (source == optimizationCheckbox_) {
            boolean checked = optimizationCheckbox_.isSelected();
            if (checked)  {
                // open a dlg to get a location for the optimization log
                // if they cancel this dlg then we leave the checkbox unchecked
                if (GUIUtil.isStandAlone())  {
                    JOptionPane.showMessageDialog(this, GameContext.getLabel("CANT_RUN_OPT_WHEN_STANDALONE"));
                }
                else {
                    JFileChooser chooser = FileChooserUtil.getFileChooser();
                    chooser.setCurrentDirectory( new File( FileUtil.getHomeDir() ) );
                    chooser.setFileFilter(new TextFileFilter());
                    int state = chooser.showOpenDialog( this );
                    File file = chooser.getSelectedFile();
                    if ( file == null || state != JFileChooser.APPROVE_OPTION )  {
                        optimizationCheckbox_.setSelected(false);
                        return;
                    }
                    else
                        get2PlayerController().getTwoPlayerOptions().setAutoOptimizeFile( file.getAbsolutePath() );
                 }
            }

            playersPanel_.setBothComputerPlayers();
        }
        else {
            throw new IllegalStateException("unexpected source="+ source);
        }
    }
}