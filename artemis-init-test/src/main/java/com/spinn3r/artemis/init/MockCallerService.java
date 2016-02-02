package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.advertisements.Caller;
import com.spinn3r.artemis.init.advertisements.CallerServiceType;
import com.spinn3r.artemis.init.modular.ModularService;

/**
 *
 */
public class MockCallerService extends BaseService implements ModularService, CallerServiceType {

    @Override
    public void init() {
        advertise( Caller.class, new Caller( "test" ) );
    }

    @Override
    protected void configure() {
        bind( Caller.class ).toInstance( new Caller( "test" ) );
    }
}
