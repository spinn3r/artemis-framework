package com.spinn3r.artemis.client.json;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class BaseContentTest {

    @Test
    public void testWithUnderScore() throws Exception {

        Date dateFound = new Date( 1403399332000L );

        Content c0 = new Content();
        c0.setDateFound( dateFound );

        String json = c0.asJSON().toJSON();

        assertEquals( "{\n" +
                        "  \"date_found\" : \"2014-06-22T01:08:52Z\"\n" +
                        "}", json );

        Content c1 = Content.fromJSON( json );

        assertNotNull( c1.dateFound );

        assertEquals( c0.getDateFound(), c1.getDateFound() );

    }

}