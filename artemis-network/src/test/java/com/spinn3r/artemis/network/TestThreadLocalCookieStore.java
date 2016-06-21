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

        String cookie = "name=test1";
        String resource = "http://httpbin.org/cookies/set?" + cookie;

        HttpRequest httpRequest = httpRequestBuilder.get(resource).execute().connect();

        assertEquals("[Cookie{name='name', value='test1', path=Optional[/], domain=Optional[httpbin.org], httpOnly=true, secure=false, maxAge=Optional[-1]}]", httpRequest.getEffectiveCookies().toString());

    }

    @Test
    public void testSecureThreadLocalCookieStore() throws Exception {

        String cookie = "name=test1";
        String resource = "https://httpbin.org/cookies/set?" + cookie;

        HttpRequest httpRequest = httpRequestBuilder.get(resource).execute().connect();

        assertEquals("[Cookie{name='name', value='test1', path=Optional[/], domain=Optional[httpbin.org], httpOnly=true, secure=false, maxAge=Optional[-1]}]", httpRequest.getEffectiveCookies().toString());

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

        String cookie = "name=test2";
        String cookieSetUrl = "http://httpbin.org/cookies/set?" + cookie;
        String firstRedirectUrl = "https://httpbin.org/redirect-to?url=" + URLEncoder.encode(cookieSetUrl, "UTF-8");

        HttpRequest httpRequest = httpRequestBuilder.get(firstRedirectUrl).execute().connect();

        assertEquals("[Cookie{name='name', value='test2', path=Optional[/], domain=Optional[httpbin.org], httpOnly=true, secure=false, maxAge=Optional[-1]}]",
                     httpRequest.getEffectiveCookies().toString());

    }

    @Test
    public void testNonSecureToSecureRedirect() throws Exception {

        // this is working but I think because the URI isn't using the scheme.

        String cookie = "name=test2";
        String lastURL = "https://httpbin.org/cookies/set?" + cookie;

        String firstURL = "http://httpbin.org/redirect-to?url=" + URLEncoder.encode(lastURL, "UTF-8");

        HttpRequest httpRequest
          = httpRequestBuilder
              //.withProxy("http://localhost:8080")
              .get(firstURL).execute().connect();

        assertEquals("[Cookie{name='name', value='test2', path=Optional[/], domain=Optional[httpbin.org], httpOnly=true, secure=false, maxAge=Optional[-1]}]",
                     httpRequest.getEffectiveCookies().toString());

    }

    // FIXME: this doesn't test with the redirect IN THE MIDDLE of the chain
    // which is the biggest thing we need to verify

    // FIXME: test redirecting to a completely separate site.. for example.. from httpbin then localhost and make sure cookies are still set...

    // FIXME:

    /*

    consider a vocabulary to encode HTTP requests in the URI

    status=301&header.Location=http://cnn.com&-cookie.foo=


     */


}
