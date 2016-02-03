package com.spinn3r.artemis.init.modular.stages;

import com.google.inject.Injector;
import com.spinn3r.artemis.init.modular.ModularService;

/**
 *
 */
public interface StageRunner {

    /**
     * Run a given state like init/start/stop.
     *
     */
    void run( Injector injector, ModularService modularService ) throws Exception;

}
