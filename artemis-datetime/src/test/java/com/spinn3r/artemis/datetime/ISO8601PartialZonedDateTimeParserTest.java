package com.spinn3r.artemis.datetime;

import com.spinn3r.artemis.datetime.iso8601.ISO8601PartialZonedDateTimeParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ISO8601PartialZonedDateTimeParserTest {

    ISO8601PartialZonedDateTimeParser iso8601PartialZonedDateTimeParser = new ISO8601PartialZonedDateTimeParser();

    @Test
    public void testWithZoneOffset() throws Exception {

        assertEquals( "2011-12-03T10:15:30+01:00", iso8601PartialZonedDateTimeParser.parse( "2011-12-03T10:15:30+01:00" ).get().toString() );

    }

    @Test
    public void testWithISOInstant() throws Exception {

        assertEquals( "2011-12-03T10:15:30Z", iso8601PartialZonedDateTimeParser.parse( "2011-12-03T10:15:30Z" ).get().toString() );

    }

    @Test
    public void testWithBrokenInput() throws Exception {

        assertFalse( iso8601PartialZonedDateTimeParser.parse( "broken" ).isPresent() );

    }

}