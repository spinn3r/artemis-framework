package com.spinn3r.artemis.util.collections;

import java.util.Map;

/**
 *
 */
public class Maps {

    public static void recurse(Map<?,?> map, MapListener listener) {

        for (Map.Entry<?,?> entry : map.entrySet()) {

            listener.onEntry(map, entry.getKey(), entry.getValue());

            if ( entry.getValue() instanceof Map) {
                Map<?,?> entryAsMap = (Map<?,?>)entry.getValue();
                recurse(entryAsMap, listener);
            }

        }

    }

}
