package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.advertisements.Caller;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import com.spinn3r.artemis.util.misc.Stack;
import org.junit.After;
import org.junit.Before;

import static com.spinn3r.artemis.init.Services.ref;

/**
 * JUnit test that uses a launcher and can shut down once tests are complete.
 */
public class BaseLauncherTest {

    protected Launcher launcher;

    /**
     * Setup with no services but run an injector.
     */
    @Before
    public void setUp() throws Exception {
        setUp( new ResourceConfigLoader(), new ServiceReferences() );
    }

    @SafeVarargs
    protected final void setUp( Class<? extends Service>... classes ) throws Exception {
        setUp( new ResourceConfigLoader(), classes );
    }

    @SafeVarargs
    protected final void setUp( ConfigLoader configLoader, Class<? extends Service>... classes ) throws Exception {

        ServiceReferences serviceReferences = new ServiceReferences();

        for (Class<? extends Service> clazz : classes) {
            serviceReferences.add( ref( clazz ) );
        }

        setUp( configLoader, serviceReferences );

    }

    protected void setUp( ServiceReference... serviceReferences ) throws Exception {
        setUp( new ServiceReferences( serviceReferences ) );
    }

    protected void setUp( ServiceReferences serviceReferences ) throws Exception {
        setUp( new ResourceConfigLoader(), serviceReferences );
    }

    protected void setUp( ConfigLoader configLoader, ServiceReferences serviceReferences ) throws Exception {

        launcher = Launcher.newBuilder(configLoader ).build();

        launcher.advertise( Caller.class, new Caller( Stack.caller().getClassName() ) );
        launcher.launch( serviceReferences );

        launcher.getInjector().injectMembers( this );

    }

    @After
    public void tearDown() throws Exception {

        if ( launcher != null )
            launcher.stop();

    }

}
