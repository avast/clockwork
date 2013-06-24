package com.avast.clockwork;

import java.util.Comparator;
import java.util.Map;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 7:57 PM
 */
public abstract class ForwardingAccumulator<K, V> extends TableAccumulator<K, V> {

    public ForwardingAccumulator() {
    }

    public ForwardingAccumulator(Comparator<? super K> comparator, Reducer<K, V, K, V> combiner) {
        super(comparator, combiner);
    }

    @Override
    protected void drain() throws Exception {
        for (Map.Entry<K, V> kvEntry : table.entrySet()) {
            send_(kvEntry.getKey(), kvEntry.getValue());
        }
        clear();
    }

    protected abstract void send_(K key, V value) throws Exception;
}
