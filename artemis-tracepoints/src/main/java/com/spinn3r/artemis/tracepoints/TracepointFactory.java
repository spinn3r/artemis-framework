package com.spinn3r.artemis.tracepoints;

import com.google.inject.Inject;
import com.spinn3r.artemis.time.Clock;

/**
 *
 */
class TracepointFactory {

    private final Clock clock;

    @Inject
    TracepointFactory(Clock clock) {
        this.clock = clock;
    }

    Tracepoint create(Tracepoints tracepoints, TraceKey traceKey, ThreadInfo threadInfo, long acquired) {
        return new Tracepoint(clock, tracepoints, traceKey, threadInfo, acquired);
    }

}
