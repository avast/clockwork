package com.avast.clockwork;

import com.google.common.base.Preconditions;

/**
 * A special abstract implementation of the Accumulator interface representing a partitioner. Such a partitioner forwards
 * the incoming key-value pairs to a partition (like another node) selected by consulting the partition function.
 * <p/>
 * User: slajchrt
 * Date: 1/14/12
 * Time: 9:07 PM
 */
public abstract class AbstractPartitioner<K, V, P> implements Accumulator<K, V> {

    protected final PartitionFunction<K, V, P> partitionFunction;
    protected final int modulo;

    /**
     * @param partitionFunction the partition function
     * @param modulo the modulo passed to the partition function when resolving the target partition
     */
    protected AbstractPartitioner(PartitionFunction<K, V, P> partitionFunction, int modulo) {
        Preconditions.checkNotNull(partitionFunction);
        this.partitionFunction = partitionFunction;
        this.modulo = modulo;
    }

    /**
     * Called when the partition is resolved and the key-value pair is to be forwarded to the partition.
     * @param partition the partition for the key-value pair
     * @param key the forwarded key
     * @param value the forwarded value
     * @param context the context
     * @throws Exception
     */
    protected abstract void writeToPartition(P partition, K key, V value, Context context) throws Exception;

    /**
     * Resolves the partition for the key-value pair and forwards it to the partition.
     * @param key the last emitted key
     * @param value the last emitted value
     * @param context the context
     * @throws Exception
     */
    @Override
    public void send(K key, V value, Context context) throws Exception {
        P partition = partitionFunction.getPartition(key, value, modulo);
        writeToPartition(partition, key, value, context);
    }

    @Override
    public void flush(K key) throws Exception {
    }

    @Override
    public void flush() throws Exception {
    }

}
