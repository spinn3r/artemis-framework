package com.spinn3r.artemis.datetime;

import com.spinn3r.artemis.datetime.iso8601.ISO8601DateTimeParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ISO8601DateTimeParserTest {

    ISO8601DateTimeParser iso8601DateTimeParser = new ISO8601DateTimeParser();

    @Test
    public void testWithZoneOffset() throws Exception {

        assertEquals( "2011-12-03T10:15:30+01:00", iso8601DateTimeParser.parse( "2011-12-03T10:15:30+01:00" ).get().toString() );

    }

    @Test
    public void testWithISOInstant() throws Exception {

        assertEquals( "2011-12-03T10:15:30Z", iso8601DateTimeParser.parse( "2011-12-03T10:15:30Z" ).get().toString() );

    }

    @Test
    public void testWithBrokenInput() throws Exception {

        assertFalse( iso8601DateTimeParser.parse( "broken" ).isPresent() );

    }

}