package com.spinn3r.artemis.time;

import java.util.concurrent.TimeUnit;

/**
 * Represents the amount of time that we've been up and online for.
 */
public class Uptime {

    private final Clock clock;
    private final Time epoch;

    public Uptime(Clock clock, Time epoch) {
        this.clock = clock;
        this.epoch = epoch;
    }

    /**
     * Determine the amount of time we've been up and running for.
     *
     * @return
     */
    public long toMillis() {
        return clock.getTime().toMillis() - epoch.toMillis();
    }

    public String format() {
        return new TimeRange( toMillis(), TimeUnit.MILLISECONDS ).format();
    }

    /**
     * The exact we've been running for...
     * @return
     */
    public Time epoch() {
        return epoch;
    }

}
