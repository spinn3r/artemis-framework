package com.spinn3r.artemis.time.init;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.time.Clock;
import com.spinn3r.artemis.time.Uptime;

/**
 * Create an Uptime instance to track how long we've been up and online for.
 */
public class UptimeService extends BaseService {

    private final Clock clock;

    @Inject
    public UptimeService(Clock clock) {
        this.clock = clock;
    }

    @Override
    public void init() {
        advertise( Uptime.class, new Uptime( clock, clock.getTime() ) );
    }

}
