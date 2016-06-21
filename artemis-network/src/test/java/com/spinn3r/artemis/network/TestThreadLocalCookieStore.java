package com.spinn3r.artemis.network;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.builder.*;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.time.init.SyntheticClockService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.List;

public class TestThreadLocalCookieStore extends BaseLauncherTest {

    @Inject
    HttpRequestExecutorFactory httpRequestExecutorFactory;

    @Inject
    HttpRequestBuilder httpRequestBuilder;


    @Inject
    DirectNetworkService directNetworkService;

    @Inject
    NetworkConfig networkConfig;
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp(
                SyntheticClockService.class,
                DirectNetworkService.class);

        networkConfig.setCookieManagerEnabled(true);
        directNetworkService.start();
    }

    @Test
    public void testThreadLocalCookieStore() throws Exception {

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        String cookie = "name=test1";
        String resource = "http://httpbin.org/cookies/set?" + cookie;


        httpRequestExecutor.execute(() -> httpRequestBuilder.get(resource).execute().connect());

        Assert.assertEquals(cookie, getCookies("http://httpbin.org").iterator().next());
    }

    @Test
    public void testThreadLocalCookieStoreInRedirectionChain() throws Exception {

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        String cookie = "name=test2";
        String cookieSetUrl = "http://httpbin.org/cookies/set?" + cookie;
        String firstRedirectUrl = "https://httpbin.org/redirect-to?url=" + URLEncoder.encode(cookieSetUrl, "UTF-8");

        httpRequestExecutor.execute(() -> httpRequestBuilder.get(firstRedirectUrl).execute().connect());

        Assert.assertEquals(cookie, getCookies("http://httpbin.org").iterator().next());
    }

    private static List<String> getCookies(String domain) throws IOException, URISyntaxException {

        return CookieHandler.getDefault().get(new URI(domain), new HashMap<>()).get("Cookie");
    }

}
