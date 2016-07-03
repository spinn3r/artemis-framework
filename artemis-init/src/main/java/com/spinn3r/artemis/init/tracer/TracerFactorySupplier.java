package com.spinn3r.artemis.init.tracer;

import com.spinn3r.artemis.init.AtomicReferenceSupplier;

/**
 *
 */
public class TracerFactorySupplier extends AtomicReferenceSupplier<TracerFactory> {

    private TracerFactorySupplier(TracerFactory value) {
        super(value);
    }

    public static final TracerFactorySupplier of(TracerFactory tracerFactory) {
        return new TracerFactorySupplier(tracerFactory);
    }

}
