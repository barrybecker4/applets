// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.becker.game.twoplayer.comparison.ui.grid;

import com.becker.common.util.FileUtil;
import com.becker.game.common.GameContext;
import com.becker.game.common.plugin.PluginManager;
import com.becker.game.common.ui.SgfFileFilter;
import com.becker.game.common.ui.menu.GameMenuListener;
import com.becker.game.common.ui.panel.IGamePanel;
import com.becker.game.twoplayer.common.ui.TwoPlayerPanel;
import com.becker.game.twoplayer.comparison.execution.PerformanceRunner;
import com.becker.game.twoplayer.comparison.execution.PerformanceRunnerListener;
import com.becker.game.twoplayer.comparison.model.ResultsModel;
import com.becker.game.twoplayer.comparison.model.SearchOptionsConfigList;
import com.becker.ui.components.GradientButton;
import com.becker.ui.file.DirFileFilter;
import com.becker.ui.file.FileChooserUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


/**
 * Show grid of game trials with run button at top.
 *
 * @author Barry Becker
 */
public final class ComparisonGridPanel
           extends JPanel
        implements ActionListener, GameMenuListener, PerformanceRunnerListener {

    private static final String DEFAULT_SAVE_LOCATION = FileUtil.PROJECT_HOME + "temp/comparisonResults/temp";
    private GradientButton runButton_;
    private GradientButton resultsLocationButton_;
    private ComparisonGrid grid_;
    private JScrollPane scrollPane;
    private SearchOptionsConfigList optionsList;
    private String gameName;
    private String resultsSaveLocation = DEFAULT_SAVE_LOCATION;

    /**
     * constructor - create the tree dialog.
     */
    public ComparisonGridPanel() {

        grid_ = ComparisonGrid.createInstance(new SearchOptionsConfigList());
        init();
    }
    
    public void setOptionsList(SearchOptionsConfigList optionsList) {

        grid_ = ComparisonGrid.createInstance(optionsList);
        this.optionsList = optionsList;
        runButton_.setEnabled(optionsList.size() > 0);
        scrollPane.setViewportView(grid_.getTable());
    }

    private void init() {

        this.setLayout(new BorderLayout());

        add(createTopControls(), BorderLayout.NORTH);

        scrollPane = createGridPane();
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createTopControls()    {
        JPanel topControlsPanel = new JPanel(new BorderLayout());
        runButton_ = new GradientButton("Run Comparisons");
        runButton_.addActionListener(this);
        runButton_.setEnabled(false);
        
        topControlsPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        topControlsPanel.add(runButton_, BorderLayout.WEST);
        
        JLabel saveToLabel = new JLabel("and save results to : " + DEFAULT_SAVE_LOCATION);
        resultsLocationButton_ = new GradientButton("...");
        resultsLocationButton_.addActionListener(this);

        JPanel saveLocationPanel = new JPanel();
        saveLocationPanel.add(saveToLabel);
        saveLocationPanel.add(resultsLocationButton_);
        topControlsPanel.add(saveLocationPanel, BorderLayout.CENTER);
                
        return topControlsPanel;
    }
    
    private JScrollPane createGridPane() {
        JScrollPane scrollPane = new JScrollPane(grid_.getTable());
        scrollPane.setPreferredSize(new Dimension(360, 120));
        scrollPane.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5),
                scrollPane.getBorder()));
        return scrollPane;
    }
        
    public void actionPerformed(ActionEvent e) {

        Object source = e.getSource();

        if (source == runButton_) {
            final IGamePanel gamePanel = createGamePanel(gameName);

            PerformanceRunner runner =
                new PerformanceRunner((TwoPlayerPanel)gamePanel, optionsList, this);


            // when done performanceRunsDone will be called.
            runner.doComparisonRuns(resultsSaveLocation);
        }    
        else if (source == resultsLocationButton_) {
            JFileChooser chooser = FileChooserUtil.getFileChooser(new DirFileFilter());

            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            int state = chooser.showDialog(null, "Select this location");
            File file = chooser.getSelectedFile();
            resultsSaveLocation = file.getAbsolutePath();
        }
    }

    public void gameChanged(String gameName) {

        this.gameName = gameName;
    }

    public IGamePanel createGamePanel(String gameName) {

        // this will load the resources for the specified game.
        GameContext.loadGameResources(gameName);
        grid_.setGameName(gameName);
        repaint();

        return PluginManager.getInstance().getPlugin(gameName).getPanelInstance();
    }

    @Override
    public void paint(Graphics g) {

        grid_.updateRowHeight(scrollPane.getHeight());
        super.paint(g);
    }

    public void performanceRunsDone(ResultsModel model) {
        grid_.updateWithResults(model);
    }
}

