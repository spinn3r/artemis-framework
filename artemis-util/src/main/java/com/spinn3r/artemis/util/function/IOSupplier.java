package com.spinn3r.artemis.util.function;

import java.io.IOException;

/**
 *
 */
@FunctionalInterface
public interface IOSupplier<T> extends CheckedSupplier<T> {

    @Override
    T get() throws IOException;

}
