package com.becker.game.twoplayer.go.board.analysis.group;

import com.becker.common.util.LRUCache;
import com.becker.game.twoplayer.go.board.elements.group.IGoGroup;

import java.util.Map;
import java.util.WeakHashMap;


/**
 * Maintains a map from groups to GroupAnalyzers.
 * If we request an analyzer for a group that is not in the map, we add it.
 *
 * @author Barry Becker
 */
public class GroupAnalyzerMap {

    private LRUCache<IGoGroup, GroupAnalyzer> analyzerMap;

    /**
     * Constructor.
     * Use a weak HashMap because we do not want this to be the only thing
     * keeps it around when it is no longer on the board.
     */
    public GroupAnalyzerMap() {
        analyzerMap = new LRUCache<IGoGroup, GroupAnalyzer>(20000);
                    //new WeakHashMap<IGoGroup, GroupAnalyzer>();
    }

    /**
     * Get an analyzer for a specific group.
     * If the analyzer is not there yet, it gets added.
     * @param group the group to analyze.
     * @return the analyzer for the specified group
     */
    public GroupAnalyzer getAnalyzer(IGoGroup group) {
        GroupAnalyzer cachedAnalyzer = analyzerMap.get(group);
        if (cachedAnalyzer != null) {
            return cachedAnalyzer;
        }
        GroupAnalyzer analyzer = new GroupAnalyzer(group, this);
        analyzerMap.put(group, analyzer);

        //System.out.println("num analyzers in map =" + analyzerMap.numEntries() );
        return analyzer;
    }

}
