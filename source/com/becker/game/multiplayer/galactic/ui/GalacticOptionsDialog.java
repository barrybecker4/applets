package com.becker.game.multiplayer.galactic.ui;

import com.becker.ui.components.NumberInput;
import com.becker.game.common.*;
import com.becker.game.multiplayer.common.ui.*;
import com.becker.game.multiplayer.galactic.*;
import com.becker.ui.*;

import javax.swing.*;
import java.awt.event.*;

/**
 * Use this modal dialog to let the user choose from among the
 * different game options.
 *
 * @author Barry Becker
 */
class GalacticOptionsDialog extends MultiGameOptionsDialog
                            implements ActionListener, ItemListener
{

    // game params
    private NumberInput numPlanets_;
    private NumberInput planetProductionRate_;
    private NumberInput maxYearsToPlay_;
    private NumberInput initialFleetSize_;
    private JCheckBox neutralsBuild_;


    // constructor
    GalacticOptionsDialog( JFrame parent, GameController controller )
    {
        super( parent, controller);
    }


    /**
     * @return galactic game optiosn tab panel.
     */
    protected JComponent[] getControllerParamComponents()
    {
        GalacticOptions options = (GalacticOptions)controller_.getOptions();

        numPlanets_ =  new NumberInput( GameContext.getLabel("NUMBER_OF_PLANETS"), options.getNumPlanets(),
                                        GameContext.getLabel("NUMBER_OF_PLANETS_TIP"),
                                        Galaxy.MIN_NUM_PLANETS, Galaxy.MAX_NUM_PLANETS, true);
        planetProductionRate_ =
                new NumberInput(GameContext.getLabel("PLANETS_PRODUCTION_RATE"), options.getPlanetProductionRate(),
                                GameContext.getLabel("PLANETS_PRODUCTION_RATE_TIP"), 0, 10, true);
        initialFleetSize_ = new NumberInput(GameContext.getLabel("INITIAL_FLEET_SIZE"), options.getInitialFleetSize(),
                                            GameContext.getLabel("INITIAL_FLEET_SIZE_TIP"), 1, 100, true);
        neutralsBuild_ = new JCheckBox( GameContext.getLabel("SHOULD_NEUTRALS_BUILD"), options.doNeutralsBuild() );
        neutralsBuild_.setToolTipText(GameContext.getLabel("SHOULD_NEUTRALS_BUILD_TIP"));
        maxYearsToPlay_ =  new NumberInput(GameContext.getLabel("MAX_YEARS_TO_PLAY"), options.getMaxYearsToPlay(),
                                           GameContext.getLabel("MAX_YEARS_TO_PLAY_TIP"), 1, 100, true);

        initMultiControllerParamComponents(options);
        return new JComponent[] {
            numPlanets_, planetProductionRate_, initialFleetSize_, neutralsBuild_, maxYearsToPlay_,
            maxNumPlayers_, numRobotPlayers_
        };
    }


    public GameOptions getOptions() {
        return new GalacticOptions(maxNumPlayers_.getIntValue(),
                                   numRobotPlayers_.getIntValue(),
                                   numPlanets_.getIntValue(),
                                   planetProductionRate_.getIntValue(),
                                   maxYearsToPlay_.getIntValue(),
                                   initialFleetSize_.getIntValue(),
                                   neutralsBuild_.isSelected());
    }

}