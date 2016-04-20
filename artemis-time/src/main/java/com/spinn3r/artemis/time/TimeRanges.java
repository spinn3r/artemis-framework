package com.spinn3r.artemis.time;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class TimeRanges {

    public static TimeRange millis( long millis ) {
        return new TimeRange( millis, TimeUnit.MILLISECONDS );
    }

    /**
     * Compute the amount of time between these two dates.
     * @param d0
     * @param d1
     * @return
     */
    public static TimeRange delta( Date d0, Date d1 ) {
        long delta = Math.abs( d0.getTime() - d1.getTime() );
        return new TimeRange( delta, TimeUnit.MILLISECONDS );
    }

}
