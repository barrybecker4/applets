/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.becker.optimization.parameter.ui;

import com.becker.optimization.parameter.ParameterChangeListener;
import com.becker.optimization.parameter.Parameter;
import com.becker.ui.sliders.LabeledSlider;
import com.becker.ui.sliders.SliderChangeListener;
import com.becker.ui.sliders.LabeledSlider;
import java.awt.BorderLayout;

/**
 *
 * @author becker
 */
public class DoubleParameterWidget extends ParameterWidget implements SliderChangeListener {
   
    public DoubleParameterWidget(Parameter param, ParameterChangeListener listener) {
        super(param, listener);
    }

   /**
     * Create a ui widget appropriate for the parameter type.
     */
    protected void addChildren() {
        
             LabeledSlider slider = 
                     new LabeledSlider(parameter.getName(), parameter.getValue(), 
                                                   parameter.getMinValue(), parameter.getMaxValue());
             if (parameter.isIntegerOnly()) {
                 slider.setShowAsInteger(true);
             }
             slider.addChangeListener(this);             
             add(slider, BorderLayout.CENTER);
    }

    /**
     * @param slider the slider that changed.
     */
    public void sliderChanged(LabeledSlider slider) {    
         parameter.setValue(slider.getValue());
         doNotification();        
    }
}
