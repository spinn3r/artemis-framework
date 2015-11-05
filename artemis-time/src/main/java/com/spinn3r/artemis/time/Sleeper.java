package com.spinn3r.artemis.time;

import com.google.inject.Inject;

import java.util.concurrent.TimeUnit;

/**
 * Provides a way for code to sleep during execution without having to inject a
 * full clock.  Injecting a full clock means more surface area and room for
 * bugs.
 */
public class Sleeper {

    private final Clock clock;

    @Inject
    Sleeper(Clock clock) {
        this.clock = clock;
    }

    public void sleepUninterruptibly(long sleepFor, TimeUnit timeUnit) {
        clock.sleepUninterruptibly( sleepFor, timeUnit );
    }

    public void sleep(long sleepFor, TimeUnit timeUnit) throws InterruptedException {
        clock.sleep( sleepFor, timeUnit );
    }

}
