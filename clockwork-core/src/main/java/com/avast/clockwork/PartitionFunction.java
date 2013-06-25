package com.avast.clockwork;

/**
 * This function resolves the partition for a given key-value pair and the modulo.
 * <p/>
 * User: slajchrt
 * Date: 1/14/12
 * Time: 9:07 PM
 */
public interface PartitionFunction<K, V, P> {

    /**
     * @param key the key
     * @param value the value
     * @param modulo the modulo
     * @return the partition for a given key-value pair and the modulo
     * @throws Exception
     */
    P getPartition(K key, V value, int modulo) throws Exception;
    
}
