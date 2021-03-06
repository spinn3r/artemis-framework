package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.http.init.DefaultWebserverReferencesService;
import com.spinn3r.artemis.http.init.WebserverPort;
import com.spinn3r.artemis.http.init.WebserverService;
import com.spinn3r.artemis.http.servlets.evaluate.ResponseDescriptor;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.init.resource_mutexes.PortMutex;
import com.spinn3r.artemis.init.resource_mutexes.PortMutexes;
import com.spinn3r.artemis.metrics.init.MetricsService;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;
import com.spinn3r.artemis.network.builder.proxies.ProxyReferences;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.time.init.SyntheticClockService;
import com.spinn3r.artemis.time.init.UptimeService;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.*;

public class HttpRequestExecutorWithProxyTest extends BaseLauncherTest {

    @Inject
    HttpRequestExecutorFactory httpRequestExecutorFactory;

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Inject
    WebserverPort webserverPort;

    @Inject
    PortMutexes portMutexes;

    PortMutex server503Port;

    Server server;


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

        //We use a raw Server here instead of Evaluate Servlet framework because ProxyReference ignores the parameters making Evaluate Servlet to return 200 instead of 503

        server503Port = portMutexes.acquire(10000, 11000);

        server = new Server(server503Port.getPort());
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                response.sendError(503);
            }
        });
        server.start();
        server.dumpStdErr();



    }
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testProxyResponding503() throws Exception {

        String url = new ResponseDescriptor.Builder()
                .withStatus(200)
                .build()
                .toURL("localhost", webserverPort.getPort());

        String proxyUrl = "http://localhost:"+server503Port.getPort();

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        NetworkException cause = null;
        ProxyReference proxy = ProxyReferences.create(proxyUrl );

        try {

            httpRequestExecutor.execute( () -> httpRequestBuilder.get( url ).withProxy(proxy).execute().connect() );
        } catch ( NetworkException ne ) {
            cause = ne;
        }

        assertEquals( 5, httpRequestExecutor.getRetries() );
        assertNotNull( cause );
        assertEquals( 503, cause.getResponseCode() );
        assertThat(cause.getCause().getMessage(), Matchers.startsWith("Server returned HTTP response code: 503 for URL:"));

    }

}
