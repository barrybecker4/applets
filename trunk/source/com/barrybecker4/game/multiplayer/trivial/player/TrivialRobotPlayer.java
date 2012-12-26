/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.multiplayer.trivial.player;

import com.barrybecker4.game.common.GameContext;
import com.barrybecker4.game.multiplayer.trivial.TrivialAction;
import com.barrybecker4.game.multiplayer.trivial.TrivialController;

import java.awt.*;

import static com.barrybecker4.game.multiplayer.trivial.TrivialAction.Name.KEEP_HIDDEN;
import static com.barrybecker4.game.multiplayer.trivial.TrivialAction.Name.REVEAL;

/**
 * Represents a Robot player.
 *
 * @author Barry Becker
 */
public class TrivialRobotPlayer extends TrivialPlayer {

    private static final long serialVersionUID = 1;


    public TrivialRobotPlayer(String name, Color color ) {
        super(name,  color, false);
        GameContext.log(0, "created a robot with name="+name);
    }

    /**
     * @return a string describing the type of robot.
     */
    public String getType() {
        return "Robot";
    }

    /**
     * Only reveal action with  a certain probability.
     * @param pc
     * @return the action
     */
    public TrivialAction getAction(TrivialController pc) {
        // 60/40 chance to reveal value
        TrivialAction.Name opt = (Math.random() > 0.4) ? REVEAL : KEEP_HIDDEN;
        return new TrivialAction(this.getName(), opt);
    }

}



