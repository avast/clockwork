package com.avast.clockwork.util;

import com.avast.clockwork.PartitionFunction;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 9:14 PM
 */
public class HashCodePartFunction<K, V> implements PartitionFunction<K, V, Integer> {
    @Override
    public Integer getPartition(K key, V value, int modulo) {
        int hash = Math.abs(key.hashCode());
        return hash % modulo;
    }
}
