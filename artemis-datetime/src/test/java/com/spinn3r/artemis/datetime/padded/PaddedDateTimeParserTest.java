package com.spinn3r.artemis.datetime.padded;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PaddedDateTimeParserTest {

    @Test
    public void testParse() throws Exception {
        assertEquals("2016-02-27T05:28:16", PaddedDateTimeParser.parse("2016-02-27 05:28:16 -0500").get().toString());
        assertEquals("2016-02-27T05:28", PaddedDateTimeParser.parse("2016-02-27 05:28 -0500").get().toString());
        assertEquals("2016-02-27T05:28", PaddedDateTimeParser.parse("2016-02-27 05:28").get().toString());
        assertEquals("2016-02-27T00:00", PaddedDateTimeParser.parse("2016-02-27").get().toString());
        assertEquals("2016-02-27T05:28:16", PaddedDateTimeParser.parse("2016-02-27 05:28:16 Z-0500").get().toString());
        assertEquals("2016-02-27T05:28:16", PaddedDateTimeParser.parse("2016-02-27 05:28:16 UTC-0500").get().toString());
        assertEquals("2016-02-27T05:28:16", PaddedDateTimeParser.parse("2016-02-27 05:28:16 PST-0500").get().toString());
        assertEquals("2016-02-27T00:00:01", PaddedDateTimeParser.parse("2016-02-27 00:00:01 EST-0500").get().toString());
        assertEquals("2016-02-27T05:28:16", PaddedDateTimeParser.parse("2016-02-27 05:28:16 GMT-0500").get().toString());

    }

}
