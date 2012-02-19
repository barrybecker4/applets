/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.common.ui;

import com.becker.game.common.GameContext;
import com.becker.game.common.GameController;
import com.becker.game.common.ui.dialogs.GameOptionsDialog;
import com.becker.game.common.ui.panel.GameChangedListener;
import com.becker.game.common.ui.panel.GamePanel;
import com.becker.game.twoplayer.common.TwoPlayerController;
import com.becker.game.twoplayer.common.TwoPlayerOptions;
import com.becker.game.twoplayer.common.ui.dialogs.TwoPlayerOptionsDialog;
import com.becker.game.twoplayer.common.ui.gametree.GameTreeCellRenderer;
import com.becker.game.twoplayer.common.ui.gametree.GameTreeDialog;
import com.becker.ui.components.TexturedPanel;
import com.becker.ui.themes.BarryTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This is an abstract base class for a two player game UI.
 * See derived classes for specific game implementations.
 *
 * It contains a dockable toolbar which shows at least 5 buttons:
 *  new game, undo, redo, options, and help.
 * It puts the game board viewer in a scrollable pane on the left.
 * There is an info window on the right that gives statistics about the current game state.
 * There is a progress bar at the bottom that shows whenever the computer is thinking.
 *
 * This class is the main panel in the applet or application.
 * It contains everything related to actually playing the board game.
 *
 *  @author Barry Becker
 */
public abstract class TwoPlayerPanel extends GamePanel
                                  implements ActionListener, GameChangedListener {

    /** for showing progress while the computer thinks. */
    private JProgressBar progressBar_;

    /** dialog to show the game tree while processing and other debug info. */
    private GameTreeDialog treeDialog_;

    /**
     * Construct the panel.
     */
    protected TwoPlayerPanel()  {}


    public TwoPlayerController get2PlayerController() {
        return (TwoPlayerController)boardViewer_.getController();
    }

    /**
     * UIComponent initialization.
     */
    @Override
    protected void initGui(Component parent) {
        super.initGui(parent);

        // we create a separate controller for the TreeDialog so it can browse without
        // disturbing the state of the actual game.
        treeDialog_ = createGameTreeDialog();

        TwoPlayerOptions options = get2PlayerController().getTwoPlayerOptions();
        GameContext.log(2, "2player pane init  get2PlayerController().getShowGameTree() ="
                + options.getShowGameTree() );
        if (options.getShowGameTree()) {
            showGameTreeDialog();
        }
    }

    @Override
    protected JPanel createBottomDecorationPanel() {
        progressBar_ = createProgressBar();
        boardViewer_.setProgressBar(progressBar_);

        // put the progress bar in a panel so it does not cause a re-layout
        JPanel progressPanel = new TexturedPanel(BG_TEXTURE);
        progressPanel.setLayout(new BorderLayout());
        //progressPanel.setPreferredSize(new Dimension(1000, 20));
        progressPanel.add(progressBar_, BorderLayout.CENTER);
        return progressPanel;
    }

    JProgressBar createProgressBar()  {
        JProgressBar progressBar = new JProgressBar();
        progressBar.setOpaque(false);
        // show only when used
        progressBar.setVisible(!get2PlayerController().getPlayers().allPlayersHuman());
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setBackground(BarryTheme.UI_COLOR_SECONDARY1);
        progressBar.setForeground(BarryTheme.UI_COLOR_PRIMARY2);
        progressBar.setStringPainted(true);
        progressBar.setBorderPainted(false);
        progressBar.setString(" ");
        return progressBar;
    }

    /**
     * @return  the dialog used to specify various game options and parameters.
     */
    @Override
    protected GameOptionsDialog createOptionsDialog(Component parent, GameController controller ) {
        return new TwoPlayerOptionsDialog(parent, (TwoPlayerController) controller);
    }

    /**
     * The game tree dialog shows the game tree for better understanding.
     * but it also has its own (view only) viewer that is used to show any particular node in the game tree.
     * @return the new game tree dialog
     */
    protected GameTreeDialog createGameTreeDialog() {
        AbstractTwoPlayerBoardViewer viewer = (AbstractTwoPlayerBoardViewer)createBoardViewer();
        // we don't want it to receive click events
        viewer.setViewOnly(true);
        GameTreeCellRenderer renderer =
                new GameTreeCellRenderer((TwoPlayerPieceRenderer)viewer.getPieceRenderer());
        return new GameTreeDialog( null, viewer, renderer );
    }

    /**
     *  Take the root from the treeDialog and set it on the TwoPlayerController so it can
     * create the tree and allow the treeDialog to show it when the
     * change event happens.
     */
    private void showGameTreeDialog() {
         treeDialog_.reset();
         boardViewer_.addGameChangedListener( treeDialog_ );
         get2PlayerController().setGameTreeViewable( treeDialog_.getGameTreeViewable() );
         treeDialog_.setVisible(true);
    }

    /**
     * handle button click actions.
     * If you add your own custom buttons, you should override this, but be sure the first line is
     * <P>
     * super.actionPerformed(e);
     */
    @Override
    public void actionPerformed( ActionEvent e ) {
        Object source = e.getSource();

        if ( source == toolBar_.getNewGameButton()) {
            get2PlayerController().pause();
            boolean canceled = newGameDialog_.showDialog();
            if ( !canceled ) { // newGame a game with the newly defined options
                startGame();
            }
            else {
                getBoardViewer().continueProcessing();
            }
        }
        else if ( source == toolBar_.getUndoButton() ) {
            getBoardViewer().undoLastManMove();
            // gray it if there are now no more moves to undo
            toolBar_.getUndoButton().setEnabled(boardViewer_.canUndoMove());
            toolBar_.getRedoButton().setEnabled(true);
        }
        else if ( source == toolBar_.getRedoButton() ) {
            getBoardViewer().redoLastManMove();
            // gray it if there are now no more moves to undo
            toolBar_.getRedoButton().setEnabled(boardViewer_.canRedoMove());
            toolBar_.getUndoButton().setEnabled(true);
        }
        if ( source == toolBar_.getOptionsButton() ) {
            //optionsDialog_.setLocationRelativeTo( this );
            boolean canceled = optionsDialog_.showDialog();
            GameContext.log(2, "options selected  canceled=" + canceled );
            if ( !canceled ) { // start a game with the newly defined options
                GameContext.log(0, "options selected not canceled  show game tree=" + get2PlayerController().getTwoPlayerOptions().getShowGameTree() );
                if ( get2PlayerController().getTwoPlayerOptions().getShowGameTree() ) {
                    showGameTreeDialog();
                }
                else {
                    treeDialog_.setVisible(false);
                }
            }
        }
        else if ( source == toolBar_.getHelpButton() )  {
            showHelpDialog();
        }
    }

    private AbstractTwoPlayerBoardViewer getBoardViewer() {
        return (AbstractTwoPlayerBoardViewer)boardViewer_;
    }

    /**
     * We don't show the progress bar is if both players are human.
     */
    public void startGame()  {
         progressBar_.setVisible(!get2PlayerController().getPlayers().allPlayersHuman());
         getBoardViewer().startNewGame();
    }

}
