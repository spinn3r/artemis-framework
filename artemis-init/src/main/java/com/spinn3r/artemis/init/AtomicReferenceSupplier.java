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
        set(value );
    }

    @Override
    public T get() {
        return reference.get();
    }

    /**
     * Define the value of this supplier.  We only allow this to be called once,
     * during initialization.
     */
    public void set(T value ) {

        if ( reference.get() != null )
            throw new RuntimeException("Value already defined");

        reference.set( value );

    }

}



