package com.spinn3r.artemis.threads;

import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains an index of shutdownables and a name that represents them.
 */
public class ShutdownableIndex implements Shutdownable {

    private final String name;

    private final ConcurrentHashMap<String,Shutdownable> backing = new ConcurrentHashMap<>();

    public ShutdownableIndex(String name) {
        this.name = name;
    }

    public ShutdownableIndex(Class<?> clazz) {
        this(clazz.getName());
    }

    public String getName() {
        return name;
    }

    public void put(Class<?> clazz, Shutdownable entry) {
        put(clazz.getName(), entry);
    }

    public void put(String key, Shutdownable entry) {
        if (backing.containsKey( key )) {
            throw new IllegalStateException("Entry with key already exists: " + key);
        }

        backing.put(key, entry);

    }

    public void putAll(Map<String,Shutdownable> newEntries) {
        for (Map.Entry<String, Shutdownable> entry : newEntries.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    public void putAll(ShutdownableIndex shutdownableIndex) {
        putAll(shutdownableIndex.backing);
    }

    public void add(Shutdownable shutdownable) {
        put(shutdownable.getClass().getName(), shutdownable);
    }

    public int size() {
        return backing.size();
    }

    public ImmutableSet<Map.Entry<String, ? extends Shutdownable>> entrySet() {
        return ImmutableSet.copyOf(backing.entrySet());
    }

    @Override
    public void shutdown() throws Exception {
        Shutdownables.shutdown(this);
    }

    public static class Builder {

        private final ShutdownableIndex result;

        public Builder(String name) {
            result = new ShutdownableIndex(name);
        }

        public Builder(Class<?> clazz) {
            this(clazz.getName());

        }

        public Builder with(ShutdownableIndex shutdownableIndex) {
            result.putAll(shutdownableIndex);
            return this;
        }

        public ShutdownableIndex build() {
            return result;
        }

    }

}
