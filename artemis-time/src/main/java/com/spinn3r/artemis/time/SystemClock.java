package com.spinn3r.artemis.time;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.inject.Singleton;

import java.util.concurrent.TimeUnit;

/**
 * A clock that uses the current machine time.
 */
@Singleton
public class SystemClock extends BaseClock {

    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    @Override
    public void sleepUninterruptibly( long sleepFor, TimeUnit timeUnit ) {
        Uninterruptibles.sleepUninterruptibly( sleepFor, timeUnit );
    }

    @Override
    public void sleep( long sleepFor, TimeUnit timeUnit ) throws InterruptedException {
        Thread.sleep( timeUnit.toMillis( sleepFor ) );
    }

}
