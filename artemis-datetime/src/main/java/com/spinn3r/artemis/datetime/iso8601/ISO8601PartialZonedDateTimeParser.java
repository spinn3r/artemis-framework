package com.spinn3r.artemis.datetime.iso8601;

import com.spinn3r.artemis.datetime.ZonedDateTimes;
import com.spinn3r.artemis.datetime.partial.PartialDateTime;
import com.spinn3r.artemis.datetime.partial.PartialDateTimes;
import com.spinn3r.artemis.datetime.partial.decorators.ZonedDateTimePartialDateTimeDecorator;
import com.spinn3r.artemis.streams.lazy.LazyFunctionStream;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Date time parser to parse ISO8601 in multiple formats.
 */
public class ISO8601PartialZonedDateTimeParser {

    // TODO: there are other ISO time formats here including:
    //
    // with and without seconds
    // with and without milliseconds

    public static Optional<PartialDateTime> parse(String text) {

        if ( text == null )
            return Optional.empty();

        LazyFunctionStream<String,Optional<ZonedDateTime>> lazyFunctionStream = new LazyFunctionStream<>( text );

        return lazyFunctionStream
          .of( ISO8601PartialZonedDateTimeParser::parseInstant, ISO8601PartialZonedDateTimeParser::parseWithOffset )
          .map( Supplier::get )
          .filter( Optional::isPresent )
          .map( Optional::get )
          .findFirst()
          .map( PartialDateTimes::forZonedDateTime );

    }

    private static Optional<ZonedDateTime> parseWithOffset(String text) {
        return ZonedDateTimes.parseOptionally(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private static Optional<ZonedDateTime> parseInstant(String text) {
        return ZonedDateTimes.parseOptionally(text, DateTimeFormatter.ISO_INSTANT);
    }

}
