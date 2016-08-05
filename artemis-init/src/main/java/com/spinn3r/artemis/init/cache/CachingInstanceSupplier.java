package com.spinn3r.artemis.init.cache;

/**
 *
 */
public interface CachingInstanceSupplier<T> {

    T get() throws Exception;

}
