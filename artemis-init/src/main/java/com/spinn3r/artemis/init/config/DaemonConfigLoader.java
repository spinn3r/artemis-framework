package com.spinn3r.artemis.init.config;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.advertisements.Daemon;

/**
 * Use the daemon name to determine where we should load our config.
 */
public class DaemonConfigLoader extends FileConfigLoader {

    @Inject
    public DaemonConfigLoader(Daemon daemon) {
        super( String.format( "/etc/%s", daemon.getValue() ) );
    }

}
