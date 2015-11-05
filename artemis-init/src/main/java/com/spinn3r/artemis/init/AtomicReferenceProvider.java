package com.spinn3r.artemis.init;

import com.google.inject.Provider;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A provider backed by an AtomicReference.  This allows a service to export
 * multiple providers if necessary.
 */
public class AtomicReferenceProvider<T> implements Provider<T> {

    private AtomicReference<T> reference = new AtomicReference<>( null );

    public AtomicReferenceProvider() {
    }

    public AtomicReferenceProvider( T value ) {
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



