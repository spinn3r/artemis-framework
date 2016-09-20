package com.spinn3r.artemis.util.misc;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.util.text.TableFormatter;

import java.util.*;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A map which contains a histograph of entries by key.  This is a cleaner version
 * of HitMap in that the code and API are lighter and more like a collections
 * object.
 */
public class Histograph<K extends Comparable<K>> {

    protected ConcurrentMap<K,AtomicInteger> delegate = Maps.newConcurrentMap();

    public Histograph() {
    }

    public Histograph( Histograph<K> parent ) {

        for (Map.Entry<K, AtomicInteger> entry : parent.delegate.entrySet()) {
            delegate.put( entry.getKey(), new AtomicInteger( entry.getValue().get() ) );
        }

    }

    /**
     * Increment a key with a delta of 1
     */
    public void incr(K key) {
        incr( key, 1 );
    }

    /**
     * Increment a key with the given delta.
     */
    public void incr(K key, int delta) {
        delegate.putIfAbsent( key, new AtomicInteger( 0 ) );
        delegate.get( key ).getAndAdd( delta );
    }

    /**
     * Get all keys in the index.
     */
    public Set<K> keySet() {
        return delegate.keySet();
    }

    /**
     * Get a given entry by key.
     */
    public int get( K key ) {
        return delegate.get( key ).get();
    }

    public int getOrDefault(K key, int defaultValue) {
        return delegate.getOrDefault(key, new AtomicInteger(0)).get();
    }

    public int size() {
        return delegate.size();
    }

    protected void put( K key, int value ) {
        delegate.put( key, new AtomicInteger( value ) );
    }

    protected void remove( String key ) {
        delegate.remove( key );
    }

    public Map<K,AtomicInteger> read() {
        return read( Integer.MAX_VALUE );
    }

    /**
     * Read all keys by rank descending.  This returns a LinkedHashMap so that
     * we can iterate over it in descending order by still preserve value lookup.
     */
    public Map<K,AtomicInteger> read( int limit ) {

        List<Map.Entry<K,AtomicInteger>> list = Lists.newArrayList( delegate.entrySet() );

        // sort the list descending by value but ascending by key if the values
        // are identical
        Collections.sort( list, new Comparator<Map.Entry<K, AtomicInteger>>() {
            @Override
            public int compare(Map.Entry<K, AtomicInteger> o1, Map.Entry<K, AtomicInteger> o2) {

                int diff = o2.getValue().get() - o1.getValue().get();

                if ( diff == 0 ) {
                    return o1.getKey().toString().compareTo( o2.getKey().toString() );
                }

                return diff;

            }
        } );

        Map<K,AtomicInteger> result = Maps.newLinkedHashMap();

        int idx = 0;

        for (Map.Entry<K, AtomicInteger> entry : list) {

            result.put( entry.getKey(), entry.getValue() );
            ++idx;

            if ( idx >= limit )
                break;

        }

        return result;

    }

    public void merge( Histograph<K> source ) {

        for (Map.Entry<K, AtomicInteger> entry : source.delegate.entrySet()) {
            incr( entry.getKey(), entry.getValue().get() );
        }

    }


    public String format() {
        return format( Integer.MAX_VALUE );
    }

    public String format( int limit ) {

        Map<K,AtomicInteger> histograph = read( limit );

        List<List<String>> table = Lists.newArrayList();

        for (Map.Entry<K, AtomicInteger> entry : histograph.entrySet()) {

            table.add( Lists.newArrayList( entry.getKey().toString(),
                                           String.format( "%,d", entry.getValue().get() ) ) );

        }

        return TableFormatter.format( table );

    }

    /**
     * Compute a new histograph using values that are percentages, not literal
     * counts.  You can then use format() to print a histograph with % format.
     */
    public Histograph<K> toPerc() {

        Histograph<K> result = new Histograph<>();

        int sum = 0;

        for (AtomicInteger value : delegate.values()) {
            sum += value.get();
        }

        if ( sum == 0 ) {
            // not defined for empty histographs.
            return result;
        }

        for (Map.Entry<K, AtomicInteger> entry : delegate.entrySet()) {

            K key = entry.getKey();
            int value = entry.getValue().get();

            int perc = (int)(100 * (value / (double)sum));

            result.incr( key, perc );

        }

        return result;

    }

    /**
     * Convert this to a string representation of the index.  Since it's a
     * histograph, we first rebuild the index.
     *
     * @return
     */
    @Override
    public String toString() {
        return read().toString();
    }

}
