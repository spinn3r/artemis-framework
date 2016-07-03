package com.spinn3r.artemis.logging.init;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.Version;
import com.spinn3r.artemis.init.modular.ModularService;
import com.spinn3r.artemis.init.tracer.Log4jTracerFactory;
import com.spinn3r.artemis.init.tracer.TracerFactory;
import com.spinn3r.artemis.init.tracer.TracerFactorySupplier;
import org.apache.log4j.xml.DOMConfigurator;

import java.net.URL;

/**
 * Setup logging to stdout for console applications.
 */
public class ConsoleLoggingService extends BaseService implements ModularService, LoggingServiceType {

    private final Provider<Version> versionProvider;

    private final Provider<Hostname> hostnameProvider;

    private final TracerFactorySupplier tracerFactorySupplier;

    @Inject
    ConsoleLoggingService(Provider<Version> versionProvider, Provider<Hostname> hostnameProvider, TracerFactorySupplier tracerFactorySupplier) {
        this.versionProvider = versionProvider;
        this.hostnameProvider = hostnameProvider;
        this.tracerFactorySupplier = tracerFactorySupplier;
    }

    @Override
    protected void configure() {
        bind( TracerFactory.class ).to( Log4jTracerFactory.class );
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

        tracerFactorySupplier.set(new Log4jTracerFactory());

    }

}
