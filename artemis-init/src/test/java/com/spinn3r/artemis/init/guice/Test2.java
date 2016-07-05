package com.spinn3r.artemis.init.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.spinn3r.artemis.init.Advertised;
import com.spinn3r.artemis.init.guice.misc.*;
import org.junit.Test;

/**
 *
 */
public class Test2 {

    @Test
    public void test2() throws Exception {

        Advertised advertised = new Advertised();

        advertised.advertise( this, CreditCardProcessor.class, PaypalCreditCardProcessor.class );
        advertised.advertise( this, TransactionLog.class, new DefaultTransactionLog() );

        Injector injector = Guice.createInjector( advertised.toModule() );

        injector.getInstance( BillingService.class );

    }

}
