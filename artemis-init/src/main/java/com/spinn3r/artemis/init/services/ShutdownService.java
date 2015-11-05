package com.spinn3r.artemis.init.services;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.LauncherShutdownHook;

/**
 * The last service a daemon should run if it wants to properly shutdown by
 * calling launcher.stop during JVM shutdown.
 */
public class ShutdownService extends BaseService {

    private final Launcher launcher;

    @Inject
    ShutdownService(Launcher launcher) {
        this.launcher = launcher;
    }

    @Override
    public void start() throws Exception {
        Runtime.getRuntime().addShutdownHook( new LauncherShutdownHook( launcher ) );
    }

}
