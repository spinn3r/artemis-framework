package com.spinn3r.artemis.init.cache;

/**
 * Generic caching infra to resolve instances of
 */
public interface ServiceCache {

    <T> T getInstance(Class<T> clazz);

    <T> void putInstance(Class<T> clazz, T instance);

    boolean hasInstance(Class<?> clazz);

    /**
     * Return the total number of hits in the service cache.  Used for testing
     */
    int hits();

}
