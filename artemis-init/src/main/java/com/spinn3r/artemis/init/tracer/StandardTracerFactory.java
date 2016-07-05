package com.spinn3r.artemis.init.tracer;

/**
 *
 */
public class StandardTracerFactory implements TracerFactory {

    @Override
    public Tracer create(Object source) {
        return new StandardTracer( source );
    }

}
