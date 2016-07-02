package com.spinn3r.artemis.init.modular.stages.impl;

import com.google.common.base.Stopwatch;
import com.google.inject.Injector;
import com.spinn3r.artemis.init.Service;
import com.spinn3r.artemis.init.modular.ModularService;
import com.spinn3r.artemis.init.modular.stages.StageRunner;
import com.spinn3r.artemis.init.tracer.Tracer;
import com.spinn3r.artemis.init.tracer.TracerFactory;

/**
 *
 */
public class StartStageRunner implements StageRunner{

    @Override
    public void run(Injector injector, Service service ) throws Exception {

        try {

            TracerFactory tracerFactory = injector.getInstance( TracerFactory.class );
            Tracer tracer = tracerFactory.create( service );

            tracer.info( "Starting service: %s ...", service.getClass().getName() );

            Stopwatch stopwatch = Stopwatch.createStarted();

            service.start();

            tracer.info( "Starting service: %s ...done (%s)", service.getClass().getName(), stopwatch.stop() );

        } catch ( Exception e ) {
            throw new Exception( "Failed to start: " + service.getClass().getName(), e );
        }

    }

}
