package com.spinn3r.artemis.init.guice.delegator;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.guice.delegator.impl.AuthenticatingEmailer;
import com.spinn3r.artemis.init.guice.delegator.impl.DefaultEmailer;
import com.spinn3r.artemis.init.guice.delegator.impl.InstrumentingEmailer;
import com.spinn3r.artemis.init.guice.delegator.impl.LoggingEmailer;

/**
 *
 */
public class EmailerDelegator implements Emailer {

    private Emailer delegate;

    @Inject
    public EmailerDelegator(AuthenticatingEmailer emailer0,
                            InstrumentingEmailer emailer1,
                            LoggingEmailer emailer2,
                            DefaultEmailer emailer3) {

        emailer0.setDelegate( emailer1 );
        emailer1.setDelegate( emailer2 );
        emailer2.setDelegate( emailer3 );

        delegate = emailer0;

    }

    @Override
    public void send(String message) {
        delegate.send( message );
    }

}
