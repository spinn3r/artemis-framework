package com.spinn3r.artemis.init.modular.stages.impl;

import com.google.inject.Injector;
import com.spinn3r.artemis.init.Advertised;
import com.spinn3r.artemis.init.Service;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.modular.ModularService;
import com.spinn3r.artemis.init.modular.stages.StageRunner;
import com.spinn3r.artemis.init.tracer.TracerFactory;

/**
 *
 */
public class InitStageRunner implements StageRunner {

    private final ConfigLoader configLoader;

    private final Advertised advertised;

    public InitStageRunner(ConfigLoader configLoader, Advertised advertised) {
        this.configLoader = configLoader;
        this.advertised = advertised;
    }

    @Override
    public void run(Injector injector, Service service ) {

        TracerFactory tracerFactory = injector.getInstance( TracerFactory.class );

        service.setAdvertised( advertised );
        service.setTracer( tracerFactory.create( service ) );
        service.setConfigLoader( configLoader );

        service.init();

    }

}
