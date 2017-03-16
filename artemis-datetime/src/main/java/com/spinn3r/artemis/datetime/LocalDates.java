package com.spinn3r.artemis.datetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

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

    public static Stream<LocalDate> streamRange(LocalDate startDate, LocalDate endDate){

        return Stream.iterate(startDate, d -> d.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1);

    }

}
