package com.spinn3r.artemis.util.collections;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

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

    protected final HashMap<K,V> backing;

    public ImmutableDictionary() {
        this.backing = new HashMap<>(0);
    }

    public ImmutableDictionary(Map<K,V> parent) {
        Objects.requireNonNull(parent);
        this.backing = new HashMap<>(parent.size());
        this.backing.putAll(parent);
    }

    public ImmutableDictionary(ImmutableDictionary<K,V> parent) {
        Objects.requireNonNull(parent);
        this.backing = new HashMap<>(parent.size());
        this.backing.putAll(parent.backing);
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

    public ImmutableSet<K> keySet() {
        return ImmutableSet.copyOf( backing.keySet() );
    }

    public ImmutableMap<K,V> toMap() {
        return ImmutableMap.copyOf(backing);
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

    public static class Builder<K,V> {

        private ImmutableDictionary<K,V> result = new ImmutableDictionary<>();

        public Builder putAll( Map<K,V> map ) {
            result.backing.putAll(map);
            return this;
        }

        public Builder putAll( ImmutableDictionary<K,V> map ) {
            result.backing.putAll(map.backing);
            return this;
        }

        public ImmutableDictionary<K,V> build() {
            return result;
        }

    }

}
