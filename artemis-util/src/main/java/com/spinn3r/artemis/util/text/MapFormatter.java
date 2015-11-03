package com.spinn3r.artemis.util.text;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/**
 *
 */
public class MapFormatter {


    public static  <K,V> String table(Map<K, V> map) {

        StringBuilder buff = new StringBuilder();

        for (Map.Entry<K, V> entry : map.entrySet()) {
            buff.append( String.format( "%s: %s\n", entry.getKey().toString(), entry.getValue().toString() ) );

        }

        return buff.toString();

    }

    public static <K,V> String tableSortedByKey(Map<K,V> map) {
        Map<K,V> copy = new TreeMap<>();
        copy.putAll( map );
        return table( copy );
    }

    /**
     * More portable and deterministic version of Map.toString which sort keys.
     *
     * Various map implementations have different semantics on how keys are sorted.
     *
     * @param map
     * @return
     */
    public static String toString(Map<?, ?> map) {

        Map<String,Object> stringMap = Maps.newTreeMap();

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            stringMap.put( entry.getKey().toString(), entry.getValue() );
        }

        return stringMap.toString();

    }

}

