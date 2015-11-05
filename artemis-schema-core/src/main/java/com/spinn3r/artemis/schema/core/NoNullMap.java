package com.spinn3r.artemis.schema.core;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
@SuppressWarnings("serial")
public class NoNullMap<K,V> extends LinkedHashMap<K,V> {

    private static final String NO_NULL_KEYS = "Null keys are not accepted";
    private static final String NO_NULL_VALUES = "Null values are not accepted";

    @Override
    public V put(K key, V value) {

        if ( key == null ) {
            throw new RuntimeException( NO_NULL_KEYS );
        }

        if ( value == null ) {
            throw new RuntimeException( NO_NULL_VALUES );
        }

        return super.put( key, value );
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

        validate( m );

        super.putAll( m );

    }

    public static void validate( Map<? extends Object, ? extends Object> m ) {

        for (Map.Entry<?,?> entry : m.entrySet()) {

            if ( entry.getKey() == null ) {
                throw new RuntimeException( NO_NULL_KEYS );
            }

            if ( entry.getValue() == null ) {
                throw new RuntimeException( NO_NULL_VALUES );
            }

        }

    }

}
