package com.spinn3r.artemis.init.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.spinn3r.artemis.init.AutoService;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
@Ignore
public class TestAutoService {

    @Test
    @Ignore
    public void testBasicUsage() throws Exception {

        // FIXME: there is a problem here - the injector only sees the instance
        // AFTER we get the instance...

        Launcher launcher = Launcher.newBuilder()
                                    .setConfigLoader(new ResourceConfigLoader())
                                    .setModule(new ShoppingCartModule())
                                    .build();
        launcher.launch();

        ShoppingCart instance = launcher.getInstance(ShoppingCart.class);

        DefaultShoppingCart defaultShoppingCart = launcher.getInstance(DefaultShoppingCart.class);
        Assert.assertTrue(defaultShoppingCart.started);

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

        boolean started = false;

        @Override
        public void checkout() {

        }

        @Override
        public void start() throws Exception {
            started = true;
        }

        @Override
        public void stop() throws Exception {

        }

    }

}
