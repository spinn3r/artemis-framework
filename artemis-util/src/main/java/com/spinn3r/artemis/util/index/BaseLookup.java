package com.spinn3r.artemis.util.index;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import com.spinn3r.artemis.util.text.MapFormatter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create an index object that has a lookup from key K to value V and throws exception E.
 */
public abstract class BaseLookup<K,V> implements Lookup<K,V> {

    protected ConcurrentHashMap<K,V> delegate = new ConcurrentHashMap<>();

    /**
     * Get an existing mapping or create a new one representing the key.
     * @param key
     * @return
     */
    public final V getOrCreate( K key ) {

        V result = delegate.get( key );

        if ( result == null ) {

            result = create( key );

            // returns the previous value associated with the specified key, or
            // null if there was no mapping for the key

            V existing = delegate.putIfAbsent( key, result );

            if ( existing == null ) {

                onCreateSuccessful( key, result );

                // TODO call onCreated by using a listener...  this is different
                // than onCreateSuccessful because listeners don't need to worry
                // about the issues of concurrent hash maps.

                return result;

            } else {
                return existing;
            }

        }

        return result;

    }

    public V get( K key ) {
        return delegate.get( key );
    }

    public V remove( K key ) {
        return delegate.remove( key );
    }

    public boolean containsKey( K key ) {
        return delegate.containsKey( key );
    }

    @Override
    public Set<K> keySet() {

        Set<K> result = new TreeSet<>();
        result.addAll( delegate.keySet() );

        return result;


    }

    @Override
    public Set<Map.Entry<K,V>> entrySet() {
        return delegate.entrySet();
    }

    @Override
    public Collection<V> getValues() {
        return delegate.values();
    }

    protected abstract V create(K key);

    /**
     * Called when the create executed and successfully adds a new entry to the
     * map.  This is different from onCreated because a race would mean we index
     * it twice.
     *
     * @param key
     */
    protected void onCreateSuccessful( K key, V value ) {

    }

    /**
     * Return the size of the throttle queue index.  This is non blocking
     * and not constant time but not bad either.
     *
     * @return
     */
    public int size() {
        return delegate.size();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    public String table() {
        return MapFormatter.table( delegate );
    }

}