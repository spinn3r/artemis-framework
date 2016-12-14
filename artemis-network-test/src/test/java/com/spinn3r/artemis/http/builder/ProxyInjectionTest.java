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
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.builder.HttpRequestMethod;
import com.spinn3r.artemis.network.builder.proxies.ProxyReferences;
import com.spinn3r.artemis.network.init.NetworkService;
import com.spinn3r.artemis.time.init.UptimeService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.spinn3r.artemis.init.Services.*;
import static org.junit.Assert.assertEquals;

public class ProxyInjectionTest {

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
                         ref(NetworkService.class));

        launcher.getInjector().injectMembers( this );

    }

    @After
    public void tearDown() throws Exception {

        launcher.stop();

    }

    @Test
    public void testBasicProxyUsage() throws Exception {

        // make sure the methods return the right 'this' instance

        HttpRequestMethod method = httpRequestBuilder.get("http://cnn.com");
        method.execute();

        assertEquals("HTTP @ /127.0.0.1:9997", method.getProxy().toString());

    }

    @Test
    public void testCustomProxyUsage() throws Exception {

        // make sure the methods return the right 'this' instance

        HttpRequestMethod method = httpRequestBuilder.get("http://cnn.com");
        method.withProxy(ProxyReferences.create("http://127.0.0.1:12345"));
        method.execute();

        assertEquals("HTTP @ /127.0.0.1:12345", method.getProxy().toString());

    }

}

