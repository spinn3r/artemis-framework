package com.spinn3r.artemis.datetime.rfc822;

import com.spinn3r.artemis.datetime.LocalDateTimes;
import com.spinn3r.artemis.datetime.ZonedDateTimes;
import com.spinn3r.artemis.datetime.partial.PartialDateTime;
import com.spinn3r.artemis.datetime.partial.PartialDateTimes;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 *
 */
public class RFC822PartialDateTimeParser {

    public static Optional<PartialDateTime> parse(String text) {
        return ZonedDateTimes.parseOptionally( text, DateTimeFormatter.RFC_1123_DATE_TIME ).map( PartialDateTimes::forZonedDateTime );
    }

}

