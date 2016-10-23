package com.spinn3r.artemis.init;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import com.spinn3r.artemis.init.guice.TestEagerSingleton;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 *
 */
public class TestSingletonsInFirstModule {

    @Test
    @Ignore
    public void testSingletonsInFirstModule() throws Exception {

        Launcher launcher = Launcher.newBuilder()
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .setModule(new ShoppingCartModule())
                                    .build();
        launcher.launch(new ServiceReferences()
                          .add(Service1.class)
                          .add(Service2.class));

        DefaultShoppingCart defaultShoppingCart = launcher.getInjector().getInstance(DefaultShoppingCart.class);
        ShoppingCart shoppingCart = launcher.getInjector().getInstance(ShoppingCart.class);

        assertTrue(shoppingCart == defaultShoppingCart);
        // TODO: this doesn't work because the cart
        Assert.assertEquals(1, defaultShoppingCart.created);

    }

    static class ShoppingCartModule extends AbstractModule {

        @Override
        protected void configure() {

            bind(ShoppingCart.class).to(DefaultShoppingCart.class);

        }

    }

    interface ShoppingCart {

        void checkout();

    }

    @Singleton
    static class DefaultShoppingCart implements AutoService, ShoppingCart {

        public static int created = 0;

        @Inject
        DefaultShoppingCart() {
            System.out.printf("Created!");
            ++created;
        }

        @Override
        public void checkout() {

        }

        @Override
        public void start() throws Exception {
        }

        @Override
        public void stop() throws Exception {

        }

    }

    static class Service1 extends BaseService {

        private final ShoppingCart shoppingCart;

        @Inject
        Service1(ShoppingCart shoppingCart) {
            this.shoppingCart = shoppingCart;
        }

    }

    static class Service2 extends BaseService {

        private final ShoppingCart shoppingCart;

        @Inject
        Service2(ShoppingCart shoppingCart) {
            this.shoppingCart = shoppingCart;
        }

    }

}



