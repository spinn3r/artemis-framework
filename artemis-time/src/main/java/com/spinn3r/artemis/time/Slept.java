package com.spinn3r.artemis.time;

import java.util.concurrent.TimeUnit;

/**
 * Used to track for how long we've slept.
 */
public class Slept {

    private long sleepFor;

    private TimeUnit timeUnit;

    public Slept(long sleepFor, TimeUnit timeUnit) {
        this.sleepFor = sleepFor;
        this.timeUnit = timeUnit;
    }

    public long getSleepFor() {
        return sleepFor;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public String toString() {
        return "Slept{" +
                 "sleepFor=" + sleepFor +
                 ", timeUnit=" + timeUnit +
                 '}';
    }

}
