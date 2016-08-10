package com.spinn3r.artemis.util.collections;

import java.util.Collection;
import java.util.Map;

/**
 * Called for each record in a map.  Used primarily during recursion.
 */
public interface CollectionRecursionListener {

    void onMap(Map<?,?> map);

    void onCollection(Collection<?> collection);
}
