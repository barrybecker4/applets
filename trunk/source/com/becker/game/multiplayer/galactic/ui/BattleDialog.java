package com.becker.game.multiplayer.galactic.ui;

import com.becker.game.common.*;
import com.becker.game.multiplayer.galactic.*;
import com.becker.game.multiplayer.galactic.player.*;
import com.becker.ui.*;
import com.becker.sound.MusicMaker;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Iterator;

/**
 * Play a battle sequence that is stored in a GalacticTurn.
 *
 * @author Barry Becker
 */
final class BattleDialog extends OptionsDialog
                         implements ActionListener

{
    // smaller number means faster battle sequence
    private static final int BATTLE_SPEED = 2000;
    private static final int WIDTH = 300;
    private static final int LEFT_MARGIN = 7;
    // where the test and bar start. to the left of this is the icon.
    private static final int LEFT_IMAGE_MARGIN = 60;
    private static final int ATTACKER_Y = 20;
    private static final int DEFENDER_Y = 70;

    private static final int IMAGE_WIDTH = LEFT_IMAGE_MARGIN - 2 * LEFT_MARGIN;
    private static final int IMAGE_HEIGHT = DEFENDER_Y - ATTACKER_Y - 10;

    private static final int BAR_THICKNESS = 20;

    private final JEditorPane descriptionLabel_ = new JEditorPane();
    private final BattleCanvas canvas_ = new BattleCanvas();

    private final GradientButton startButton_ = new GradientButton();
    private final GradientButton closeButton_ = new GradientButton();

    private final JLabel infoLabel_ = new JLabel();

    private BattleSimulation battle_;
    private GalaxyViewer viewer_;


    /**
     * constructor - create the Battle dialog.
     * @param parent frame to display relative to
     * @param battle the simulation
     * @param viewer send in the viewer so we can give feedbak about the battle while it is occurring
     */
    BattleDialog( Frame parent, BattleSimulation battle, GalaxyViewer viewer )
    {
        super( parent );
        this.setResizable(false);
        //if (!GUIUtil.isStandAlone())
        //    this.setAlwaysOnTop(true);   // causes access control exception in applet
        battle_ = battle;
        viewer_ = viewer;
        initUI();
        this.setModal(true);
    }


    public String getTitle()
    {
        return "Battle Sequence";
    }

    /**
     * ui initialization of the tree control.
     */
    protected void initUI()
    {
        mainPanel_.setLayout( new BorderLayout() );


        JPanel viewerPanel = new JPanel();
        viewerPanel.setLayout(new BorderLayout());
        viewerPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        infoLabel_.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
                             BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        infoLabel_.setVerticalAlignment(JLabel.TOP);
        infoLabel_.setPreferredSize( new Dimension( WIDTH, 200 ) );
        infoLabel_.setBackground(new Color(180, 100, 255));

        viewerPanel.add( infoLabel_, BorderLayout.SOUTH);

        JPanel buttonsPanel = createButtonsPanel();

        Planet defendingPlanet =  battle_.getPlanet();
        String text = "There is a battle at "+defendingPlanet.getName()+".\n";

        descriptionLabel_.setEditable(false);
        //descriptionLabel_.setLineWrap(true);
        descriptionLabel_.setContentType("text/html");
        descriptionLabel_.setText(text);

        canvas_.setPreferredSize(new Dimension(WIDTH, 120));
        JPanel canvasPanel = new JPanel();
        canvasPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5,5,5,5),
                              BorderFactory.createLineBorder(Color.black, 1)));
        canvasPanel.add(canvas_);

        mainPanel_.add(descriptionLabel_, BorderLayout.NORTH);
        mainPanel_.add(canvasPanel, BorderLayout.CENTER);
        mainPanel_.add( buttonsPanel, BorderLayout.SOUTH );
        getContentPane().add( mainPanel_ );

        viewer_.showPlanetUnderAttack(battle_.getPlanet(), true);

        int numAttackShips = battle_.getOrder().getFleetSize();
        int numDefendShips = battle_.getPlanet().getNumShips();
        this.refresh(numAttackShips, numDefendShips);
        pack();
    }


    protected JPanel createButtonsPanel()
    {
        JPanel buttonsPanel_=new JPanel( new BorderLayout());
        buttonsPanel_.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        initBottomButton( startButton_, "Fight!", "Begin the battle sequence");
        initBottomButton( closeButton_, GameContext.getLabel("CLOSE"), "Close dialog" );
        //initBottomButton( cancelButton_, GameContext.getLabel("CANCEL"), GameContext.getLabel("CANCEL") );

        buttonsPanel_.add( startButton_, BorderLayout.CENTER);
        buttonsPanel_.add( closeButton_, BorderLayout.EAST );
        closeButton_.setEnabled(false);

        return buttonsPanel_;
    }


    /**
     * called when one of the buttons at the bottom has been pressed.
     * @param e
     */
    public void actionPerformed( ActionEvent e )
    {

        Object source = e.getSource();
        if (source == closeButton_) {
            this.setVisible(false);

        }
        else if (source == startButton_) {

            startButton_.setEnabled(false);
            closeButton_.setEnabled(true);
            this.invalidate();
            this.paint(this.getGraphics());

            Thread battle =  new Thread(canvas_);
            SwingUtilities.invokeLater(battle);

            //doAnimation();
        }
    }


    /**
     * refresh the game tree.
     */
    protected void refresh(int attackers, int defenders)
    {
        canvas_.setFleetSizes(attackers, defenders);
    }



    /**
     * Canvas for showing the animation ----------------------------------
     */
    private class BattleCanvas extends Canvas implements Runnable
    {
        private int attackers_;
        private int defenders_;

        private BattleCanvas()
        {
            //this.setDoubleBuffered(false);
        }

        public void setFleetSizes(int attackers, int defenders)
        {
            attackers_ = attackers;
            defenders_ = defenders;
            this.paint(this.getGraphics());
        }

        public void run()
        {
             Planet destPlanet = battle_.getPlanet();
             int numAttackShips = battle_.getOrder().getFleetSize();
             int numDefendShips = destPlanet.getNumShips();
             //String defender = (destPlanet.getOwner()==null)? "Neutral" : destPlanet.getOwner().getName();

             // play back the move sequence
             List sequence = battle_.getHitSequence();
             if (sequence.isEmpty()) {
                 // reinforced!
                 GameContext.getMusicMaker().playNote( MusicMaker.APPLAUSE, 45, 0, 200, 1000 );
                 GameContext.getMusicMaker().playNote(70, 50, 900);
                 GameContext.getMusicMaker().playNote(90, 40, 1000);
                 descriptionLabel_.setText("Planet "+destPlanet.getName()+" has been reinforced.");
             }
             else {
                 boolean useSound = GameContext.getUseSound();
                 Iterator it = sequence.iterator();
                 if (useSound)
                     GameContext.getMusicMaker().playNote( MusicMaker.GUNSHOT, 45, 0, 200, 1000 );

                 while (it.hasNext()) {
                     GalacticPlayer p = (GalacticPlayer)it.next();
                     int total = numAttackShips + numDefendShips;
                     int time = 1 + BATTLE_SPEED / (1+total);
                     if (p == battle_.getOrder().getOwner()) {
                         if (useSound)
                             GameContext.getMusicMaker().playNote(100, time, 800);
                         numAttackShips--;
                     }
                     else {
                         if (useSound)
                             GameContext.getMusicMaker().playNote(80, time, 800);
                         numDefendShips--;
                     }

                     refresh(numAttackShips, numDefendShips);

                     try {
                         Thread.sleep(time);
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
                 assert(numAttackShips == 0 || numDefendShips == 0):
                         "numAttackShips="+numAttackShips+" numDefendShips="+numDefendShips;
                 String winMessage;
                 if (numAttackShips==0)
                     winMessage = "Planet "+destPlanet.getName()+" has successfully defended itself.";
                 else
                     winMessage = battle_.getOrder().getOwner().getName()+ " has conquered planet "+destPlanet.getName();

                 descriptionLabel_.setText( "<html>"+ descriptionLabel_.getText()+ "<b>"+ winMessage +"/b></html>");
             }

             viewer_.showPlanetUnderAttack(battle_.getPlanet(), false);  // battle is done
             //closeButton_.setEnabled(true);

             canvas_.repaint();
         }


        public void paint(Graphics g) {

            if (g == null)
                return;

            Graphics2D g2 = (Graphics2D) canvas_.getGraphics();

            // clear background
            g2.setColor( Color.white );
            g2.fillRect( 0, 0, this.getWidth(), this.getHeight() );

            GalacticPlayer attacker = battle_.getOrder().getOwner();
            String title = "Attacker : " + attacker.getName();
            drawPlayerRep(g2, attacker, attacker.getColor(), attackers_, title,
                          LEFT_MARGIN, LEFT_IMAGE_MARGIN, ATTACKER_Y);

            GalacticPlayer defender = battle_.getPlanet().getOwner(); // null if nuetral
            Color defenderColor = (defender == null) ? Planet.NEUTRAL_COLOR : defender.getColor();
            String planetName = battle_.getPlanet().getName() + "";
            title = "Defender :"+ ( defender== null ? planetName : defender.getName() + " at "+ planetName);
            drawPlayerRep(g2, defender, defenderColor, defenders_, title,
                          LEFT_MARGIN, LEFT_IMAGE_MARGIN, DEFENDER_Y);
        }

        private void drawPlayerRep(Graphics2D g2, GalacticPlayer player, Color color, int numShips, String title,
                                   int margin, int imageMargin, int yPos) {

            if (player != null)
                g2.drawImage(player.getIcon().getImage(), margin, yPos - 10, IMAGE_WIDTH, IMAGE_HEIGHT, this);

            g2.setColor(color);
            g2.fillRect(imageMargin, yPos + 5, numShips, BAR_THICKNESS);

            g2.setColor(color.darker());
            g2.drawString(title, LEFT_IMAGE_MARGIN, yPos);
            g2.drawString(Integer.toString(numShips), LEFT_IMAGE_MARGIN  + numShips + 15, yPos+22);
            if (numShips > 0)
                g2.drawRect(LEFT_IMAGE_MARGIN, yPos + 5, numShips, BAR_THICKNESS);

        }

    }

}

