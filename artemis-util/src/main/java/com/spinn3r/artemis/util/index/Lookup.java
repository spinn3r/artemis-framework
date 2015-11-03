package com.spinn3r.artemis.util.index;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public interface Lookup<K,V> {

    V getOrCreate(K key);

    V get(K key);

    Set<K> keySet();

    Set<Map.Entry<K,V>> entrySet();

    Collection<V> getValues();

}
