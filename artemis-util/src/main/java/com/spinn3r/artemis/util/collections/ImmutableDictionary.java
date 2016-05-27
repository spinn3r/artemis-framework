package com.spinn3r.artemis.util.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * An immutable dictionary to map keys to values.  Note that this does NOT
 * have remove, removeAll, etc methods that other immutable map implementations
 * expose.  We don't want these visible because we should generate compilation
 * errors when these are called.
 */
public class ImmutableDictionary<K,V> {

    private final HashMap<K,V> backing;

    public ImmutableDictionary() {
        this.backing = new HashMap<>(0);
    }

    public ImmutableDictionary(Map<K,V> map) {
        Objects.requireNonNull(map);
        this.backing = new HashMap<>(map.size());
        this.backing.putAll(map);
    }

    public int size() {
        return backing.size();
    }

    public boolean isEmpty() {
        return backing.isEmpty();
    }

    public V get(Object key) {
        return backing.get(key);
    }

    public boolean containsKey(Object key) {
        return backing.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return backing.containsValue(value);
    }

    public V getOrDefault(Object key, V defaultValue) {
        return backing.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        backing.forEach(action);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImmutableDictionary)) return false;

        ImmutableDictionary<?, ?> that = (ImmutableDictionary<?, ?>) o;

        return backing.equals(that.backing);

    }

    @Override
    public int hashCode() {
        return backing.hashCode();
    }

    public String toString() {
        return backing.toString();
    }

}
