package com.spinn3r.artemis.init.cache;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class DefaultServiceCache implements ServiceCache {

    private final Map<Class<?>,Object> backing = Maps.newConcurrentMap();

    private final AtomicInteger hits = new AtomicInteger();

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz) {

        if (! hasInstance(clazz))
            throw new NullPointerException("No backing for class: " + clazz);

        hits.getAndIncrement();

        return (T)backing.get(clazz);
    }

    @Override
    public <T> void putInstance(Class<T> clazz, T instance) {
        backing.put(clazz, instance);
    }

    @Override
    public boolean hasInstance(Class<?> clazz) {
        return backing.containsKey(clazz);
    }

    @Override
    public int hits() {
        return hits.get();
    }

}
