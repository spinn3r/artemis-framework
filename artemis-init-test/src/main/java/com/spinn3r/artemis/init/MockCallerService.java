package com.spinn3r.artemis.init;

import com.spinn3r.artemis.init.advertisements.Caller;
import com.spinn3r.artemis.init.advertisements.Version;

/**
 *
 */
public class MockCallerService extends BaseService {

    @Override
    public void init() {
        advertise( Caller.class, new Caller( "test" ) );
    }

}
