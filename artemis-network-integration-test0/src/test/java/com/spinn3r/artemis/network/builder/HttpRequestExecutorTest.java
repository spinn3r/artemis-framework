package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.http.init.DefaultWebserverReferencesService;
import com.spinn3r.artemis.http.init.WebserverPort;
import com.spinn3r.artemis.http.init.WebserverService;
import com.spinn3r.artemis.http.servlets.evaluate.ResponseDescriptor;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.metrics.init.MetricsService;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.time.init.SyntheticClockService;
import com.spinn3r.artemis.time.init.UptimeService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpRequestExecutorTest extends BaseLauncherTest {

    @Inject
    HttpRequestExecutorFactory httpRequestExecutorFactory;

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Inject
    WebserverPort webserverPort;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp( SyntheticClockService.class,
                     MockHostnameService.class,
                     MockVersionService.class,
                     UptimeService.class,
                     MetricsService.class,
                     DirectNetworkService.class,
                     DefaultWebserverReferencesService.class,
                     WebserverService.class);
    }

    @Test
    public void testBasicRequestWithoutExceptions() throws Exception {

        String url = new ResponseDescriptor.Builder()
                             .withStatus(200)
                             .build()
                             .toURL("localhost", webserverPort.getPort());

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        HttpRequest httpRequest
          = httpRequestExecutor.execute( () -> httpRequestBuilder.get( url ).execute().connect() );

        assertEquals( 200, httpRequest.getResponseCode() );
        assertEquals( 0, httpRequestExecutor.getRetries() );

    }

    @Test
    public void test404NotFound() throws Exception {

        String url = new ResponseDescriptor.Builder()
                       .withStatus(404)
                       .build()
                       .toURL("localhost", webserverPort.getPort());

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        NetworkException cause = null;
        HttpRequest httpRequest = null;

        try {
            httpRequest = httpRequestExecutor.execute( () -> httpRequestBuilder.get( url ).execute().connect() );
        } catch ( NetworkException ne ) {
            cause = ne;
        }

        assertEquals( 0, httpRequestExecutor.getRetries() );
        assertNotNull( cause );
        assertEquals( 404, cause.getResponseCode() );

    }
    @Test
    public void test503() throws Exception {

        String url = new ResponseDescriptor.Builder()
                       .withStatus(503)
                       .build()
                       .toURL("localhost", webserverPort.getPort());

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        NetworkException cause = null;
        HttpRequest httpRequest = null;

        try {
            httpRequest = httpRequestExecutor.execute( () -> httpRequestBuilder.get( url ).execute().connect() );
        } catch ( NetworkException ne ) {
            cause = ne;
        }

        assertEquals( 5, httpRequestExecutor.getRetries() );
        assertNotNull( cause );
        assertEquals( 503, cause.getResponseCode() );

    }

    @Test
    public void test500() throws Exception {

        String url = new ResponseDescriptor.Builder()
                       .withStatus(500)
                       .build()
                       .toURL("localhost", webserverPort.getPort());

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        NetworkException cause = null;
        HttpRequest httpRequest = null;

        try {
            httpRequest = httpRequestExecutor.execute( () -> httpRequestBuilder.get( url ).execute().connect() );
        } catch ( NetworkException ne ) {
            cause = ne;
        }

        assertEquals( 5, httpRequestExecutor.getRetries() );
        assertNotNull( cause );
        assertEquals( 500, cause.getResponseCode() );

    }

}
