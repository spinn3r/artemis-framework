package com.spinn3r.artemis.datetime;

import com.spinn3r.artemis.streams.lazy.LazyFunctionStream;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Date time parser to parse ISO8601 in multiple formats.
 */
public class ISO8601DateTimeParser {

    public Optional<ZonedDateTime> parse(String text) {

        if ( text == null )
            return Optional.empty();

        LazyFunctionStream<String,Optional<ZonedDateTime>> lazyFunctionStream = new LazyFunctionStream<>( text );

        return lazyFunctionStream
          .of( this::parseInstant, this::parseWithOffset )
          .map( Supplier::get )
          .filter( Optional::isPresent )
          .map( Optional::get )
          .findFirst();

    }

    private Optional<ZonedDateTime> parseWithOffset(String text) {

        try {
            return Optional.ofNullable( ZonedDateTime.parse( text, DateTimeFormatter.ISO_OFFSET_DATE_TIME ) );
        } catch ( DateTimeParseException e ) {
            // not good to just swallow exceptions this would happen so often
            // an empty value is a better solution.
            return Optional.empty();
        }

    }

    private Optional<ZonedDateTime> parseInstant(String text) {

        try {
            return Optional.ofNullable( ZonedDateTime.parse(text, DateTimeFormatter.ISO_INSTANT) );
        } catch ( DateTimeParseException e ) {
            // not good to just swallow exceptions this would happen so often
            // an empty value is a better solution.
            return Optional.empty();
        }

    }

}
