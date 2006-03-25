package com.becker.game.twoplayer.common.ui;

import com.becker.game.common.*;
import com.becker.game.common.ui.*;
import com.becker.game.twoplayer.common.*;
import com.becker.optimization.*;
import com.becker.ui.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Use this modal dialog to initialize the required game parameters that
 * are needed in order to start play in a 2 player game.
 *
 * @author Barry Becker
 */
public class TwoPlayerNewGameDialog extends NewGameDialog implements ActionListener
{

    private JPanel optimizationPanel_ = null;

    // radio buttons for selecting 1st and second players
    private JRadioButton human1Button_;
    private JRadioButton computer1Button_;
    private JRadioButton human2Button_;
    private JRadioButton computer2Button_;

    private JCheckBox optimizationCheckbox_;

    private final GameWeights gameWeights_;

    private GradientButton editWts1Button_;
    private GradientButton editWts2Button_;


    // constructor
    public TwoPlayerNewGameDialog( JFrame parent, ViewerCallbackInterface viewer )
    {
        super( parent, viewer );
        gameWeights_ = get2PlayerController().getComputerWeights();  // gets the actual weights

        optimizationPanel_ = createOptimizationPanel();
        initUI();
    }

    private TwoPlayerController get2PlayerController()
    {
        return (TwoPlayerController)controller_;
    }

    protected void buildMainOptionsPanel(JPanel mainOptionsPanel)
    {
        mainOptionsPanel.add( playerPanel_ );
        mainOptionsPanel.add( optimizationPanel_ );
        mainOptionsPanel.add( boardParamPanel_ );
        if ( customPanel_ != null )
            mainOptionsPanel.add( customPanel_ );
    }


    private JPanel createOptimizationPanel()
    {
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


    protected JPanel createPlayerPanel()
    {
        TwoPlayerController c = get2PlayerController();


        JPanel outerPanel = new JPanel(new BorderLayout());

        JPanel p = new JPanel();
        p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
        final String human = GameContext.getLabel("HUMAN");
        final String computer = GameContext.getLabel("COMPUTER");
        final String editWeights = GameContext.getLabel("EDIT_WEIGHTS");
        p.setBorder(
                BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(),
                                                  GameContext.getLabel("PLAYER_ASSIGNMENT") ) );

        Player p1 = c.getPlayer1();
        Player p2 = c.getPlayer2();

        human1Button_ = new JRadioButton( human, p1.isHuman() );
        computer1Button_ = new JRadioButton( computer, !p2.isHuman() );
        editWts1Button_ = new GradientButton( editWeights );
        editWts1Button_.setEnabled( !p1.isHuman() );
        JPanel firstP =
                createPlayerEntry( getPlayer1Label(), human1Button_, computer1Button_, editWts1Button_ );

        human2Button_ = new JRadioButton( human, p2.isHuman());
        computer2Button_ = new JRadioButton( computer, !p2.isHuman() );
        editWts2Button_ = new GradientButton( editWeights );
        editWts2Button_.setEnabled( !p2.isHuman() );
        JPanel secondP =
                createPlayerEntry( getPlayer2Label(), human2Button_, computer2Button_, editWts2Button_ );

        firstP.setAlignmentX( Component.LEFT_ALIGNMENT );
        secondP.setAlignmentX( Component.LEFT_ALIGNMENT );
        p.add( firstP );
        p.add( secondP );

        outerPanel.add(p, BorderLayout.CENTER );
        outerPanel.add(new Panel(), BorderLayout.WEST);

        return outerPanel;
    }

    protected String getPlayer1Label()
    {
        return GameContext.getLabel("FIRST_PLAYER_COLON");
    }

    protected String getPlayer2Label()
    {
        return GameContext.getLabel("SECOND_PLAYER_COLON");
    }

    private JPanel createPlayerEntry( String message,
                                      JRadioButton humanButton,
                                      JRadioButton computerButton,
                                      GradientButton editWtsButton )
    {
        JPanel p = new JPanel();
        p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );

        JLabel label = new JLabel( message );
        label.setMinimumSize(new Dimension(140, 18));
        p.add( label );
        humanButton.addActionListener( this );
        computerButton.addActionListener( this );
        editWtsButton.addActionListener( this );

        ButtonGroup group = new ButtonGroup();
        group.add( humanButton );
        group.add( computerButton );
        computerButton.addActionListener( this );
        humanButton.addActionListener( this );

        humanButton.setAlignmentX( Component.LEFT_ALIGNMENT );
        computerButton.setAlignmentX( Component.LEFT_ALIGNMENT );
        editWtsButton.setAlignmentX( Component.RIGHT_ALIGNMENT );

        p.add( humanButton );
        p.add( computerButton );
        p.add( new JPanel()); // filler
        p.add( editWtsButton );
        return p;
    }


    // return true if canceled
    private boolean showEditWeightsDialog( ParameterArray weights )
    {
        EditWeightsDialog editWtsDlg = new EditWeightsDialog( parent_, weights, gameWeights_ );
        Dimension dlgSize = editWtsDlg.getPreferredSize();
        Dimension frmSize = getSize();
        Point pt = getLocation();
        editWtsDlg.setLocation( (frmSize.width - dlgSize.width) >> 1
                + pt.x, ((frmSize.height - dlgSize.height) >> 1) + pt.y );
        editWtsDlg.setModal( true );
        editWtsDlg.setVisible(true);
        return false; // how to cancel?
    }

    protected void ok()
    {
        TwoPlayerController c = get2PlayerController();
        if (optimizationCheckbox_.isSelected())
        {
            c.getPlayer1().setHuman(false);
            c.getPlayer2().setHuman(false);
            c.getOptions().setAutoOptimize(true);
        }
        else {
            c.getPlayer1().setHuman( human1Button_.isSelected() );
            c.getPlayer2().setHuman( human2Button_.isSelected() );
        }
        board_.setSize( rowSizeField_.getIntValue(), colSizeField_.getIntValue() );

        //restore the saved file if one was specified
        String fileToOpen = openFileField_.getText();
        if ( fileToOpen != null && fileToOpen.length() > 1 ) {
            get2PlayerController().restoreFromFile( fileToOpen );
            canceled_ = true;
        }
        else
            canceled_ = false;
        setVisible( false );
    }

    public void actionPerformed( ActionEvent e )
    {
        Object source = e.getSource();

        if ( source == startButton_ ) {
            ok();
        }
        else if ( source == cancelButton_ ) {
            cancel();
        }
        else if ( source == computer1Button_ ) {
            editWts1Button_.setEnabled( true );
        }
        else if ( source == computer2Button_ ) {
            editWts2Button_.setEnabled( true );
        }
        else if ( source == human1Button_ ) {
            editWts1Button_.setEnabled( false );
        }
        else if ( source == human2Button_ ) {
            editWts2Button_.setEnabled( false );
        }
        else if ( source == editWts1Button_ ) {
            showEditWeightsDialog( gameWeights_.getPlayer1Weights() );
        }
        else if ( source == editWts2Button_ ) {
            showEditWeightsDialog( gameWeights_.getPlayer2Weights() );
        }
        else if ( source == openFileButton_ ) {
            openFile();
        }
        else if (source == optimizationCheckbox_) {
            boolean checked = optimizationCheckbox_.isSelected();
            if (checked)  {
                // open a dlg to get a location for the optimization log
                // if they cancel this dlg then we leave the checkbox uchecked
                 if (GUIUtil.isStandAlone())  {
                   JOptionPane.showMessageDialog(this, GameContext.getLabel("CANT_RUN_OPT_WHEN_STANDALONE"));
                }
                else {
                    JFileChooser chooser = GUIUtil.getFileChooser();
                    chooser.setCurrentDirectory( new File( GameContext.getHomeDir() ) );
                    chooser.setFileFilter(new TextFileFilter());
                    int state = chooser.showOpenDialog( this );
                    File file = chooser.getSelectedFile();
                    if ( file == null || state != JFileChooser.APPROVE_OPTION )  {
                        optimizationCheckbox_.setSelected(false);
                        return;
                    }
                    else
                        get2PlayerController().getOptions().setAutoOptimizeFile( file.getAbsolutePath() );
                 }
            }

            playerPanel_.setEnabled(checked);
            computer1Button_.setSelected(checked);
            computer2Button_.setSelected(checked);
            editWts1Button_.setEnabled( !checked );
            editWts2Button_.setEnabled( !checked );
            human1Button_.setEnabled(!checked);
            computer1Button_.setEnabled(!checked);
            human2Button_.setEnabled(!checked);
            computer2Button_.setEnabled(!checked);
        }
    }
}