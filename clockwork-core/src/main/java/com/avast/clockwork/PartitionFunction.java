package com.avast.clockwork;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 9:07 PM
 */
public interface PartitionFunction<K, V, P> {
    
    P getPartition(K key, V value, int modulo) throws Exception;
    
}
