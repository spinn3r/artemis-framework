package com.spinn3r.artemis.init;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * A supplier backed by an AtomicReference.  This allows a service to export
 * multiple providers if necessary.  Used in cases where Guice gets confused
 * about Providers and Supplier is a custom non-guice aware class.
 */
public class AtomicReferenceSupplier<T> implements Supplier<T> {

    private AtomicReference<T> reference = new AtomicReference<>( null );

    public AtomicReferenceSupplier() {
    }

    public AtomicReferenceSupplier(T value ) {
        set( value );
    }

    @Override
    public T get() {
        return reference.get();
    }

    public void set( T value ) {
        reference.set( value );
    }

}



