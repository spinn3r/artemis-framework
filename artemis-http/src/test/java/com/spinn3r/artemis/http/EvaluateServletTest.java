package com.spinn3r.artemis.http;

import com.google.inject.Inject;
import com.spinn3r.artemis.http.servlets.evaluate.EvaluateServlet;
import com.spinn3r.artemis.http.servlets.evaluate.ResponseDescriptor;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.init.ServiceReferences;
import com.spinn3r.artemis.logging.init.ConsoleLoggingService;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.jayway.awaitility.Awaitility.*;
import static org.junit.Assert.*;

public class EvaluateServletTest extends BaseLauncherTest {

    private static final int PORT = 8081;

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    Server server;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp(DirectNetworkService.class);
    }

    @After
    public void tearDown() throws Exception {

        System.out.printf( "Stopping server..." );

        this.server.stop();

    }

    @Test
    @Ignore
    public void testRequestMeta() throws Exception {

        this.server = new ServerBuilder()
                        .setPort( PORT )
                        .addServlet( "/evaluate", new EvaluateServlet())
                        .build();

        this.server.start();

        ResponseDescriptor responseDescriptor
          = new ResponseDescriptor.Builder()
              .build();

        String link = String.format( "http://localhost:%s/evaluate?response=%s", PORT, responseDescriptor.toParam() );
        String content = httpRequestBuilder.get( link ).execute().getContentWithEncoding();

    }

    static class TestServiceReferences extends ServiceReferences {

        public TestServiceReferences() {
            add( DirectNetworkService.class );
        }

    }


}