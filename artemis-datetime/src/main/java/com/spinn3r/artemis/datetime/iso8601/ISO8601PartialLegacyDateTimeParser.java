package com.spinn3r.artemis.datetime.iso8601;

import com.spinn3r.artemis.datetime.partial.PartialDateTime;
import com.spinn3r.artemis.datetime.partial.PartialDateTimes;
import com.spinn3r.artemis.time.ISO8601DateParser;

import java.util.Optional;

/**
 * Legacy support for our old ISO8601 date /time parser.  This parser has
 * some extra features including liberal support for timezone specification.
 * Which we need to eventually build into our modern parsers.
 */
public class ISO8601PartialLegacyDateTimeParser {

    public static Optional<PartialDateTime> parse(String text) {

        if ( text == null ) {
            return Optional.empty();
        }

        return Optional.ofNullable( ISO8601DateParser.parse( text ) )
          .map( PartialDateTimes::forDate );

    }

}
