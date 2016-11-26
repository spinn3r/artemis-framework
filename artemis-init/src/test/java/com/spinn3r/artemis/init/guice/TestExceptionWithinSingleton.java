package com.spinn3r.artemis.init.guice;

import com.google.inject.*;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import org.junit.Test;

/**
 *
 */
public class TestExceptionWithinSingleton {

    @Test(expected = ProvisionException.class)
    public void testBasicUsage() throws Exception {

        Launcher launcher = Launcher.newBuilder()
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .setModule(new ReactorModule())
                                    .build();
        launcher.launch();

        Injector injector = launcher.getInjector();
        injector.getInstance(Reactor.class);

    }

    static class ReactorModule extends AbstractModule {

        @Override
        protected void configure() {

            bind(Reactor.class ).to(FusionReactor.class);

        }

    }

    interface Reactor {

    }

    @Singleton
    static class FusionReactor implements Reactor {

        @Inject
        FusionReactor() throws Exception {
            throw new ReactorException("Core meltdown.");
        }

    }

    static class ReactorException extends Exception {

        public ReactorException(String message) {
            super(message);
        }
    }

}
