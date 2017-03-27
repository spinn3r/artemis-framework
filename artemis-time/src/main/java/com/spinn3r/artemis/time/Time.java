package com.spinn3r.artemis.time;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Represents a specific time, since unix epoch.  A <code>Time</code> is just a
 * <code>TimeRange</code> from unix epoch.
 */
public class Time extends TimeRange {

    private static final ZoneId UTC = ZoneId.of("UTC");

    public Time(long unixtime) {
        this( unixtime, TimeUnit.MILLISECONDS );
    }

    public Time(Date date) {
        this( date.getTime(), TimeUnit.MILLISECONDS );
    }

    public Time(ZonedDateTime zonedDateTime){
        this(Date.from( zonedDateTime.toInstant()));
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
        return new Time((toMillis() / 1000) * 1000, TimeUnit.MILLISECONDS );
    }

    public ZonedDateTime toZonedDateTime() {
        return ZonedDateTime.ofInstant(toDate().toInstant(), UTC);
    }

    /**
     * Get the time as a Java Date object.
     */
    public Date toDate() {
        return new Date(toMillis() );
    }

    public String toISO8601() {
        return ISO8601.format( toDate() );
    }

    @Override
    public String toString() {

        // NOTE: call this time instead of ```duration``` since this is representing
        // a time value not a time range.

        return "Time{" +
                 "time=" + duration +
                 ", timeUnit=" + timeUnit +
                 "}: " + ISO8601.format(toDate()) ;
    }

}
