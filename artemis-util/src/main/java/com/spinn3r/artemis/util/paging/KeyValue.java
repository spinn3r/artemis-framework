package com.spinn3r.artemis.util.paging;

/**
 * Implements a basic, immutable key value pair.
 */
public class KeyValue<K,V> {

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
        return "KeyValue{" +
                 "key=" + key +
                 ", value=" + value +
                 '}';
    }

}
