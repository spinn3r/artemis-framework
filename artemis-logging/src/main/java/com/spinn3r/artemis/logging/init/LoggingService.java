package com.spinn3r.artemis.logging.init;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.Version;
import com.spinn3r.artemis.init.modular.ModularService;
import com.spinn3r.artemis.init.tracer.Log4jTracerFactory;
import com.spinn3r.artemis.init.tracer.TracerFactory;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Perform log4j init.
 */
@Config( path = "logging.conf",
         required = true,
         implementation = LoggingConfig.class )
public class LoggingService extends BaseService implements ModularService, LoggingServiceType {

    private final LoggingConfig config;

    private final Provider<Version> versionProvider;

    private final Provider<Hostname> hostnameProvider;

    @Inject
    LoggingService(LoggingConfig config, Provider<Version> versionProvider, Provider<Hostname> hostnameProvider ) {
        this.config = config;
        this.versionProvider = versionProvider;
        this.hostnameProvider = hostnameProvider;
    }

    @Override
    public void start() throws Exception {

        info( "Using log config: %s", config );

        if ( config.getDir() != null ) {

            String logRoot = System.getProperty( "log.root", config.getDir() );

            // set it again in case we're using the default.
            System.setProperty( "log.root", logRoot );

            info( "Initializing using log.root: %s", logRoot );

            Files.createDirectories( Paths.get( logRoot ) );

        }

        info( "Loading log4j config file: %s", config.getConf() );

        URL url = getConfigLoader().getResource( this.getClass(), config.getConf() );

        if ( url == null ) {
            throw new Exception( "Could not find config file: " );
        }


        DOMConfigurator.configure( url );

        // **** load the artemis version.

        info( "Using artemis.version: %s", versionProvider.get().getValue() );
        info( "Using server.hostname: %s", hostnameProvider.get().getValue() );

        org.apache.log4j.MDC.put( "artemis.version", versionProvider.get().getValue() );
        org.apache.log4j.MDC.put( "server.hostname", hostnameProvider.get().getValue() );

        // now start using the new log4j tracer so that we can switch to having
        // services write via log4j.

        // TODO: special case this one and don't use replace..
        replace( TracerFactory.class, new Log4jTracerFactory() );

    }

}
