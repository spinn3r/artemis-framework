package com.spinn3r.artemis.init.guice;

import com.google.inject.*;
import com.spinn3r.artemis.init.*;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import jdk.management.resource.internal.inst.SocketRMHooks;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static com.spinn3r.artemis.init.Lifecycle.STARTED;
import static org.apache.log4j.Category.getInstance;
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
    public void testMultipleGetInstanceWithOneStartMethodCalled() throws Exception {

        // verify that auto services only have their start method called once...

        Launcher launcher = Launcher.newBuilder()
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .setModule(new ShoppingCartModule())
                                    .build();
        launcher.launch();

        ShoppingCart s0 = launcher.getInstance(ShoppingCart.class);
        ShoppingCart s1 = launcher.getInstance(ShoppingCart.class);

        DefaultShoppingCart s2 = launcher.getInstance(DefaultShoppingCart.class);
        DefaultShoppingCart s3 = launcher.getInstance(DefaultShoppingCart.class);

        assertTrue(s0 == s1);
        assertTrue(s1 == s2);
        assertTrue(s2 == s3);

        assertEquals(1, s2.started);

        launcher.stop();

        assertEquals(State.STOPPED, s2.state);

    }

    // FIXME: test that multiple getInstance() calls don't call start N times... just once.

    @Test
    @Ignore
    public void testLaunchWithServices() throws Exception {

        // TODO: this still doesn't work because we create a new injector
        // for each iteration but AutoServices, from now on, will work properly
        // once the injector is created...

        Launcher launcher = Launcher.newBuilder()
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .setModule(new ShoppingCartModule())
                                    .build();

        launcher.launch(new ServiceReferences()
                          .add(FirstService.class)
                          .add(SecondService.class));

        Injector injector = launcher.getInjector();

        ShoppingCart shoppingCart = injector.getInstance(ShoppingCart.class);
        DefaultShoppingCart defaultShoppingCart = injector.getInstance(DefaultShoppingCart.class);

        assertTrue(shoppingCart == defaultShoppingCart);

        FirstService firstService = injector.getInstance(FirstService.class);
        SecondService secondService = injector.getInstance(SecondService.class);

        assertEquals(1, DefaultShoppingCart.INSTANCES_CREATED);

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

        public static int INSTANCES_CREATED = 0;

        State state = State.NONE;

        int started = 0;

        @Inject
        DefaultShoppingCart() {
            ++INSTANCES_CREATED;
        }

        @Override
        public void checkout() {

        }

        @Override
        public void start() throws Exception {
            state = State.STARTED;
            ++started;
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

    @Singleton
    static class FirstService extends BaseService {

        protected final ShoppingCart shoppingCart;

        @Inject
        FirstService(ShoppingCart shoppingCart) {
            System.out.printf("Starting first service...: %s\n", shoppingCart.hashCode());

            this.shoppingCart = shoppingCart;
        }

    }

    @Singleton
    static class SecondService extends BaseService {

        protected final ShoppingCart shoppingCart;

        @Inject
        SecondService(ShoppingCart shoppingCart) {
            System.out.printf("Starting second service...\n");

            this.shoppingCart = shoppingCart;
        }

    }

}
