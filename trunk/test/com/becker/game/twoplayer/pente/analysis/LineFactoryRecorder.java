/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.pente.analysis;

import com.becker.game.twoplayer.pente.Patterns;
import com.becker.optimization.parameter.ParameterArray;

import java.util.LinkedList;
import java.util.List;

/**
 * Creates lines and remembers what lines were created.
 * @author Barry Becker
 */
public class LineFactoryRecorder extends LineFactory {

    private List<Line> lines_;

    public LineFactoryRecorder() {
        lines_ = new LinkedList<Line>();
    }

    @Override
    public Line createLine(Patterns patterns, ParameterArray weights) {
        Line line = new Line(new LineEvaluator(patterns, weights));
        lines_.add(line);
        return line;
    }

    public List<Line> getCreatedLines() {
        return lines_;
    }

    public void clearLines() {
        lines_.clear();
    }
}