package com.spinn3r.artemis.init.guice.misc;

/**
 *
 */
public class VisaCreditCardProcessor implements CreditCardProcessor {

    @Override
    public void charge() {
        System.out.printf( "charged visa.\n" );
    }

}
