package com.spinn3r.artemis.datetime.rfc822;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class RFC822PartialDateTimeParserTest {

    // FIXME: test continental US time zone abbreviations... UTC, GMT,

    // Mon, 25 Dec 1995 13:30:00 +0430


    @Test
    public void testParse() throws Exception {
        assertEquals( "2008-06-03T11:05:30Z", RFC822PartialDateTimeParser.parse( "Tue, 3 Jun 2008 11:05:30 GMT" ).get().toISO8601() );
    }

}