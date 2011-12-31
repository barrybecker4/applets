/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.go.ui.dialogs;

import com.becker.game.common.GameContext;
import com.becker.game.common.GameViewable;
import com.becker.game.twoplayer.common.ui.dialogs.PlayerAssignmentPanel;
import com.becker.game.twoplayer.common.ui.dialogs.TwoPlayerNewGameDialog;
import com.becker.game.twoplayer.go.GoController;
import com.becker.game.twoplayer.go.board.GoBoard;
import com.becker.ui.components.NumberInput;

import javax.swing.*;
import java.awt.event.ActionListener;

public final class GoNewGameDialog extends TwoPlayerNewGameDialog
                                implements ActionListener {

    /** must not initialize to null */
    private NumberInput handicapField_;


    /** constructor */
    public GoNewGameDialog( JFrame parent, GameViewable viewer ) {
        super( parent, viewer );
    }

    @Override
    protected JPanel createCustomBoardConfigPanel() {
        JPanel p = new JPanel();
        p.setLayout( new BoxLayout( p, BoxLayout.Y_AXIS ) );

        handicapField_ =
           new NumberInput(GameContext.getLabel("HANDICAP_LABEL"), ((GoBoard) board_).getHandicap());

        p.add( handicapField_ );
        assert ( handicapField_!=null );
        return p;
    }

    @Override
    protected PlayerAssignmentPanel createPlayerAssignmentPanel() {
        return new GoPlayerAssignmentPanel(get2PlayerController(), parent_);
    }

    @Override
    protected void ok() {
        GoController gcontroller = (GoController) controller_;

        assert ( handicapField_!=null );
        gcontroller.setHandicap( handicapField_.getIntValue() );

        GameContext.log( 2, "GoOptionsDlg: the handicap is:" + handicapField_.getIntValue());
        super.ok();
    }

}

