package com.spinn3r.artemis.http;

import com.google.inject.Inject;
import com.spinn3r.artemis.http.init.DefaultWebserverReferencesService;
import com.spinn3r.artemis.http.init.WebserverPort;
import com.spinn3r.artemis.http.init.WebserverService;
import com.spinn3r.artemis.http.servlets.evaluate.EvaluateServlet;
import com.spinn3r.artemis.http.servlets.evaluate.ResponseDescriptor;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.init.ServiceReferences;
import com.spinn3r.artemis.logging.init.ConsoleLoggingService;
import com.spinn3r.artemis.metrics.init.MetricsService;
import com.spinn3r.artemis.metrics.init.uptime.UptimeMetricsService;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.time.init.UptimeService;
import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static com.jayway.awaitility.Awaitility.*;
import static org.junit.Assert.*;

public class EvaluateServletTest extends BaseLauncherTest {

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Inject
    WebserverPort webserverPort;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp(MockHostnameService.class,
                    MockVersionService.class,
                    MetricsService.class,
                    UptimeService.class,
                    DirectNetworkService.class,
                    DefaultWebserverReferencesService.class,
                    WebserverService.class);
    }

    @Test
    public void testEvaluate() throws Exception {

        ResponseDescriptor responseDescriptor
          = new ResponseDescriptor.Builder()
              .build();

        String link = String.format( "http://localhost:%s/evaluate?response=%s", webserverPort.getPort(), responseDescriptor.toParam() );
        String content = httpRequestBuilder.get( link ).execute().getContentWithEncoding();

    }

    static class TestServiceReferences extends ServiceReferences {

        public TestServiceReferences() {
            add( DirectNetworkService.class );
        }

    }


}