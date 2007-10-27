package com.becker.game.common.ui;

import com.becker.game.common.*;
import com.becker.game.common.online.ui.*;
import com.becker.ui.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

/**
 * Use this modal dialog to let the user to configure a new local game.
 * The have a choice of a new player vs player game or combinations of player vs computer or all computer.
 *
 * @author Barry Becker
 */
public abstract class NewGameDialog extends OptionsDialog implements ChangeListener
{
    /**
     * the options get set directly on the game controller that is passed in.
     */
    protected GameController controller_;

    // contains the two tabs : options for creating a new game, or loading a saved game
    protected final JTabbedPane tabbedPanel_ = new JTabbedPane();

    protected JPanel playLocalPanel_;
    protected JPanel loadGamePanel_;
    protected OnlineGameManagerPanel playOnlinePanel_;

    protected GradientButton openFileButton_;

    protected NumberInput rowSizeField_;
    protected NumberInput colSizeField_;
    protected JTextField openFileField_;

    protected final GradientButton startButton_ = new GradientButton();

    // the options get set directly on the game controller and viewer that are passed in
    protected final Board board_;
    protected final ViewerCallbackInterface viewer_;


    // constructor
    public NewGameDialog( JFrame parent, ViewerCallbackInterface viewer)
    {
        super( parent );
        controller_ = viewer.getController();
        board_ = controller_.getBoard();
        viewer_ = viewer;
    }

    protected void initUI()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout( new BorderLayout() );

        playLocalPanel_ = createPlayLocalPanel();
        loadGamePanel_ = createLoadGamePanel();

        JPanel buttonsPanel = createButtonsPanel();

        // add the tabs
        tabbedPanel_.add( GameContext.getLabel("NEW_GAME"), playLocalPanel_ );
        tabbedPanel_.setToolTipTextAt( 0, GameContext.getLabel("NEW_GAME_TIP") );
        tabbedPanel_.add( GameContext.getLabel("LOAD_GAME"), loadGamePanel_ );
        tabbedPanel_.setToolTipTextAt( 1, GameContext.getLabel("LOAD_GAME_TIP") );
        tabbedPanel_.addChangeListener(this);

        mainPanel.add( tabbedPanel_, BorderLayout.CENTER );
        mainPanel.add( buttonsPanel, BorderLayout.SOUTH );

        this.getContentPane().add( mainPanel );
        this.pack();
    }

    protected OnlineGameManagerPanel createPlayOnlinePanel() {
        return null; // nothing if no online play supported
        //return new OnlineGameManagerPanel(viewer_);
    }

    protected JPanel createPlayLocalPanel()
    {
        JPanel playLocalPanel = new JPanel();
        playLocalPanel.setLayout( new BoxLayout( playLocalPanel, BoxLayout.Y_AXIS ) );
        JPanel playerPanel = createPlayerPanel();
        JPanel boardParamPanel = createBoardParamPanel();
        JPanel customPanel = createCustomPanel();

        if (playerPanel != null)
            playLocalPanel.add( playerPanel );
        if (boardParamPanel != null)
            playLocalPanel.add( boardParamPanel );
        if (customPanel != null )
            playLocalPanel.add( customPanel );

        return playLocalPanel;
    }

    protected abstract JPanel createPlayerPanel();


    protected JPanel createButtonsPanel()
    {
        JPanel buttonsPanel = new JPanel( new FlowLayout() );

        initBottomButton( startButton_, GameContext.getLabel("START_GAME"), GameContext.getLabel("START_GAME_TIP") );
        initBottomButton( cancelButton_, GameContext.getLabel("CANCEL"), GameContext.getLabel("NGD_CANCEL_TIP") );

        buttonsPanel.add( startButton_ );
        buttonsPanel.add( cancelButton_ );

        return buttonsPanel;
    }

    /**
     * Subclasses use this to create their own custom options
     * Default is to have no custom panel.
     */
    protected JPanel createCustomPanel()
    {
        return null;
    }

    /**
     * Subclasses use this to create their own custom board configuration options
     * Default is to have no custom panel.
     */
    protected JPanel createCustomBoardConfigPanel()
    {
        return null;
    }

    public String getTitle()
    {
        return GameContext.getLabel("NEW_GAME_DLG_TITLE");
    }


    protected JPanel createBoardParamPanel()
    {
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BorderLayout());
        JPanel p = new JPanel();
        p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );
        p.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Board Configuration" ) );
        JLabel label = new JLabel( GameContext.getLabel("BOARD_SIZE") + COLON );
        label.setAlignmentX( Component.LEFT_ALIGNMENT );
        p.add( label );

        if (board_!=null) {
            rowSizeField_ = new NumberInput(GameContext.getLabel("NUMBER_OF_ROWS"), board_.getNumRows());
            colSizeField_ = new NumberInput( GameContext.getLabel("NUMBER_OF_COLS"), board_.getNumCols());

            rowSizeField_.setAlignmentX( Component.LEFT_ALIGNMENT );
            colSizeField_.setAlignmentX( Component.LEFT_ALIGNMENT );
            p.add( rowSizeField_ );
            p.add( colSizeField_ );
        }

        // add a custom section if desired (override createCustomBoardConfigPanel in derived class)
        JPanel customConfigPanel = createCustomBoardConfigPanel();
        if ( customConfigPanel != null )
            p.add( customConfigPanel );

        outerPanel.add(p, BorderLayout.CENTER);
        outerPanel.add(new JPanel(), BorderLayout.EAST);
        return outerPanel;
    }


    protected JPanel createLoadGamePanel()
    {
        JPanel loadGamePanel = new JPanel();
        loadGamePanel.setLayout( new BoxLayout( loadGamePanel, BoxLayout.Y_AXIS ) );

        JPanel p = new JPanel();
        p.setLayout( new BoxLayout( p, BoxLayout.X_AXIS ) );
        p.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(),
                     GameContext.getLabel("SPECIFY_SGF") ) );
        p.setMaximumSize( new Dimension( 400, ROW_HEIGHT + 30 ) );
        //p.setAlignmentX(Component.LEFT_ALIGNMENT);

        openFileField_ = new JTextField( "" );
        openFileField_.setAlignmentX( Component.LEFT_ALIGNMENT );
        openFileButton_ = new GradientButton( "..." );
        openFileButton_.setPreferredSize( new Dimension( 20, ROW_HEIGHT ) );
        openFileButton_.addActionListener( this );
        openFileButton_.setAlignmentX( Component.LEFT_ALIGNMENT );

        JLabel label = new JLabel(GameContext.getLabel("FILE_NAME") + COLON);
        label.setAlignmentX( Component.LEFT_ALIGNMENT );
        p.add( label );

        p.add( openFileField_ );
        p.add( openFileButton_ );

        loadGamePanel.add( p);

        return loadGamePanel;
    }

    protected void ok()
    {
        if (board_ != null && rowSizeField_!= null) {
            board_.setSize( rowSizeField_.getIntValue(), colSizeField_.getIntValue() );
        }

        //restore the saved file if one was specified
        String fileToOpen = openFileField_.getText();
        if ( fileToOpen != null && fileToOpen.length() > 1 ) {
            controller_.restoreFromFile( fileToOpen );
            canceled_ = true;
        }
        else
            canceled_ = false;
        this.setVisible( false );
    }

    protected void openFile()
    {
       if (GUIUtil.isStandAlone())  {
             JOptionPane.showMessageDialog(this, GameContext.getLabel("CANT_OPEN_WHEN_STANDALONE"));
       } else {
            JFileChooser chooser = GUIUtil.getFileChooser();
            chooser.setCurrentDirectory( new File( GameContext.getHomeDir()) );
            int state = chooser.showOpenDialog( null );
            File file = chooser.getSelectedFile();
            if ( file != null && state == JFileChooser.APPROVE_OPTION )
                openFileField_.setText( file.getAbsolutePath() );
        }
    }

    public boolean showDialog() {

        boolean serverAvailable =  controller_.isOnlinePlayAvailable();
        if (serverAvailable) {
             if (playOnlinePanel_ == null) {
                 playOnlinePanel_ = createPlayOnlinePanel();
                 tabbedPanel_.add(playOnlinePanel_, 0);
                 tabbedPanel_.setTitleAt(0, "Play Online");
                 tabbedPanel_.setSelectedIndex(0);
                 pack();
             }
             tabbedPanel_.setEnabledAt(0, true);
        }
        else {
            if (playOnlinePanel_ != null) {
                tabbedPanel_.setEnabledAt(0, false);
            }
        }
        return super.showDialog();
    }

    /**
     * Called when one of the buttons at the bottom pressed
     */
    public void actionPerformed( ActionEvent e )
    {
        Object source = e.getSource();

        if ( source == startButton_ ) {
            ok();
        }
        else if ( source == cancelButton_ ) {
            cancel();
        }
        else if ( source == openFileButton_ ) {
            openFile();
        }
    }
    
    /**
     * cancel button pressed
     */
    protected void cancel()
    {
        // You are only allowed to participate in only games when the dialog is open.
        if (playOnlinePanel_ != null) {
            playOnlinePanel_.closing();
        }
        super.cancel();
    }

    /**
     * Called when the selected tab changes,
     * Or in the case of online play when the player has joined a table that is now ready to play.
     * I that case the dialog will close and play will begin.
     * @param e
     */
    public void stateChanged( ChangeEvent e) {
        if (e.getSource() == tabbedPanel_) {
            if (tabbedPanel_.getSelectedComponent() == playOnlinePanel_) {
                startButton_.setVisible(false);
            }
            else {
                startButton_.setVisible(true);
            }
        }
        else if (e.getSource() == playOnlinePanel_) {
            this.setVisible(false);
        }
    }


    /**
     * If the window gets closed, then the player has stood up from his table if online.
     */
    protected void processWindowEvent( WindowEvent e )
    {
        if ( e.getID() == WindowEvent.WINDOW_CLOSING ) {

            System.out.println("Window closing!");
            if (controller_.isOnlinePlayAvailable()) {
                System.out.println("Standing up from table.");
                playOnlinePanel_.closing();
            }
        }
    }   

}