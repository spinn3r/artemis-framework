package com.spinn3r.artemis.http.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.http.init.DefaultWebserverReferencesService;
import com.spinn3r.artemis.http.init.WebserverPort;
import com.spinn3r.artemis.http.init.WebserverService;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import com.spinn3r.artemis.logging.init.ConsoleLoggingService;
import com.spinn3r.artemis.metrics.init.MetricsService;
import com.spinn3r.artemis.metrics.init.uptime.UptimeMetricsService;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.time.init.UptimeService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.spinn3r.artemis.init.Services.*;
import static org.junit.Assert.*;

public class HttpRequestBuilderTest {

    Launcher launcher = null;

    @Inject
    protected HttpRequestBuilder httpRequestBuilder;

    @Inject
    protected WebserverPort webserverPort;

    @Before
    public void setUp() throws Exception {

        ConfigLoader configLoader = new ResourceConfigLoader();

        launcher = Launcher.newBuilder(configLoader ).build();

        launcher.launch( ref(MockHostnameService.class),
                         ref(MockVersionService.class),
                         ref(ConsoleLoggingService.class),
                         ref(MetricsService.class),
                         ref(UptimeService.class),
                         ref(UptimeMetricsService.class),
                         ref(DefaultWebserverReferencesService.class),
                         ref(WebserverService.class),
                         ref(DirectNetworkService.class));

        launcher.getInjector().injectMembers( this );

    }

    @After
    public void tearDown() throws Exception {

        launcher.stop();

    }

    @Test
    public void testWithAcceptLanguage() throws Exception {

        HttpRequest request = httpRequestBuilder.get( "http://twitter.com/youtube" )
                                                .withFollowContentRedirects( false )
                                                .execute()
                                                .connect();

        assertEquals("{Accept-Language=en-US,en;q=0.8}",
                     request.getHttpRequestMeta().getRequestHeadersMap().toString());

    }

}

