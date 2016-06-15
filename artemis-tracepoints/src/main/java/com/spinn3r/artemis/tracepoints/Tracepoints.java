package com.spinn3r.artemis.tracepoints;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.spinn3r.artemis.time.Clock;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Main entry point for tracepoints we're working with.
 */
@Singleton
public class Tracepoints {

    private ConcurrentHashMap<TraceKey, Tracepoint> index = new ConcurrentHashMap<>();

    private final Clock clock;

    private final TracepointFactory tracepointFactory;

    @Inject
    Tracepoints(Clock clock, TracepointFactory tracepointFactory) {
        this.clock = clock;
        this.tracepointFactory = tracepointFactory;
    }

    public Tracepoint acquire(String name) {

        Thread currentThread = Thread.currentThread();

        ThreadInfo threadInfo = new ThreadInfo(currentThread.getId(), currentThread.getName(), currentThread.getPriority());

        TraceKey traceKey = new TraceKey(currentThread.getId(), name);

        Tracepoint tracepoint = tracepointFactory.create(this, traceKey, threadInfo, clock.currentTimeMillis());
        index.put(traceKey, tracepoint);

        return tracepoint;

    }

    protected void release(TraceKey traceKey) {
        index.remove(traceKey);
    }

}
