package com.spinn3r.artemis.init.guice.misc;

/**
 *
 */
public class PaypalCreditCardProcessor implements CreditCardProcessor {

    @Override
    public void charge() {
        System.out.printf( "charged paypal.\n" );
    }

}
