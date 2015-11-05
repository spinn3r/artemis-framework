package com.spinn3r.artemis.init.tracer;

import com.spinn3r.artemis.init.Service;

/**
 *
 */
public class StandardTracerFactory implements TracerFactory {

    @Override
    public Tracer newTracer(Object source) {
        return new StandardTracer( source );
    }

}
