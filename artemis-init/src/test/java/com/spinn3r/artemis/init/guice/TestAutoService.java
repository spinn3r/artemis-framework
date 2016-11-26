package com.spinn3r.artemis.init.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.spinn3r.artemis.init.AutoService;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.Mode;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static com.spinn3r.artemis.init.Lifecycle.STARTED;
import static org.junit.Assert.*;

/**
 *
 */
public class TestAutoService {

    @Test
    public void testBasicUsage() throws Exception {

        Launcher launcher = Launcher.newBuilder()
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .setModule(new ShoppingCartModule())
                                    .build();
        launcher.launch();

        ShoppingCart shoppingCart = launcher.getInstance(ShoppingCart.class);

        DefaultShoppingCart defaultShoppingCart = launcher.getInstance(DefaultShoppingCart.class);

        assertTrue(shoppingCart == defaultShoppingCart);
        assertEquals(State.STARTED, defaultShoppingCart.state);
        assertEquals(Mode.LAUNCH, launcher.getMode());

        launcher.stop();

        assertEquals(State.STOPPED, defaultShoppingCart.state);


    }

    static class ShoppingCartModule extends AbstractModule {

        @Override
        protected void configure() {

            bind(ShoppingCart.class ).to(DefaultShoppingCart.class);

        }

    }

    interface ShoppingCart {

        void checkout();

    }

    @Singleton
    static class DefaultShoppingCart implements AutoService, ShoppingCart {

        State state = State.NONE;

        @Override
        public void checkout() {

        }

        @Override
        public void start() throws Exception {
            state = State.STARTED;
        }

        @Override
        public void stop() throws Exception {
            state = State.STOPPED;
        }

    }

    enum State {

        NONE,

        STARTED,

        STOPPED

    }

}
