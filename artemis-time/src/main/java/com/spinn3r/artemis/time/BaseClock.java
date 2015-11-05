package com.spinn3r.artemis.time;

import java.util.concurrent.TimeUnit;

/**
 * Base for all clock implementations and supports getTime from the current
 * time in millis.
 */
public abstract class BaseClock implements Clock {

    @Override
    public Time getTime() {
        return new Time( currentTimeMillis(), TimeUnit.MILLISECONDS );
    }

}
