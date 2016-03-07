package com.spinn3r.artemis.datetime.iso8601;

import com.spinn3r.artemis.datetime.LocalDateTimes;
import com.spinn3r.artemis.datetime.partial.PartialDateTime;
import com.spinn3r.artemis.datetime.partial.PartialDateTimes;
import com.spinn3r.artemis.streams.lazy.LazyFunctionStream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Parse ISO8601 timestamps which are partial and lack time or timezone.
 */
public class ISO8601PartialLocalDateTimeParser {

    private static final DateTimeFormatter ISO_INSTANT_WITH_HOURS_MINUTES = DateTimeFormatter.ofPattern( "yyyy-MM-dd'T'HH:mm" );
    private static final DateTimeFormatter ISO_INSTANT_WITH_HOURS_MINUTES_SECONDS_MILLIS = DateTimeFormatter.ofPattern( "yyyy-MM-dd'T'HH:mm:ss.SSS" );

    // TODO: there are other ISO time formats here including:
    // with and without seconds

    public static Optional<PartialDateTime> parse(String text) {

        if ( text == null )
            return Optional.empty();

        LazyFunctionStream<String,Optional<LocalDateTime>> lazyFunctionStream = new LazyFunctionStream<>( text );

        return lazyFunctionStream
                 .of( ISO8601PartialLocalDateTimeParser::parseWithLocalDatetime,
                      ISO8601PartialLocalDateTimeParser::parseWithHoursMinutes,
                      ISO8601PartialLocalDateTimeParser::parseWithHoursMinutesSecondsMillis )
                 .map( Supplier::get )
                 .filter( Optional::isPresent )
                 .map( Optional::get )
                 .findFirst()
                 .map( PartialDateTimes::forLocalDateTime );

    }


    public static Optional<LocalDateTime> parseWithLocalDatetime(String text) {

        return LocalDateTimes.parseOptionally( text, DateTimeFormatter.ISO_LOCAL_DATE_TIME );

    }

    public static Optional<LocalDateTime> parseWithHoursMinutes(String text) {

        return LocalDateTimes.parseOptionally( text, ISO_INSTANT_WITH_HOURS_MINUTES );

    }

    public static Optional<LocalDateTime> parseWithHoursMinutesSecondsMillis(String text) {

        return LocalDateTimes.parseOptionally( text, ISO_INSTANT_WITH_HOURS_MINUTES_SECONDS_MILLIS );

    }

}
