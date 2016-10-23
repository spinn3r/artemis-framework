package com.spinn3r.artemis.init;

import com.google.inject.*;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class TestNewBindingsInChildInjector {

    @Test(expected = CreationException.class)
    public void testSingletonsInFirstModule() throws Exception {

        Injector injector = Guice.createInjector(new DefaultShoppingCartModule());

        injector.createChildInjector(new FancyShoppingCartModule());

    }

    static class DefaultShoppingCartModule extends AbstractModule {

        @Override
        protected void configure() {

            bind(ShoppingCart.class).to(DefaultShoppingCart.class);

        }

    }

    static class FancyShoppingCartModule extends AbstractModule {

        @Override
        protected void configure() {

            bind(ShoppingCart.class).to(FancyShoppingCart.class);

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

    static class FancyShoppingCart extends DefaultShoppingCart {

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



