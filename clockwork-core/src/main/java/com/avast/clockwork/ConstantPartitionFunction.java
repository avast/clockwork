package com.avast.clockwork;

/**
 * This partition function resolves all key-value pairs to the same partition.
 * <p/>
 * User: slajchrt
 * Date: 9/14/12
 * Time: 5:01 PM
 */
public class ConstantPartitionFunction<K, V, P> implements PartitionFunction<K, V, P> {
    private final P partition;

    public ConstantPartitionFunction(P partition) {
        this.partition = partition;
    }

    @Override
    public P getPartition(K key, V value, int modulo) throws Exception {
        return partition;
    }
}
