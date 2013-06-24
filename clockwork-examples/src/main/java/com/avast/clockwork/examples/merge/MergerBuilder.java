package com.avast.clockwork.examples.merge;

import java.util.HashMap;
import java.util.Map;

/**
 * User: zslajchrt
 * Date: 6/14/13
 * Time: 10:02 PM
 */
public class MergerBuilder {

    private final Map mergers = new HashMap();

    private MergerBuilder() {
    }

    public MergerBuilder keyMerger(Object key, MergeFunction mergeFunction) {
        mergers.put(key, mergeFunction);
        return this;
    }

    public Merger build() {
        return new Merger(new MergingReducer(new HashMap(mergers)));
    }

    public static MergerBuilder newBuilder() {
        return new MergerBuilder();
    }
}
