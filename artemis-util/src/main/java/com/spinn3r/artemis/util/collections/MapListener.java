package com.spinn3r.artemis.util.collections;

import java.util.Map;

/**
 * Called for each record in a map.  Used primarily during recursion.
 */
public interface MapListener {

    void onEntry(Map<?,?> map, Object key, Object value);

}
