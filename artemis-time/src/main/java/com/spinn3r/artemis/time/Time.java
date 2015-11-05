package com.spinn3r.artemis.time;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Represents a specific time, since unix epoch.
 */
public class Time extends TimeRange {

    public Time( long unixtime ) {
        this( unixtime, TimeUnit.MILLISECONDS );
    }

    public Time( Date date ) {
        this( date.getTime(), TimeUnit.MILLISECONDS );
    }

    public Time(long time, TimeUnit unit) {
        super( time, unit );
    }

    /**
     * Round the milliseconds to zero which is needed in some situations as
     * our JSON encoder at the time wasn't including milliseconds.
     *
     * @return
     */
    public Time withRoundedMillis() {
        return new Time( (getTimeMillis() / 1000) * 1000, TimeUnit.MILLISECONDS );
    }

    /**
     * Get the time as a Java Date object.
     */
    public Date toDate() {
        return new Date( getTimeMillis() );
    }

    public String toISO8601() {
        return ISO8601.format( toDate() );
    }

    @Override
    public String toString() {
        return "Time{" +
                 "time=" + time +
                 ", unit=" + unit +
                 "}: " + ISO8601.format( toDate() ) ;
    }

}
