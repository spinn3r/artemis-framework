package com.spinn3r.artemis.threads;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Contains an index of shutdownables and a name that represents them.
 */
public class ShutdownableIndex extends ConcurrentHashMap<String,Shutdownable> implements Shutdownable {

    // TODO: consider making this immutable...

    private final String name;

    public ShutdownableIndex(String name) {
        this.name = name;
    }

    public ShutdownableIndex(Class<?> clazz) {
        this(clazz.getName());
    }

    public String getName() {
        return name;
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
