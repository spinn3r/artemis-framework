package com.spinn3r.artemis.init.guice;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.*;
import com.spinn3r.artemis.init.AutoService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 *
 */
@Ignore
public class TestAutoService {

    @Test
    public void test1() throws Exception {

        ListenModule listenModule = new ListenModule();

        Injector injector = Guice.createInjector(listenModule, new ShoppingCartModule());

        // FIXME: use an injection listener here...

        ShoppingCart instance = injector.getInstance(ShoppingCart.class);

        for (AutoService autoService : listenModule.autoServices) {
            autoService.start();
        }

        DefaultShoppingCart defaultShoppingCart = injector.getInstance(DefaultShoppingCart.class);
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

        public DefaultShoppingCart() {
            System.out.printf("here\n");
        }

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

    static class ListenModule extends AbstractModule {

        List<AutoService> autoServices = Lists.newArrayList();

        @Override
        protected void configure() {

            bindListener(Matchers.any(), new TypeListener() {
                @Override
                public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                    encounter.register(new InjectionListener<I>() {
                        @Override
                        public void afterInjection(I injectee) {

                            System.out.printf("GOT INJECTION OF %s\n", injectee.getClass().getName());

                            if ( injectee instanceof AutoService) {
                                AutoService autoService = (AutoService) injectee;
                                autoServices.add(autoService);
                            }

                        }
                    });

                }
            });

        }

        public ImmutableList<AutoService> getAutoServices() {
            return ImmutableList.copyOf(autoServices);
        }

    }

}
