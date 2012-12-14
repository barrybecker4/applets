/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.card;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Barry Becker
 */
public enum Suit {

    // listed in increasing order of value
    SPADES("S"),
    CLUBS("C"),
    DIAMONDS("D"),
    HEARTS("H");

    private final String symbol;
    private static final Map<String, Suit> SUIT_FROM_SYMBOL = new HashMap<String, Suit>();

    static {
        for (Suit r : values()) {
            SUIT_FROM_SYMBOL.put(r.getSymbol(), r);
        }
    }

    Suit(String symbol) {
        this.symbol = symbol;
    }

    String getSymbol() {
        return symbol;
    }

    public static Suit getSuitForSymbol(String symbol) {
        return SUIT_FROM_SYMBOL.get(symbol);
    }

}