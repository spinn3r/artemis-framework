package com.spinn3r.artemis.network;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.init.DirectNetworkService;

@Ignore
public class ReadTimeoutTest extends BaseLauncherTest {

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp( DirectNetworkService.class ) ;
    }

    @Test
    public void readTimeoutTest() throws Exception {
        // Give a very short read timeout to force a timeout
        try {
            httpRequestBuilder
                .get("http://www.spinn3r.com")
                .withReadTimeout(25)
                .execute()
                .getContentWithEncoding();
        } catch (NetworkException e) {
            assertEquals("http://www.spinn3r.com: Read timed out (25 ms)", e.getMessage());
        }
        
    }

    @Test
    public void connectTimeoutTest() throws Exception {
        // Give a very short connect timeout to force a timeout
        try {
            httpRequestBuilder
                .get("http://www.spinn3r.com")
                .withConnectTimeout(10)
                .execute()
                .getContentWithEncoding();
        } catch (NetworkException e) {
            assertEquals("http://www.spinn3r.com: connect timed out (10 ms)", e.getMessage());
        }
        
    }
}
