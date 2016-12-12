package com.spinn3r.artemis.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

/**
 *
 */
public class LocalDates {

    public static YearMonthDay toYearMonthDay(LocalDate localDate) {
        return new YearMonthDay(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
    }

    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay( ZoneOffset.UTC ).toInstant());
    }

    public static Optional<LocalDate> parseOptionally(String text, DateTimeFormatter dateTimeFormatter) {

        try {
            return Optional.ofNullable( LocalDate.parse(text, dateTimeFormatter) );
        } catch ( DateTimeParseException e ) {
            // not good to just swallow exceptions this would happen so often
            // an empty value is a better solution.
            return Optional.empty();
        }

    }

}
