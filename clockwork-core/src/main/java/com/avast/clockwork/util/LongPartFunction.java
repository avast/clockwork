package com.avast.clockwork.util;

import com.avast.clockwork.PartitionFunction;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 9:14 PM
 */
public class LongPartFunction<V> implements PartitionFunction<Long, V, Integer> {
    @Override
    public Integer getPartition(Long key, V value, int modulo) {
        int ikey = key.intValue();
        return Math.abs(ikey % modulo);
    }

}
