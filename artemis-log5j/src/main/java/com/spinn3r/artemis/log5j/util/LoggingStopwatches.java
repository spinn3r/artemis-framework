package com.spinn3r.artemis.log5j.util;

import com.spinn3r.artemis.log5j.DefaultLogTarget;
import com.spinn3r.artemis.log5j.LogTarget;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.artemis.time.SystemClock;
import com.spinn3r.log5j.Logger;

import java.util.concurrent.TimeUnit;

/**
 *
 */
public class LoggingStopwatches {

    private static Clock clock = new SystemClock();

    public static LoggingStopwatchBuilder create( Logger log, Class<?> clazz ) {
        LogTarget logTarget = new DefaultLogTarget( log );
        return create( logTarget, clazz );
    }

    public static LoggingStopwatchBuilder create( LogTarget logTarget, Class<?> clazz ) {
        return new LoggingStopwatchBuilder( logTarget, clazz );
    }

    public static LoggingStopwatch create( Logger log, Class<?> clazz, long warnDuration, TimeUnit timeUnit ) {
        LogTarget logTarget = new DefaultLogTarget( log );
        return create( logTarget, clazz, warnDuration, timeUnit );
    }

    public static LoggingStopwatch create( LogTarget logTarget, Class<?> clazz, long warnDuration, TimeUnit timeUnit ) {
        return new LoggingStopwatch( clock, logTarget, clazz, warnDuration, timeUnit );
    }

}
