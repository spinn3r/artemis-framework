package com.spinn3r.artemis.init;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import com.spinn3r.artemis.util.misc.Stack;
import org.junit.After;
import org.junit.Before;

import static com.spinn3r.artemis.init.Services.*;

/**
 * JUnit test that uses a launcher and can shut down once tests are complete.
 */
public class LauncherTest {

    protected Launcher launcher;

    protected ConfigLoader configLoader = new ResourceConfigLoader();

    protected Module module = Modules.EMPTY_MODULE;

    protected ServiceReferences serviceReferences = new ServiceReferences();

    /**
     * Setup with no services but run an injector.
     */
    @Before
    public void setUp() throws Exception {

        launcher = Launcher.newBuilder(configLoader)
                           .setCaller(Stack.caller().getClassName())
                           .setModule(module)
                           .build();

        launcher.launch(serviceReferences);

        launcher.getInjector().injectMembers(this);

    }

    @After
    public void tearDown() throws Exception {

        if ( launcher != null )
            launcher.stop();

    }

    public void setConfigLoader(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setServiceReferences(ServiceReferences serviceReferences) {
        this.serviceReferences = serviceReferences;
    }

    public void setServiceReferences(ImmutableList<Class<? extends Service>> serviceReferences) {

        this.serviceReferences = new ServiceReferences();

        for (Class<? extends Service> serviceReference : serviceReferences) {
            this.serviceReferences.add(serviceReference);
        }

    }

}
