package com.spinn3r.artemis.datetime.iso8601;

import com.spinn3r.artemis.datetime.ZonedDateTimes;
import com.spinn3r.artemis.streams.lazy.LazyFunctionStream;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
        return ZonedDateTimes.parseOptionally(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private Optional<ZonedDateTime> parseInstant(String text) {
        return ZonedDateTimes.parseOptionally(text, DateTimeFormatter.ISO_INSTANT);
    }

}
