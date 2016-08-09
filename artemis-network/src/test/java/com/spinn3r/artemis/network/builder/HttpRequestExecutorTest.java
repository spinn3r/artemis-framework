package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.time.SyntheticClock;
import com.spinn3r.artemis.time.init.SyntheticClockService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class HttpRequestExecutorTest extends BaseLauncherTest {

    @Inject
    HttpRequestExecutorFactory httpRequestExecutorFactory;

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp( SyntheticClockService.class,
                     DirectNetworkService.class
//                     DefaultWebserverReferencesService.class,
//                     WebserverService.class
        );
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

}
