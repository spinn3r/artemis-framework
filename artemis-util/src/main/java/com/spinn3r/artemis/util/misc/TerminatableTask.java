package com.spinn3r.artemis.util.misc;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public abstract class TerminatableTask {

    private AtomicBoolean terminated = new AtomicBoolean( false );

    public boolean isTerminated() {
        return terminated.get();
    }

    public void requestTermination() {
        terminated.set( true );
    }

}
