package com.spinn3r.artemis.datetime;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

/**
 *
 */
public class LocalDateTimes {

    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from( localDateTime.toInstant( ZoneOffset.UTC ) );
    }

    public static Optional<LocalDateTime> parseOptionally(String text, DateTimeFormatter dateTimeFormatter) {

        try {
            return Optional.ofNullable( LocalDateTime.parse(text, dateTimeFormatter) );
        } catch ( DateTimeParseException e ) {
            // not good to just swallow exceptions this would happen so often
            // an empty value is a better solution.
            return Optional.empty();
        }

    }

}
