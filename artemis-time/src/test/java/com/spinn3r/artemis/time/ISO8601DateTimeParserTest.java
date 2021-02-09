package com.spinn3r.artemis.time;

import org.joda.time.DateTimeZone;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ISO8601DateTimeParserTest {

    @Test
    public void testYearMonthDay() throws Exception {
        DateTimeZone dateTimeZone = DateTimeZone.forTimeZone( TimeZones.UTC );

        assertThat( ISO8601DateTimeParser.yearMonthDay( "2015-03-31" ).withZone(dateTimeZone).toString(), startsWith( "2015-03-31T" ) );

    }

    @Test
    @Ignore
    public void testParse() throws Exception {

        // TODO: test with partials.
        // TODO: test with all timezone offsets
        // TODO: test with zulu
        // test with all options like - remove and : removed in the timezone
        assertEquals( "", ISO8601DateTimeParser.parse( "2015-07-25T14:23:57-07:00" ).toString() );

        assertEquals( "", ISO8601DateTimeParser.parse( "20150726T0003+0300Z" ).toString() );

    }

}
