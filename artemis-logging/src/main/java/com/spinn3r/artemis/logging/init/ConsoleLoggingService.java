package com.spinn3r.artemis.logging.init;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.Version;
import com.spinn3r.artemis.init.tracer.Log4jTracerFactory;
import com.spinn3r.artemis.init.tracer.TracerFactory;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Setup logging to stdout for console applications.
 */
public class ConsoleLoggingService extends BaseService {

    private final Provider<Version> versionProvider;

    private final Provider<Hostname> hostnameProvider;

    @Inject
    ConsoleLoggingService(Provider<Version> versionProvider, Provider<Hostname> hostnameProvider ) {
        this.versionProvider = versionProvider;
        this.hostnameProvider = hostnameProvider;
    }

    @Override
    public void start() throws Exception {

        String conf = "/log4j-stdout.xml";

        info( "Loading log4j config file: %s", conf );

        URL url = ConsoleLoggingService.class.getResource( conf );

        if ( url == null ) {
            throw new Exception( "Could not find config file: " + conf );
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
