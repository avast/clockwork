package com.avast.clockwork;

/**
 * The Accumulator interface is an abstraction for the terminal component in an {@link Execution execution}.
 * <p/>
 * User: slajchrt
 * Date: 1/14/12
 * Time: 6:48 PM
 */
public interface Accumulator<K, V> extends Cloneable {

    /**
     * Receives key-value pairs emitted by the last transformer (mapper or reducer) in the execution chain.
     * @param key the last emitted key
     * @param value the last emitted value
     * @param context the context
     * @throws Exception
     */
    void send(K key, V value, Context context) throws Exception;

    /**
     * Called upon closing the execution
     * @throws Exception
     */
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
