package com.spinn3r.artemis.metrics;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
public class Counter {

    private AtomicLong delegate = new AtomicLong( 0 );

    public void incr() {
        delegate.getAndAdd( 1 );
    }

    public void decr() {
        delegate.getAndAdd( -1 );
    }

    public long get() {
        return delegate.get();
    }

}
