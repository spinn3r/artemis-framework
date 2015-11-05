package com.spinn3r.artemis.init.guice.misc;

/**
 *
 */
public class DefaultTransactionLog implements TransactionLog {

    @Override
    public void log() {
        System.out.printf( "transaction logged..." );
    }
}
