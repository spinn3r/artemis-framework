package com.spinn3r.artemis.tracepoints;

import com.spinn3r.artemis.time.Clock;

import java.util.Optional;

/**
 * A Tracepoint is a block of code that may take a long period of time to execute.
 * This would be measured in the order of milliseconds, seconds, or minutes.  This
 * would be a database call, API call, HTTP request, etc.
 *
 */
public class Tracepoint {

    private final Clock clock;

    private final TraceKey traceKey;

    private final ThreadInfo threadInfo;

    private final Tracepoints tracepoints;

    private final long acquired;

    private Optional<Long> released;

    public Tracepoint(Clock clock, Tracepoints tracepoints, TraceKey traceKey, ThreadInfo threadInfo, long acquired) {
        this.clock = clock;
        this.traceKey = traceKey;
        this.threadInfo = threadInfo;
        this.tracepoints = tracepoints;
        this.acquired = acquired;
        this.released = released;
    }

    public void release() {

        released = Optional.of(clock.getTime().toMillis());

        tracepoints.release(traceKey);

    }

}
