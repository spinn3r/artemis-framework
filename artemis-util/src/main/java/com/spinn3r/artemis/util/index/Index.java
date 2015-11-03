package com.spinn3r.artemis.util.index;

/**
 *
 */
public interface Index<K,V,E extends Exception> {

    V getOrCreate( K key ) throws E;

    V get( K key );

}
