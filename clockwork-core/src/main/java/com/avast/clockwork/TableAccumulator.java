package com.avast.clockwork;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.*;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 7:57 PM
 */
public class TableAccumulator<K, V> implements Accumulator<K, V> {

    protected final Map<K, V> table;
    private final Reducer<K, V, K, V> combiner;

    public TableAccumulator() {
        this(null, null);
    }

    protected TableAccumulator(Comparator<? super K> comparator, Reducer<K, V, K, V> combiner) {
        this.table = comparator == null ? new HashMap<K, V>() : new TreeMap<K, V>(comparator);
        this.combiner = combiner;
    }

    public Map<K, V> getTable() {
        return table;
    }

    @Override
    public synchronized void send(K key, V value, Context context) throws Exception {
        if (table.containsKey(key)) {

            if (combiner != null) {
                V existingValue = table.get(key);
                Execution.execute(
                        key,
                        Lists.newArrayList(existingValue, value).iterator(), context, new Function<Map.Entry<K, V>, Void>() {
                    @Override
                    public Void apply(java.util.Map.Entry<K, V> input) {
                        // update the combined value
                        table.put(input.getKey(), input.getValue());
                        return null;
                    }
                }, combiner);
            } else {
                throw new IllegalStateException("Key " + key + " is already in the map");
            }
        } else {
            table.put(key, value);
        }

    }


    @Override
    public synchronized void close() throws Exception {
        drain();
    }

    @Override
    public void flush(K key) throws Exception {
    }

    @Override
    public synchronized void flush() throws Exception {
        drain();
    }

    protected void drain() throws Exception {
    }

    protected void clear() {
        table.clear();
    }
}
