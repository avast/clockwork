package com.avast.clockwork;

import com.google.common.base.Preconditions;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 9:07 PM
 */
public abstract class AbstractPartitioner<K, V, P> implements Accumulator<K, V> {

    protected final PartitionFunction<K, V, P> partitionFunction;
    protected final int modulo;

    /**
     * Prototype clone (instance) constructor
     * @param image the prototype
     */
    protected AbstractPartitioner(AbstractPartitioner<K, V, P> image) {
        this(image.partitionFunction, image.modulo);
    }

    protected AbstractPartitioner(PartitionFunction<K, V, P> partitionFunction, int modulo) {
        Preconditions.checkNotNull(partitionFunction);
        this.partitionFunction = partitionFunction;
        this.modulo = modulo;
    }

    protected abstract void writeToPartition(P partition, K key, V value, Context context) throws Exception;

    public PartitionFunction<K, V, P> getPartitionFunction() {
        return partitionFunction;
    }

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
