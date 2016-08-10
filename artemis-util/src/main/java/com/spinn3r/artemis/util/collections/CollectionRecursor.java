package com.spinn3r.artemis.util.collections;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class CollectionRecursor {

    public static void recurse(Map<?,?> map, CollectionRecursionListener listener) {

        listener.onMap(map);

        for (Map.Entry<?,?> entry : map.entrySet()) {

            if ( entry.getValue() instanceof Map) {
                Map<?,?> entryAsMap = (Map<?,?>)entry.getValue();
                recurse(entryAsMap, listener);
            }

            if ( entry.getValue() instanceof Collection) {
                Collection<?> entryAsCollection = (Collection<?>)entry.getValue();
                recurse(entryAsCollection, listener);
            }

        }

    }

    public static void recurse(Collection<?> collection, CollectionRecursionListener listener) {

        listener.onCollection(collection);

        for (Object current : collection) {

            if ( current instanceof Map) {
                Map<?,?> entryAsMap = (Map<?,?>)current;
                recurse(entryAsMap, listener);
            }

            if ( current instanceof Collection) {
                Collection<?> entryAsCollection = (Collection<?>)current;
                recurse(entryAsCollection, listener);
            }

        }

    }

}
