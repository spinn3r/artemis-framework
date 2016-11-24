package com.spinn3r.artemis.init;

import com.google.common.base.Preconditions;

import java.util.function.Supplier;

/**
 *
 */
public class DirectSupplier<T> implements Supplier<T> {

    private final T value;

    public DirectSupplier(T value) {
        this.value = Preconditions.checkNotNull(value);
    }

    @Override
    public T get() {
        return value;
    }

}
