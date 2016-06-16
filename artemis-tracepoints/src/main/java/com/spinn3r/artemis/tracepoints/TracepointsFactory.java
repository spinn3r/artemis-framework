package com.spinn3r.artemis.tracepoints;

import com.google.inject.Inject;
import com.spinn3r.artemis.time.Clock;

/**
 *
 */
public class TracepointsFactory {

    private final Clock clock;

    private final TracepointFactory tracepointFactory;

    @Inject
    TracepointsFactory(Clock clock, TracepointFactory tracepointFactory) {
        this.clock = clock;
        this.tracepointFactory = tracepointFactory;
    }

    public Tracepoints create() {
        return new Tracepoints(clock, tracepointFactory);
    }

}
