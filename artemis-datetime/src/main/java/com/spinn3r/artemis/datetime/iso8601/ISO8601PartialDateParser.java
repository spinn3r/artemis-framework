package com.spinn3r.artemis.datetime.iso8601;

import com.spinn3r.artemis.datetime.LocalDateTimes;
import com.spinn3r.artemis.datetime.partial.PartialDateTime;
import com.spinn3r.artemis.datetime.partial.PartialLocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Parse ISO8601 timestamps which are partial and lack time or timezone.
 */
public class ISO8601PartialDateParser {

    public Optional<PartialDateTime> parse(String text) {

        return LocalDateTimes.parseOptionally( text, DateTimeFormatter.ISO_LOCAL_DATE_TIME )
                 .map( PartialLocalDateTime::new );

    }

}
