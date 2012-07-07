// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.game.twoplayer.comparison.execution;

import com.becker.game.twoplayer.common.ui.TwoPlayerPanel;
import com.becker.game.twoplayer.comparison.model.SearchOptionsConfigList;
import com.becker.game.twoplayer.comparison.ui.execution.GameRunnerDialog;

import javax.swing.*;

/**
 * Run through the grid of game combinations and gather the performance results .
 * @author Barry Becker
 */
public class PerformanceRunner {

    private SearchOptionsConfigList optionsList;
    private TwoPlayerPanel gamePanel_;
    private PerformanceRunnerListener listener;

    public PerformanceRunner(TwoPlayerPanel gamePanel, SearchOptionsConfigList optionsList,
                             PerformanceRunnerListener listener)  {
        this.optionsList = optionsList;
        this.gamePanel_ = gamePanel;
        this.listener = listener;
    }

    /**
     * Run the NxN comparison and return the results.
     * @return model with all the results
     */
    public void doComparisonRuns() {

        GameRunnerDialog runnerDialog = new GameRunnerDialog(gamePanel_);
        runnerDialog.showDialog();
        gamePanel_.init(null);

        PerformanceWorker worker =
                new PerformanceWorker(gamePanel_.get2PlayerController(), optionsList, listener);
        SwingUtilities.invokeLater(worker);
    }

}
