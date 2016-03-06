package com.spinn3r.artemis.datetime;

import com.spinn3r.artemis.time.TimeZones;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 *
 */
public class LocalDateTimes {

    public static Date toDate( LocalDateTime localDateTime ) {
        return Date.from( localDateTime.toInstant( ZoneOffset.UTC ) );
    }

}
