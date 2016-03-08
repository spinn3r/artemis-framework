package com.spinn3r.artemis.datetime.misc;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Optional;

import com.spinn3r.artemis.datetime.LocalDateTimes;
import com.spinn3r.artemis.datetime.LocalDates;
import com.spinn3r.artemis.datetime.partial.PartialDateTime;
import com.spinn3r.artemis.datetime.partial.decorators.LocalDateTimePartialDateTimeDecorator;


public class PaddedDateTimeParser {

    private static final DateTimeFormatter PADDED_FORMATTER;

    static {
        DateTimeFormatter dateFormatter = new DateTimeFormatterBuilder()
                .appendValue(YEAR, 4)
                .appendLiteral('-')
                .appendValue(MONTH_OF_YEAR, 2)
                .appendLiteral('-')
                .appendValue(DAY_OF_MONTH, 2).toFormatter();
        
        DateTimeFormatter timeFormatter = new DateTimeFormatterBuilder()
                .appendLiteral(' ')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .appendOptional(
                    new DateTimeFormatterBuilder()
                        .appendLiteral(':')
                        .appendValue(SECOND_OF_MINUTE, 2)
                        .toFormatter()) 
                .toFormatter();
        
        DateTimeFormatter timezoneFormatter = new DateTimeFormatterBuilder()
                .appendLiteral(' ')
                .appendPattern("[z]Z").toFormatter();
                
        PADDED_FORMATTER = new DateTimeFormatterBuilder()
                .append(dateFormatter)
                .appendOptional(timeFormatter)
                .appendOptional(timezoneFormatter)
                .toFormatter();
        
    }

    public static Optional<PartialDateTime> parse(String text) {
        if(text == null) {
            return Optional.empty();
        }        
        
        Optional<LocalDateTime> parsedDateTime;
        if(text.length() == 10) { // just date here
            parsedDateTime = LocalDates.parseOptionally(text, PADDED_FORMATTER)
                                .map((date) -> LocalDateTime.of(date, LocalTime.MIDNIGHT));
        } else {
            parsedDateTime = LocalDateTimes.parseOptionally(text, PADDED_FORMATTER);
        }
        return parsedDateTime.map(LocalDateTimePartialDateTimeDecorator::new);
    }

}
