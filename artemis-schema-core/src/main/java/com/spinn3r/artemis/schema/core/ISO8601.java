package com.spinn3r.artemis.schema.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 */
public class ISO8601 {

    public static TimeZone UTC = TimeZone.getTimeZone("UTC");

    private static String format( Date date ) {

        // NOTE: Simple Date Format is NOT thread safe.
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone( UTC );
        return df.format( date );

    }

    public static Date parse(String value) throws ParseException {
        //TODO: consider using ISO8601DateParser in the future for better
        //performance
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone( UTC );
        return format.parse( value );
    }

}
