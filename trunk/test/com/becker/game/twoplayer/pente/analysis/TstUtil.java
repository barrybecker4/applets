/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.becker.game.twoplayer.pente.analysis;

import com.becker.game.common.board.BoardPosition;
import com.becker.game.common.board.GamePiece;

import java.util.List;

/**
 * Created by IntelliJ IDEA. User: becker Date: Dec 27, 2009 Time: 8:15:01 AM To change this template use File |
 * Settings | File Templates.
 */
public class TstUtil {

    private TstUtil() {}

    public static Line createLine(String linePattern, LineEvaluator evaluator) {

        Line line = new Line(evaluator);
        for (int i=0; i<linePattern.length(); i++) {
             GamePiece piece = null;
             char c = linePattern.charAt(i);
              if (c == 'X') {
                  piece = new GamePiece(true);
              }
              if (c == 'O') {
                  piece = new GamePiece(false);
              }
              BoardPosition pos = new BoardPosition(0, 0, piece);
              line.append(pos);
        }
        return line;
    }


    public static void printLines(List<Line> lines) {
        StringBuilder bldr = new StringBuilder();
        int len = lines.size();
        for (int i=0; i<len; i++)  {
            bldr.append('"').append(lines.get(i)).append('"');
            if (i<len-1)
                bldr.append(", ");
        }
        System.out.println("lines = " + bldr.toString());
    }

    public static String quoteStringList(List<CharSequence> strings) {
        StringBuilder bldr = new StringBuilder();
        int len = strings.size();
        for (int i=0; i<len; i++)  {
            bldr.append('"').append(strings.get(i)).append('"');
            if (i<len-1)
                bldr.append(", ");
        }
        return bldr.toString();
    }
}
