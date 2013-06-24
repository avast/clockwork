package com.avast.clockwork;

/**
 * User: slajchrt
 * Date: 1/14/12
 * Time: 3:45 PM
 */
public class KeyValue<K, V> {
    
    private final K key;
    private final V value;

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + ":" + value;
    }
}
