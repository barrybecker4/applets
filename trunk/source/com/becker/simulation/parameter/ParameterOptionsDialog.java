package com.becker.simulation.parameter;

import com.becker.simulation.common.*;

import javax.swing.*;
import java.awt.*;

/**
 * @author Barry Becker Date: 2007
 */
public class ParameterOptionsDialog extends SimulatorOptionsDialog {

    /** type of distribution function to test.   */
    private JComboBox parameterChoiceField_;
    private JCheckBox showRedistribution_;

    /**
     * constructor
     */ 
    public ParameterOptionsDialog( JFrame parent, Simulator simulator )
    {
        super( parent, simulator );
        ParameterSimulator psim = (ParameterSimulator) getSimulator();
        showRedistribution_ .setSelected(psim.isShowRedistribution());
    }

    @Override
    public String getTitle()
    {
        return "Parameter Simulation Configuration";
    }

    @Override
    protected JPanel createCustomParamPanel()
    {
        JPanel paramPanel = new JPanel();
        paramPanel.setLayout(new BorderLayout());
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout( new BoxLayout(innerPanel, BoxLayout.Y_AXIS));

        parameterChoiceField_ = new JComboBox();
        parameterChoiceField_.setModel(
                new DefaultComboBoxModel(ParameterDistributionType.values()));

        
        showRedistribution_ = new JCheckBox("Show Redistribution");
        innerPanel.add( parameterChoiceField_ );
        innerPanel.add(showRedistribution_);
        JPanel fill = new JPanel();
        paramPanel.add(innerPanel, BorderLayout.NORTH);
        paramPanel.add(fill, BorderLayout.CENTER);

        return paramPanel;
    }

    @Override
    protected void ok()
    {
        super.ok();

        ParameterSimulator simulator = (ParameterSimulator) getSimulator();
        // set the common rendering and global physics options
        simulator.setParameter(
                ParameterDistributionType.values()[parameterChoiceField_.getSelectedIndex()].getParameter());
        simulator.setShowRedistribution(showRedistribution_.isSelected());
    }

}
