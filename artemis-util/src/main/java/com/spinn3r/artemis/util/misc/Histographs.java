package com.spinn3r.artemis.util.misc;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class Histographs {

    public static <K extends Comparable<K>> Histograph<K> intersection( Histograph<K> h0, Histograph<K> h1 ) {

        Histograph<K> result = new Histograph<>();

        for ( K key : h0.keySet() ) {

            if ( h1.delegate.containsKey( key ) ) {
                result.incr( key, h0.get( key ) );
                result.incr( key, h1.get( key ) );
            }

        }

        return result;

    }

    public static <K extends Comparable<K>> Histograph<K> union( Histograph<K> h0, Histograph<K> h1 ) {

        Histograph<K> result = new Histograph<>( h0 );

        for (Map.Entry<K, AtomicInteger> entry : h1.delegate.entrySet()) {
            result.incr( entry.getKey(), entry.getValue().get() );
        }

        return result;

    }

    public static <K extends Comparable<K>> long sum( Histograph<K> histograph ) {

        long result = 0;

        for ( K key : histograph.keySet() ) {
            result += histograph.get( key );
        }

        return result;

    }

}
