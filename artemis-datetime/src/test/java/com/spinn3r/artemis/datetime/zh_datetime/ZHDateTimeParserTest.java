package com.spinn3r.artemis.datetime.zh_datetime;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ZHDateTimeParserTest {

    @Test
    public void testParse() throws Exception {
        ZHDateTimeParser zhDateTimeParser = new ZHDateTimeParser();
        assertEquals( "2016-03-05T17:59", zhDateTimeParser.parse( "2016年03月05日 17:59" ).get().toString() );
        assertEquals( "2016-03-03T09:09", zhDateTimeParser.parse( "2016年03月03日09:09" ).get().toString() );
    }

}