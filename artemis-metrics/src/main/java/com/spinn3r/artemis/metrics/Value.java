package com.spinn3r.artemis.metrics;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple value that (rarely) changes and is mostly static.
 */
public class Value {

    private final AtomicLong delegate = new AtomicLong( 0 );

    public Value() { }

    public Value( long value ) {
        set( value );
    }

    public void set( long value ) {
        delegate.set( value );
    }

    public long get() {
        return delegate.get();
    }

}
