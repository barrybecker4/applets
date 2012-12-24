// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.game.multiplayer.poker.ui;

import com.barrybecker4.common.geometry.Location;
import com.barrybecker4.game.common.GameContext;
import com.barrybecker4.game.twoplayer.common.ui.TwoPlayerBoardRenderer;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

import static com.barrybecker4.game.multiplayer.poker.ui.PokerPlayerRenderer.FONT_SIZE;
import static com.barrybecker4.game.multiplayer.poker.ui.PokerPlayerRenderer.POKER_FONT;

/**
 * Renders a players poker chips
 * @author Barry Becker
 */
public class ChipRenderer {

    private static final Color BLACK_COLOR   = Color.black;
    private static final double CHIP_PILE_WIDTH = 0.9;
    private static final double CHIP_HEIGHT = 0.15;


    /**
     * this draws the players chips at the specified location.
     */
    public void render( Graphics2D g2, Location location, int amount, int cellSize) {

        PokerChips chips = new PokerChips(amount);

        GameContext.log(3,"chips stacks = "+ chips);

        int x;
        int width;
        int height = 0;
        int y = cellSize * location.getRow();

        for (PokerChip chipType : chips.keySet()) {

            int numChips = chips.get(chipType);
            height = (int)(cellSize * numChips * CHIP_HEIGHT);
            width = (int)(CHIP_PILE_WIDTH * cellSize);
            g2.setColor(chipType.getColor());
            x = (int)((chipType.ordinal() * CHIP_PILE_WIDTH + location.getCol() + 1) * cellSize);
            y = location.getRow() * cellSize - height;

            g2.fillRect(x, y, width, height);
            g2.setColor(BLACK_COLOR);
            g2.drawRect(x, y, width, height);
            for (int j = 1; j < numChips; j++) {
                 y = (int)(cellSize * (location.getRow() - j * CHIP_HEIGHT));
                 g2.drawLine(x, y, (int)(x + cellSize * CHIP_PILE_WIDTH), y);
            }
        }

        // amount of cash represented by chips
        g2.setColor(BLACK_COLOR);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(JComponent.getDefaultLocale());

        String cashAmount = currencyFormat.format(amount);
        x = (location.getCol() + 1) * cellSize;
        Font f = POKER_FONT.deriveFont((float) cellSize /
                 TwoPlayerBoardRenderer.MINIMUM_CELL_SIZE * FONT_SIZE);

        g2.setFont(f);
        g2.drawString(cashAmount, x , (int)(y + height + cellSize/1.2));
    }
}
