package com.spinn3r.artemis.init.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.DependencyAndSource;
import com.google.inject.spi.ProvisionListener;
import com.spinn3r.artemis.init.guice.misc.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 *
 */
@Ignore
public class TestReplaceBindings {

    @Test
    public void test1() throws Exception {

        Injector injector = Guice.createInjector( new BillingModule(), new MockBillingModule());

        CreditCardProcessor creditCardProcessor = injector.getInstance(CreditCardProcessor.class);

        Assert.assertEquals(MockCreditCardProcessor.class, creditCardProcessor.getClass());

    }


    class BillingModule extends AbstractModule {

        @Override
        protected void configure() {

            bind(TransactionLog.class ).toInstance( new DefaultTransactionLog() );
            bind(CreditCardProcessor.class ).to( PaypalCreditCardProcessor.class );

        }

    }

    class MockBillingModule extends AbstractModule {

        @Override
        protected void configure() {

            bind(CreditCardProcessor.class ).to( MockCreditCardProcessor.class );

        }

    }

}
