package com.spinn3r.artemis.datetime.iso8601;

import com.spinn3r.artemis.time.ISO8601DateParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class ISO8601PartialDateTimeParserTest {

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

}