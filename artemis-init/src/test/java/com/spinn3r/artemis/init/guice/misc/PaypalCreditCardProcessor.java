package com.spinn3r.artemis.init.guice.misc;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 *
 */
@Singleton
public class PaypalCreditCardProcessor implements CreditCardProcessor {

    private final TransactionLog transactionLog;

    @Inject
    PaypalCreditCardProcessor(TransactionLog transactionLog) {
        this.transactionLog = transactionLog;
    }

    @Override
    public void charge() {
        System.out.printf( "charged paypal.\n" );
    }

}
