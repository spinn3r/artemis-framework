package com.spinn3r.artemis.log5j.util;

import com.spinn3r.artemis.log5j.LogTarget;
import com.spinn3r.artemis.time.Clock;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class LoggingStopwatch {

    private final long started;

    private final Clock clock;

    private final LogTarget log;

    private final Class<?> clazz;

    private final long warnDuration;

    private final TimeUnit timeUnit;

    public LoggingStopwatch(Clock clock, LogTarget log, Class<?> clazz, long warnDuration, TimeUnit timeUnit) {
        this.started = clock.currentTimeMillis();
        this.clock = clock;
        this.log = log;
        this.clazz = clazz;
        this.warnDuration = warnDuration;
        this.timeUnit = timeUnit;
    }

    public void stop( String name ) {

        long stopped = clock.currentTimeMillis();

        long duration = stopped - started;

        long warnDurationMillis = timeUnit.convert( warnDuration, TimeUnit.MILLISECONDS );

        if ( duration > warnDurationMillis ) {
            log.warn( "Call to %s in %s took too long (%,d ms)", name, clazz.getName(), duration );
        }

    }

}


