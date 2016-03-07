package com.spinn3r.artemis.datetime.iso8601;

import com.spinn3r.artemis.datetime.partial.PartialDateTime;
import com.spinn3r.artemis.streams.lazy.LazyFunctionStream;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * ISO8601 parser which tries all formats of ISO8601
 */
public class ISO8601PartialDateTimeParser {

    public static Optional<PartialDateTime> parse(String text) {

        if ( text == null )
            return Optional.empty();

        LazyFunctionStream<String,Optional<PartialDateTime>> lazyFunctionStream = new LazyFunctionStream<>( text );

        return lazyFunctionStream
                 .of( ISO8601PartialLocalDateParser::parse,
                      ISO8601PartialLocalDateTimeParser::parse,
                      ISO8601PartialZonedDateTimeParser::parse,
                      ISO8601PartialLegacyDateTimeParser::parse)
                 .map( Supplier::get )
                 .filter( Optional::isPresent )
                 .map( Optional::get )
                 .findFirst();

    }


}
