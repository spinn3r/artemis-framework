package com.spinn3r.artemis.time;

import com.google.common.collect.Lists;
import com.google.inject.Singleton;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A synthetic clock that allows us to jump time for testing purposes.
 */
@Singleton
public class SyntheticClock extends BaseClock {

    private AtomicLong currentTimeMillis;

    public List<Slept> sleep = Lists.newCopyOnWriteArrayList();

    public AtomicLong sleptDuration = new AtomicLong( 0 );

    public SyntheticClock(long currentTimeMillis) {
        this.currentTimeMillis = new AtomicLong( currentTimeMillis );
    }

    @Override
    public long currentTimeMillis() {
        return currentTimeMillis.get();
    }

    /**
     * Pretend to sleep uninterruptibly when in reality we just return immediately
     * and then jump the clock forward for the requested sleep interval.
     *
     * @param sleepFor
     * @param timeUnit
     */
    @Override
    public void sleepUninterruptibly( long sleepFor, TimeUnit timeUnit ) {
        long sleepMillis = timeUnit.toMillis( sleepFor );
        forward( sleepMillis );
        sleptDuration.getAndAdd( sleepMillis );

        // now keep track of how long we slept.
        sleep.add( new Slept( sleepFor, timeUnit ) );

    }

    @Override
    public void sleep(long sleepFor, TimeUnit timeUnit) throws InterruptedException {
        sleepUninterruptibly( sleepFor, timeUnit );
    }

    /**
     * Jump the clock forward in milliseconds.
     * @param ms
     */
    public Time backward(long ms) {
        return forward( -1 * ms );
    }

    /**
     * Jump the clock forward in milliseconds.
     * @param ms
     */
    public Time forward(long ms) {
        currentTimeMillis.getAndAdd( ms );
        return getTime();
    }

    public void setCurrentTimeMillis( long currentTimeMillis ) {
        this.currentTimeMillis.set( currentTimeMillis );
    }

    /**
     * Change the current time to given time.
     * @param time
     */
    public void setTime( Time time ) {
        setCurrentTimeMillis( time.getTimeMillis() );
    }

    public List<Slept> getSleep() {
        return sleep;
    }
}
