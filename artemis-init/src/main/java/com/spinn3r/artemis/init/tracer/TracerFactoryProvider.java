package com.spinn3r.artemis.init.tracer;

import com.spinn3r.artemis.init.AtomicReferenceProvider;

/**
 *
 */
public class TracerFactoryProvider extends AtomicReferenceProvider<TracerFactory> {

    private TracerFactoryProvider(TracerFactory value) {
        super(value);
    }

    public static final TracerFactoryProvider of(TracerFactory tracerFactory) {
        return new TracerFactoryProvider(tracerFactory);
    }

}
