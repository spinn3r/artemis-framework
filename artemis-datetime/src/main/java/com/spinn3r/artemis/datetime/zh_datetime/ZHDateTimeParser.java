package com.spinn3r.artemis.datetime.zh_datetime;


import com.spinn3r.artemis.datetime.partial.PartialDateTime;
import com.spinn3r.artemis.datetime.partial.decorators.LocalDateTimePartialDateTimeDecorator;
import com.spinn3r.artemis.streams.optionals.OptionalInts;
import com.spinn3r.artemis.streams.regex.Matchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Date and time parser for
 */
public class ZHDateTimeParser {

    // one of the entries here is an NBSP which is rather confusing.
    private static final Pattern PATTERN = Pattern.compile( "([0-9]{4})年([0-9]{2})月([0-9]{2})日[ \\s]*([0-9]{2}):([0-9]{2})" );

    public static Optional<PartialDateTime> parse(String text ) {

        Matcher matcher = PATTERN.matcher( text );

        if ( matcher.find() ) {

            List<Integer> values = Matchers.groups( matcher )
              .map( OptionalInts::parse )
              .filter( OptionalInt::isPresent )
              .map( OptionalInt::getAsInt )
              .collect( Collectors.toList() );

            if ( values.size() != 5 ) {
                return Optional.empty();
            }

            LocalDateTime localDateTime = LocalDateTime.of( values.get( 0 ),
                                                            values.get( 1 ),
                                                            values.get( 2 ),
                                                            values.get( 3 ),
                                                            values.get( 4 ) );
            return Optional.of( localDateTime )
                     .map( LocalDateTimePartialDateTimeDecorator::new );

        }

        return Optional.empty();

    }

}
