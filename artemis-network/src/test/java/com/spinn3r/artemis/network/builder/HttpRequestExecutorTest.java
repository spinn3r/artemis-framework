package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.time.init.SyntheticClockService;
import org.junit.Before;
import org.junit.Test;

import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HttpRequestExecutorTest extends BaseLauncherTest {

    @Inject
    HttpRequestExecutorFactory httpRequestExecutorFactory;

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp( SyntheticClockService.class,
                     DirectNetworkService.class );
    }

    @Test
    public void testBasicRequestWithoutExceptions() throws Exception {

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        HttpRequest httpRequest
          = httpRequestExecutor.execute( () -> httpRequestBuilder.get( "https://httpbin.org/status/200" ).execute().connect() );

        assertEquals( 200, httpRequest.getResponseCode() );
        assertEquals( 0, httpRequestExecutor.getRetries() );

    }

    @Test
    public void test404NotFound() throws Exception {

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        NetworkException cause = null;
        HttpRequest httpRequest = null;

        try {
            httpRequest = httpRequestExecutor.execute( () -> httpRequestBuilder.get( "https://httpbin.org/status/404" ).execute().connect() );
        } catch ( NetworkException ne ) {
            cause = ne;
        }

        assertEquals( 0, httpRequestExecutor.getRetries() );
        assertNotNull( cause );
        assertEquals( 404, cause.getResponseCode() );

    }

    @Test
    public void test500() throws Exception {

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        NetworkException cause = null;
        HttpRequest httpRequest = null;

        try {
            httpRequest = httpRequestExecutor.execute( () -> httpRequestBuilder.get( "https://httpbin.org/status/500" ).execute().connect() );
        } catch ( NetworkException ne ) {
            cause = ne;
        }

        assertEquals( 5, httpRequestExecutor.getRetries() );
        assertNotNull( cause );
        assertEquals( 500, cause.getResponseCode() );

    }

    @Test
    public void test302() throws Exception {

        //TODO: This test should set the cookies in one of the redirects instead of the final resource
        String redirection = "http://httpbin.org/cookies/set?name=test";
        String encode = URLEncoder.encode(redirection, "UTF-8");
        String resource = "https://httpbin.org/redirect-to?url=" + encode;

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        HttpRequest httpRequest = httpRequestExecutor.execute(() -> httpRequestBuilder.get(resource).execute().connect());

        assertEquals(200, httpRequest.getResponseCode());
        assertEquals(0, httpRequestExecutor.getRetries());
        assertEquals("test", httpRequest.getCookies().get("name"));

    }

}