package com.avast.clockwork.util;

import com.avast.clockwork.Accumulator;
import com.avast.clockwork.Context;

/**
 * User: slajchrt
 * Date: 1/15/12
 * Time: 12:28 PM
 */
public class DummyAccumulator<K, V> implements Accumulator<K, V> {
    @Override
    public void send(K key, V value, Context context) throws Exception {
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public void flush(K key) throws Exception {
    }

    @Override
    public void flush() throws Exception {
    }

}
