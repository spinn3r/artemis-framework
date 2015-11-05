package com.spinn3r.artemis.time.init;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.artemis.time.SystemClock;

/**
 *
 */
public class SystemClockService extends BaseService {

    @Override
    public void init() {
        advertise( Clock.class, new SystemClock() );
    }

}
