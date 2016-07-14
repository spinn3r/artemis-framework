package com.spinn3r.artemis.datetime;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

/**
 *
 */
public class ZonedDateTimes {

    private static final ZoneId UTC = ZoneId.of("UTC");

    public static Date toDate(ZonedDateTime zonedDateTime ) {
        return Date.from( zonedDateTime.toInstant() );
    }

    public static ZonedDateTime fromDate(Date date) {
        return ZonedDateTime.ofInstant(date.toInstant(), UTC);
    }

    public static Optional<Date> toDate( Optional<ZonedDateTime> zonedDateTime ) {

        if ( zonedDateTime.isPresent() ) {
            return Optional.of( toDate( zonedDateTime.get() ) );
        }

        return Optional.empty();

    }

    public static Optional<ZonedDateTime> parseOptionally(String text, DateTimeFormatter dateTimeFormatter) {

        try {
            return Optional.ofNullable( ZonedDateTime.parse(text, dateTimeFormatter) );
        } catch ( DateTimeParseException e ) {
            // not good to just swallow exceptions this would happen so often
            // an empty value is a better solution.
            return Optional.empty();
        }

    }

}
