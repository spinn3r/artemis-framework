package com.spinn3r.artemis.log5j.util;

import com.spinn3r.artemis.log5j.LogTarget;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class LoggingStopwatchBuilder {

    private final LogTarget logTarget;

    private final Class<?> clazz;

    private long warnDuration = 1000;

    private TimeUnit warnTimeUnit = TimeUnit.MILLISECONDS;

    public LoggingStopwatchBuilder(LogTarget logTarget, Class<?> clazz) {
        this.logTarget = logTarget;
        this.clazz = clazz;
    }

    public LoggingStopwatch build() {
        return LoggingStopwatches.create( logTarget, clazz, warnDuration, warnTimeUnit );
    }

}
