package com.spinn3r.artemis.init.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.DependencyAndSource;
import com.google.inject.spi.ProvisionListener;
import com.spinn3r.artemis.init.guice.misc.*;
import org.junit.Test;
import org.mockito.internal.matchers.Matches;

import java.util.List;

/**
 *
 */
public class TestListeners {

    @Test
    public void test1() throws Exception {

        Injector injector = Guice.createInjector( new BillingModule(), new ListenModule());

        BillingService billingService = injector.getInstance( BillingService.class );

        billingService.charge();

        // TODO: the graph is inverted... the CreditCardProcessor.class is the interface that requires PaypalCreditCardProcessor
        //
        // another way I could

    }


    class BillingModule extends AbstractModule {

        @Override
        protected void configure() {

            bind(TransactionLog.class ).toInstance( new DefaultTransactionLog() );
            bind(CreditCardProcessor.class ).to( PaypalCreditCardProcessor.class );

        }

    }

    class ListenModule extends AbstractModule {

        @Override
        protected void configure() {

            bindListener(Matchers.any(), new ProvisionListener() {

                @Override
                public <T> void onProvision(ProvisionInvocation<T> provision) {
                    System.out.printf("Got provision for binding: %s\n", provision.getBinding());

                    List<DependencyAndSource> dependencyChain = provision.getDependencyChain();

                    System.out.printf("\tDependency chain: \n");

                    for (DependencyAndSource dependencyAndSource : dependencyChain) {
                        System.out.printf("\t\t%s\n", dependencyAndSource);
                    }

                }

            });

        }

    }

}
