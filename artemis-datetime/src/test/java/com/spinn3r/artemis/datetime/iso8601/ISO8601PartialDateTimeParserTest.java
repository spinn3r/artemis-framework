package com.spinn3r.artemis.datetime.iso8601;

import com.spinn3r.artemis.time.ISO8601DateParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ISO8601PartialDateTimeParserTest {

    // there last 3-4 parts are optional for us..
    //
    // time
    // zone
    // seconds
    // milliseconds.

    // we should test the cartesian product / powerset of these

    @Test
    public void testParseStandardWithZone() throws Exception {
        assertEquals( "Optional[2011-12-03T10:15:30Z]", ISO8601PartialDateTimeParser.parse( "2011-12-03T10:15:30Z" ).toString() );
    }

    @Test
    public void testParseStandardWithUTCZone() throws Exception {
        assertEquals( "Optional[2011-12-03T10:15:30Z]", ISO8601PartialDateTimeParser.parse( "2011-12-03T10:15:30UTC" ).toString() );
    }

    @Test
    public void testParseStandardWithoutZone() throws Exception {
        assertEquals( "Optional[2011-12-03T10:15:30]", ISO8601PartialDateTimeParser.parse( "2011-12-03T10:15:30" ).toString() );
    }

    @Test
    public void testParseStandardWithoutTime() throws Exception {
        assertEquals( "Optional[2011-12-03]", ISO8601PartialDateTimeParser.parse( "2011-12-03" ).toString() );
    }

    @Test
    public void testParseStandardWithoutTimeButWithZone() throws Exception {
        assertEquals( "Optional[2011-12-03]", ISO8601PartialDateTimeParser.parse( "2011-12-03Z" ).toString() );
    }

    @Test
    public void testParseStandardWithMillisAndZone() throws Exception {
        assertEquals( "Optional[2011-12-03T01:01:01.001Z]", ISO8601PartialDateTimeParser.parse( "2011-12-03T01:01:01.001Z" ).toString() );
        assertEquals( "Optional[2011-12-03T01:01:01.001+01:00]", ISO8601PartialDateTimeParser.parse( "2011-12-03T01:01:01.001+01:00" ).toString() );
    }

    @Test
    public void testParseStandardWithMillis() throws Exception {
        assertEquals( "Optional[2011-12-03T01:01:01.001]", ISO8601PartialDateTimeParser.parse( "2011-12-03T01:01:01.001" ).toString() );
    }

    @Test
    public void testParseWithDateTimeHoursMinutes() throws Exception {
        assertEquals( "Optional[2011-12-03T01:01]", ISO8601PartialDateTimeParser.parse( "2011-12-03T01:01" ).toString() );
    }

}