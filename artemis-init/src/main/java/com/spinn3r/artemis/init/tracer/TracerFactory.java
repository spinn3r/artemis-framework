package com.spinn3r.artemis.init.tracer;

/**
 * Create a new tracer for the given source.
 */
public interface TracerFactory {

    public Tracer newTracer( Object service );

}
