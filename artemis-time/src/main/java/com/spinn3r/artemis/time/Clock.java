package com.spinn3r.artemis.time;

import com.google.inject.ImplementedBy;

import java.util.concurrent.TimeUnit;

/**
 * Interface for dealing with time in an abstract manner so that we can
 * change the underlying clock for testing (and other) purposes.
 */
@ImplementedBy( SystemClock.class )
public interface Clock {

    long currentTimeMillis();

    Time getTime();

    /**
     * Sleep for the given time value using the specified TimeUnit.
     *
     * @param sleepFor
     * @param timeUnit
     */
    void sleepUninterruptibly( long sleepFor , TimeUnit timeUnit );

    void sleep( long sleepFor, TimeUnit timeUnit ) throws InterruptedException;

}
