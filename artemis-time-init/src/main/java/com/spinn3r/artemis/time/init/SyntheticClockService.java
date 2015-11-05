package com.spinn3r.artemis.time.init;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.artemis.time.SyntheticClock;
import com.spinn3r.artemis.time.SyntheticClocks;

/**
 *
 */
public class SyntheticClockService extends BaseService {

    @Override
    public void init()  {

        SyntheticClock clock = SyntheticClocks.recentClock();

        advertise( Clock.class, clock );

        // advertise it under synthetic clock too so we can inject the actual
        // mock so we can jump time.
        advertise( SyntheticClock.class, clock );

    }

    @Override
    public void stop() throws Exception {

    }

}
