package com.spinn3r.artemis.threads;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains an index of shutdownables and a name that represents them.
 */
public class ShutdownableIndex extends ConcurrentHashMap<String,Shutdownable> {

    private final String name;

    public ShutdownableIndex(String name) {
        this.name = name;
    }

    public ShutdownableIndex(Class<?> clazz) {
        this.name = clazz.getName();
    }

    public String getName() {
        return name;
    }
}
