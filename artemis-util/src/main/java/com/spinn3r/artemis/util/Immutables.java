package com.spinn3r.artemis.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class Immutables {

    public static <K,V> ImmutableMap<K,ImmutableList<V>> copyOfMultiMap( Map<K,List<V>> input ) {

        Map<K,ImmutableList<V>> tmp = Maps.newLinkedHashMap();

        for (Map.Entry<K, List<V>> entry : input.entrySet()) {

            if ( entry.getKey() == null ) {
                // null keys are evil..
                continue;
            }

            tmp.put( entry.getKey(), ImmutableList.copyOf( entry.getValue() ) );
        }

        return ImmutableMap.copyOf( tmp );

    }

    public static <K,V> Map<K,List<V>> mutableMultimap( ImmutableMap<K,ImmutableList<V>> input ) {

        Map<K,List<V>> tmp = Maps.newLinkedHashMap();

        for (Map.Entry<K, ImmutableList<V>> entry : input.entrySet()) {

            if ( entry.getKey() == null ) {
                // null keys are evil..
                continue;
            }

            tmp.put( entry.getKey(), entry.getValue() );

        }

        return tmp;

    }

}
