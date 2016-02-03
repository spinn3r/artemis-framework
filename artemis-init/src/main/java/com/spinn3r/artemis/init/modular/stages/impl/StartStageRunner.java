package com.spinn3r.artemis.init.modular.stages.impl;

import com.google.common.base.Stopwatch;
import com.google.inject.Injector;
import com.spinn3r.artemis.init.modular.ModularService;
import com.spinn3r.artemis.init.modular.stages.StageRunner;
import com.spinn3r.artemis.init.tracer.Tracer;
import com.spinn3r.artemis.init.tracer.TracerFactory;

/**
 *
 */
public class StartStageRunner implements StageRunner{

    @Override
    public void run(Injector injector, ModularService modularService ) throws Exception {

        try {

            TracerFactory tracerFactory = injector.getInstance( TracerFactory.class );
            Tracer tracer = tracerFactory.create( modularService );

            tracer.info( "Starting service: %s ...", modularService.getClass().getName() );

            Stopwatch stopwatch = Stopwatch.createStarted();

            modularService.start();

            tracer.info( "Starting service: %s ...done (%s)", modularService.getClass().getName(), stopwatch.stop() );

        } catch ( Exception e ) {
            throw new Exception( "Failed to start: " + modularService.getClass().getName(), e );
        }

    }

}
