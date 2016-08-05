package com.spinn3r.artemis.init.cache;

import com.spinn3r.artemis.init.Service;

/**
 *
 */
public class NullServiceCache implements ServiceCache {

    @Override
    public <T> T getInstance(Class<T> clazz) {
        throw new NullPointerException("No entries");
    }

    @Override
    public <T> void putInstance(Class<T> clazz, T instance) {

    }

    @Override
    public boolean hasInstance(Class<?> clazz) {
        return false;
    }

    @Override
    public int hits() {
        return 0;
    }

}
