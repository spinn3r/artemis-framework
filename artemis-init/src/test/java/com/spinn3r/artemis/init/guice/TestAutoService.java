package com.spinn3r.artemis.init.guice;

import com.google.inject.AbstractModule;
import com.google.inject.ProvisionException;
import com.google.inject.Singleton;
import com.spinn3r.artemis.init.AutoService;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.Mode;
import com.spinn3r.artemis.init.ServiceReferences;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import jdk.management.resource.internal.inst.SocketRMHooks;
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

    @Test
    public void testInitMode() throws Exception {

        Launcher launcher = Launcher.newBuilder()
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .setModule(new ShoppingCartModule())
                                    .build();

        launcher.init(new ServiceReferences());

        ShoppingCart shoppingCart = launcher.getInstance(ShoppingCart.class);

        DefaultShoppingCart defaultShoppingCart = launcher.getInstance(DefaultShoppingCart.class);

        assertTrue(shoppingCart == defaultShoppingCart);
        assertEquals(State.NONE, defaultShoppingCart.state);
        assertEquals(Mode.INIT, launcher.getMode());

        launcher.stop();

        assertNotEquals(State.STOPPED, defaultShoppingCart.state);

    }

    @Test(expected = ProvisionException.class)
    public void testBrokenWithLaunch() throws Exception {

        Launcher launcher = Launcher.newBuilder()
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .setModule(new AbstractModule() {
                                        @Override
                                        protected void configure() {
                                            bind(ShoppingCart.class).to(BrokenShoppingCart.class);
                                        }
                                    })
                                    .build();

        launcher.launch(new ServiceReferences());
        launcher.getInstance(ShoppingCart.class);

    }

    @Test(expected = ProvisionException.class)
    public void testBrokenWithInit() throws Exception {

        Launcher launcher = Launcher.newBuilder()
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .setModule(new AbstractModule() {
                                        @Override
                                        protected void configure() {
                                            bind(ShoppingCart.class).to(BrokenShoppingCart.class);
                                        }
                                    })
                                    .build();

        launcher.init(new ServiceReferences());
        launcher.getInstance(ShoppingCart.class);

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

    static class BrokenShoppingCart implements AutoService, ShoppingCart {

        @Override
        public void start() throws Exception {

        }

        @Override
        public void stop() throws Exception {

        }

        @Override
        public void checkout() {

        }

    }

    enum State {

        NONE,

        STARTED,

        STOPPED

    }

}
