package com.spinn3r.artemis.init.guice;

import com.google.inject.*;
import com.spinn3r.artemis.init.guice.misc.*;
import org.junit.Test;

/**
 *
 */
public class Test1 {

    @Test
    public void test1() throws Exception {

        Injector injector = Guice.createInjector( new BillingModule() );

        BillingService billingService = injector.getInstance( BillingService.class );

        billingService.charge();

    }


    class BillingModule extends AbstractModule {

        @Override
        protected void configure() {

            bind(TransactionLog.class ).toInstance( new DefaultTransactionLog() );
            bind( CreditCardProcessor.class ).toInstance( new PaypalCreditCardProcessor() );

        }

    }

}
