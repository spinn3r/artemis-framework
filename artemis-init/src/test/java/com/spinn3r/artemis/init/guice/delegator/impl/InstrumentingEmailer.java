package com.spinn3r.artemis.init.guice.delegator.impl;

import com.spinn3r.artemis.init.delegate.BaseDelegatable;
import com.spinn3r.artemis.init.guice.delegator.Emailer;

/**
 *
 */
public class InstrumentingEmailer extends BaseDelegatable<Emailer> implements Emailer {

    @Override
    public void send(String message) {
        delegate.send( message );
    }

}
