package com.spinn3r.artemis.init.modular;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.init.advertisements.Hostname;
import com.spinn3r.artemis.init.advertisements.HostnameServiceType;
import com.spinn3r.artemis.init.advertisements.VersionServiceType;
import com.spinn3r.artemis.init.services.HostnameService;
import com.spinn3r.artemis.init.tracer.Tracer;
import com.spinn3r.artemis.init.tracer.TracerFactory;
import com.spinn3r.artemis.logging.init.ConsoleLoggingService;
import com.spinn3r.artemis.logging.init.LoggingServiceType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class ModularLauncherTest {

    @Inject
    Hostname hostname;

    @Test
    public void testBasic() throws Exception {

        ModularServiceReferences serviceReferences
          = new ModularServiceReferences()
            .put( HostnameServiceType.class, MockHostnameService.class );

        ModularLauncher modularLauncher =
          ModularLauncher.create( serviceReferences ).build();

        modularLauncher.start();

        assertNotNull( modularLauncher.getInjector() );

        modularLauncher.getInjector().injectMembers( this );

        modularLauncher.stop();

    }

    @Test
    public void testBasicWithTwoServices() throws Exception {

        ModularServiceReferences serviceReferences
          = new ModularServiceReferences()
              .put( HostnameServiceType.class, MockHostnameService.class )
              .put( VersionServiceType.class, MockVersionService.class );

        ModularLauncher modularLauncher =
          ModularLauncher.create( serviceReferences ).build();

        modularLauncher.start();

        assertNotNull( modularLauncher.getInjector() );

        modularLauncher.getInjector().injectMembers( this );

        modularLauncher.stop();

    }


    @Test
    @Ignore
    public void testBasicWithNewTracer() throws Exception {

        // TODO: this doesn't work because we call replace....
        //
        // we VERY rarely need to replace so maybe I can handle
        // this as a one -off...
        //
        // I did an audit and I think this is really the only one we have to
        // replace... We should use a TracerFactoryReference that we can
        // inject and then just call set() on it to replace it with a new
        // one...

        ModularServiceReferences serviceReferences
          = new ModularServiceReferences()
              .put( HostnameServiceType.class, MockHostnameService.class )
              .put( VersionServiceType.class, MockVersionService.class )
              .put( LoggingServiceType.class, ConsoleLoggingService.class )
          ;

        ModularLauncher modularLauncher =
          ModularLauncher.create( serviceReferences ).build();

        modularLauncher.start();

        modularLauncher.getInjector().injectMembers( this );

        // FIXME: this has to be added as a queue by dependencies...
        // we should have a TracerFactoryManager with push/pop methods.  These
        // can take a new TracerFactory , push it on to the queue, and pop it off
        // without conflicting with bindings.

        TracerFactory tracerFactory = modularLauncher.getInjector().getInstance( TracerFactory.class );

        modularLauncher.stop();

    }
}





