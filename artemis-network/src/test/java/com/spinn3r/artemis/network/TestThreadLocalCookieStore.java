package com.spinn3r.artemis.network;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.builder.*;
import com.spinn3r.artemis.network.builder.cookies.ThreadLocalCookieStore;
import com.spinn3r.artemis.network.builder.cookies.ThreadLocalCookies;
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

import static org.junit.Assert.*;

public class TestThreadLocalCookieStore extends BaseLauncherTest {

    @Inject
    HttpRequestExecutorFactory httpRequestExecutorFactory;

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Inject
    NetworkConfig networkConfig;

    @Inject
    ThreadLocalCookies threadLocalCookies;

    @Inject
    Provider<ThreadLocalCookieStore> threadLocalCookieStoreProvider;

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp( SyntheticClockService.class,
                     DirectNetworkService.class);

        networkConfig.setCookieManagerEnabled(true);

    }

    @Test
    public void testCookieManagerEnabled() throws Exception {

        assertTrue(networkConfig.isCookieManagerEnabled());

    }

    @Test
    public void testThreadLocalCookieStore() throws Exception {

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        String cookie = "name=test1";
        String resource = "http://httpbin.org/cookies/set?" + cookie;

        httpRequestExecutor.execute(() -> httpRequestBuilder.get(resource).execute().connect());

        assertEquals(cookie, getCookies("http://httpbin.org").iterator().next());

    }

    @Test
    public void testNoCookiesInSameThreadBetweenRequests() throws Exception {

        String cookie = "name=test1";
        String resource = "http://httpbin.org/cookies/set?" + cookie;

        HttpRequest httpRequest = httpRequestBuilder.get(resource).execute().connect();

        assertEquals("[Cookie{name='name', value='test1', path=Optional[/], domain=Optional[httpbin.org], httpOnly=true, secure=false, maxAge=Optional[-1]}]", httpRequest.getEffectiveCookies().toString());
        assertEquals("[]", threadLocalCookies.getCookies().toString());

        assertNotNull(threadLocalCookieStoreProvider.get());
        assertEquals(0, threadLocalCookieStoreProvider.get().get().getCookies().size());

    }

    @Test
    public void testThreadLocalCookieStoreInRedirectionChain() throws Exception {

        HttpRequestExecutor httpRequestExecutor = httpRequestExecutorFactory.create();

        String cookie = "name=test2";
        String cookieSetUrl = "http://httpbin.org/cookies/set?" + cookie;
        String firstRedirectUrl = "https://httpbin.org/redirect-to?url=" + URLEncoder.encode(cookieSetUrl, "UTF-8");

        httpRequestExecutor.execute(() -> httpRequestBuilder.get(firstRedirectUrl).execute().connect());

        assertEquals(cookie, getCookies("http://httpbin.org").iterator().next());
    }

    private static List<String> getCookies(String domain) throws IOException, URISyntaxException {

        return CookieHandler.getDefault().get(new URI(domain), new HashMap<>()).get("Cookie");
    }

}
