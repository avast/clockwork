package com.avast.clockwork;

import java.util.Comparator;
import java.util.Map;

/**
 * This a specialized table accumulator that drains the table content when flushing the execution.
 * <p/>
 * User: slajchrt
 * Date: 1/14/12
 * Time: 7:57 PM
 */
public abstract class ForwardingAccumulator<K, V> extends TableAccumulator<K, V> {

    public ForwardingAccumulator() {
    }

    /**
     * @param comparator the key comparator or null if no sorting
     * @param combiner the combiner used when a duplicate key is inserted or null
     */
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

    /**
     * Called for each entry when draining the table.
     * @param key the key
     * @param value the value
     * @throws Exception
     */
    protected abstract void send_(K key, V value) throws Exception;
}
