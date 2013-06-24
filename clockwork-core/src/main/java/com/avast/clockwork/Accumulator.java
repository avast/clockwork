package com.avast.clockwork;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 6:48 PM
 */
public interface Accumulator<K, V> extends Cloneable {
    
    void send(K key, V value, Context context) throws Exception;

    void close() throws Exception;

    /**
     * Flushes forcibly the buffers for the given key
     * @throws Exception
     */
    void flush(K key) throws Exception;

    /**
     * Flushes forcibly all buffers
     * @throws Exception
     */
    void flush() throws Exception;

}
