package com.becker.game.twoplayer.common.search.transposition;

import com.becker.common.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * A version of the HashKey that maintains its history.
 *
 * @author Barry Becker
 */
public class HistoricalHashKey extends HashKey {

    public List<String> moveHistory;
    public List<Long> keyHistory;

    /**
     * Create the static table of random numbers to use for the Hash from a sample board.
     */
    public HistoricalHashKey() {
        moveHistory = new LinkedList<String>();
        keyHistory = new LinkedList<Long>();
    }

    /**
     * Constructor used for tests where we want to create a HashKey with a specific value.
     * ^ is exclusive or (XOR)
     * @param key key value.
     */
    public HistoricalHashKey(Long key) {
        super(key);
    }

    @Override
    public void applyMove(Location move, long specialNumber) {
        super.applyMove(move, specialNumber);

        keyHistory.add(specialNumber);
        moveHistory.add("{" + move.toString() + specialNumber + "}");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoricalHashKey)) return false;

        HistoricalHashKey hashKey = (HistoricalHashKey) o;

        return !(getKey() != null ? !getKey().equals(hashKey.getKey()) : hashKey.getKey() != null);
    }

    public long undoKeyHistory() {
        long currentKey = getKey();
        for (Long keyHist : keyHistory)    {
            currentKey ^= keyHist;
        }
        return currentKey;
    }


    public String toString() {

        StringBuilder bldr = new StringBuilder();
        for (Long hkey : keyHistory) {
            bldr.append(Long.toBinaryString(hkey)).append(", ");
        }
        return "key="+ Long.toBinaryString(getKey()) + "\nhistory=\n" + moveHistory.toString()
                + "\nkeyHistory=\n" + bldr.toString();
    }
}
