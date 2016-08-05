package com.spinn3r.artemis.init.modular.stages.impl;

import com.google.inject.Injector;
import com.spinn3r.artemis.init.Advertised;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.Service;
import com.spinn3r.artemis.init.cache.ServiceCache;
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

    private final ServiceCache serviceCache;

    public InitStageRunner(ConfigLoader configLoader, Advertised advertised, ServiceCache serviceCache) {
        this.configLoader = configLoader;
        this.advertised = advertised;
        this.serviceCache = serviceCache;
    }

    @Override
    public void run(Injector injector, Service service ) {

        TracerFactory tracerFactory = injector.getInstance( TracerFactory.class );

        service.setAdvertised( advertised );
        service.setTracer( tracerFactory.create( service ) );
        service.setConfigLoader( configLoader );
        service.setServiceCache(serviceCache);

        service.init();

    }

}
