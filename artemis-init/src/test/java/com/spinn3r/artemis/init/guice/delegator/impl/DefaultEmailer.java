package com.spinn3r.artemis.init.guice.delegator.impl;

import com.spinn3r.artemis.init.guice.delegator.Emailer;

/**
 *
 */
public class DefaultEmailer implements Emailer {

    @Override
    public void send(String message) {
        System.out.printf( "Message sent: %s\n", message );
    }

}
