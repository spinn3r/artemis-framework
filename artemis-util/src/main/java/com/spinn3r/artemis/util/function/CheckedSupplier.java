package com.spinn3r.artemis.util.function;

/**
 *
 */
@FunctionalInterface
public interface CheckedSupplier<T> {

    T get() throws Exception;

}
