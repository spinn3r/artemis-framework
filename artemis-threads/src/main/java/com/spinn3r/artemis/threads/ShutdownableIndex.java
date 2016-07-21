package com.spinn3r.artemis.threads;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains an index of shutdownables and a name that represents them.
 */
public class ShutdownableIndex {

    private final Class<?> owner;

    private final ImmutableMap<String,? extends Shutdownable> backing;

    public ShutdownableIndex(Class<?> owner, ImmutableMap<String,? extends Shutdownable> shutdownables) {
        this.owner = owner;
        this.backing = shutdownables;
    }

    public Class<?> getOwner() {
        return owner;
    }

    public int size() {
        return backing.size();
    }

    protected ImmutableMap<String, ? extends Shutdownable> getBacking() {
        return backing;
    }
}
