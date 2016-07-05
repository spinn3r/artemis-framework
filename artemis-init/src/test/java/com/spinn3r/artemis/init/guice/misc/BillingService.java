package com.spinn3r.artemis.init.guice.misc;

import com.google.inject.Inject;

/**
 *
 */
public class BillingService {

    private final CreditCardProcessor processor;

    private final TransactionLog transactionLog;

    @Inject
    BillingService(CreditCardProcessor processor, TransactionLog transactionLog) {
        this.processor = processor;
        this.transactionLog = transactionLog;
    }

    public void charge() {
        processor.charge();
        transactionLog.log();
    }

}
