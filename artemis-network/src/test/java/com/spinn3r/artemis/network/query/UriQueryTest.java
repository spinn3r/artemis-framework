package com.spinn3r.artemis.network.query;

import org.junit.Test;

import static com.spinn3r.artemis.network.query.UriQuery.uri;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class UriQueryTest {

    @Test
    public void testDomain() throws Exception {

        assertEquals("cnn.com", uri("http://www.cnn.com").domain().get());
        assertEquals("cnn.com", uri("http://cnn.com").domain().get());

    }

}