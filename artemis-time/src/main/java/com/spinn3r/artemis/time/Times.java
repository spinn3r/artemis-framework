package com.spinn3r.artemis.time;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * References to various times.
 */
public class Times {

    public static Time epoch() {
        return new Time( 0, TimeUnit.MILLISECONDS );
    }

    public static Time toTime( Date date ) {
        return new Time( date.getTime(), TimeUnit.MILLISECONDS );
    }

}
