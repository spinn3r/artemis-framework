package com.spinn3r.artemis.time;

import com.google.common.base.Preconditions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Format and parse ISO8601 strings.
 */
public class ISO8601 {

    public static String format(long unixtime) {
        return format( new Date( unixtime ) );
    }

    /**
     * Format a date as ISO8601 using GMT/UTC/Zulu time and include the time
     * including seconds.
     */
    public static String format(Date date) {
        checkNotNull( date );

        // NOTE: Simple Date Format is NOT thread safe.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone( TimeZones.UTC );
        return df.format( date );

    }

    public static Time parse(String value) throws ParseException {
        checkNotNull( value );
        //TODO: consider using ISO8601DateParser
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone( TimeZones.UTC );
        return Times.toTime( format.parse( value ) );
    }

}
