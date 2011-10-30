/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.simulation.common;

import com.becker.common.ColorMap;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Interface for models that exist on a cartesian grid.
 *
 * @author Barry Becker
 */
public interface RectangularModel {

    /**
     * @return model value which we will create a color for
     */
    double getValue(int x, int y);

    /**
     * @return the row that we have calculated up to.
     */
    int getCurrentRow();

    /**
     * @return the row that used to be the current row the last time.
     */
    int getLastRow();

    int getWidth();
    int getHeight();
}
