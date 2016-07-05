package com.spinn3r.artemis.init.guice;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Test;

/**
 *
 */
public class TestForcedBinding {

    @Test(expected = ConfigurationException.class)
    public void testForcingBinding() throws Exception {

        Injector injector = Guice.createInjector();
        injector.getInstance(Home.class);

    }

    static class Home {

        private Home() {
        }

    }


}
