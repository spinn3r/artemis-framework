package com.spinn3r.artemis.init.tracer;

import com.spinn3r.artemis.init.Service;

/**
 *
 */
public class Log4jTracerFactory implements TracerFactory {

    @Override
    public Tracer create(Object service) {
        return new Log4jTracer( service );
    }

}


