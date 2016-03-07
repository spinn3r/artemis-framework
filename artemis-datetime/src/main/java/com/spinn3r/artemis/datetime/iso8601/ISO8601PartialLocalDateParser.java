package com.spinn3r.artemis.datetime.iso8601;

import com.spinn3r.artemis.datetime.LocalDates;
import com.spinn3r.artemis.datetime.partial.PartialDateTime;
import com.spinn3r.artemis.datetime.partial.PartialDateTimes;
import com.spinn3r.artemis.datetime.partial.decorators.LocalDatePartialDateTimeDecorator;
import com.spinn3r.artemis.streams.lazy.LazyFunctionStream;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Parse ISO8601 timestamps which are partial and lack time or timezone.
 */
public class ISO8601PartialLocalDateParser {

    public static Optional<PartialDateTime> parse(String text) {

        if ( text == null )
            return Optional.empty();

        LazyFunctionStream<String,Optional<LocalDate>> lazyFunctionStream = new LazyFunctionStream<>( text );

        return lazyFunctionStream
                 .of( ISO8601PartialLocalDateParser::parseLocalDate, ISO8601PartialLocalDateParser::parseOffsetDate )
                 .map( Supplier::get )
                 .filter( Optional::isPresent )
                 .map( Optional::get )
                 .findFirst()
                 .map( PartialDateTimes::forLocalDate );

    }

    public static Optional<LocalDate> parseLocalDate(String text) {
        return LocalDates.parseOptionally( text, DateTimeFormatter.ISO_LOCAL_DATE );
    }

    public static Optional<LocalDate> parseOffsetDate(String text) {
        return LocalDates.parseOptionally( text, DateTimeFormatter.ISO_OFFSET_DATE );
    }

}
