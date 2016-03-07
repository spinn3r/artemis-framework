package com.spinn3r.artemis.datetime;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

/**
 *
 */
public class ZonedDateTimes {

    public static Date toDate( ZonedDateTime zonedDateTime ) {
        return Date.from( zonedDateTime.toInstant() );
    }

    public static Optional<Date> toDate( Optional<ZonedDateTime> zonedDateTime ) {

        if ( zonedDateTime.isPresent() ) {
            return Optional.of( toDate( zonedDateTime.get() ) );
        }

        return Optional.empty();

    }

}
