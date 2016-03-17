package com.spinn3r.artemis.util.concurrent;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple concurrent SetMultimap.
 */
public class ConcurrentSetMultimap<K,V> {

    // TODO: implement more methods from SetMultimap

    private final ConcurrentHashMap<K,Set<V>> delegate = new ConcurrentHashMap<>();

    private final ImmutableSet<V> EMPTY_SET = ImmutableSet.of();

    public ImmutableSet<V> get(K key) {

        Set<V> result = delegate.get( key );

        if ( result == null ) {
            return EMPTY_SET;
        }

        return ImmutableSet.copyOf( result );

    }

    /**
     * Stores a key-value pair in this multimap.
     *
     * <p>Some multimap implementations allow duplicate key-value pairs, in which
     * case {@code put} always adds a new key-value pair and increases the
     * multimap size by 1. Other implementations prohibit duplicates, and storing
     * a key-value pair that's already in the multimap has no effect.
     *
     * @return {@code true} if the method increased the size of the multimap, or
     *     {@code false} if the multimap already contained the key-value pair and
     *     doesn't allow duplicates
     */
    public boolean put(K key, V value) {

        Set<V> current = delegate.computeIfAbsent( key, (k) -> Sets.newConcurrentHashSet() );
        return current.add( value );

    }

    public void clear() {
        delegate.clear();
    }

}
