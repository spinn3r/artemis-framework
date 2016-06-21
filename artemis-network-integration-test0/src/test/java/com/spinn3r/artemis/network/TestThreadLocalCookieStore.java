package com.spinn3r.artemis.network;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.http.init.DefaultWebserverReferencesService;
import com.spinn3r.artemis.http.init.WebserverPort;
import com.spinn3r.artemis.http.init.WebserverService;
import com.spinn3r.artemis.http.servlets.evaluate.ResponseDescriptor;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.metrics.init.MetricsService;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.builder.HttpRequestExecutorFactory;
import com.spinn3r.artemis.network.builder.cookies.ThreadLocalCookieStore;
import com.spinn3r.artemis.network.builder.cookies.ThreadLocalCookies;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.time.init.SyntheticClockService;
import com.spinn3r.artemis.time.init.UptimeService;
import org.junit.Before;
import org.junit.Test;

import java.net.URLEncoder;

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

    @Inject
    WebserverPort webserverPort;

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp( SyntheticClockService.class,
                     MockHostnameService.class,
                     MockVersionService.class,
                     MetricsService.class,
                     UptimeService.class,
                     DirectNetworkService.class,
                     DefaultWebserverReferencesService.class,
                     WebserverService.class );

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

    @Test
    public void testCookiesThenRedirect() throws Exception {

        // test with the redirect at the beginning of the chain
        // which is the biggest thing we need to verify

        ResponseDescriptor responseDescriptor
          = new ResponseDescriptor.Builder()
              .withStatus(301)
              .withHeader("Location", "http://httpbin.org")
              .withCookie("foo", "bar")
              .build();

        String url = String.format( "http://localhost:%s/evaluate?response=%s", webserverPort.getPort(), responseDescriptor.toParam() );

        HttpRequest httpRequest
          = httpRequestBuilder
              //.withProxy("http://localhost:8080")
              .get(url).execute().connect();

        assertEquals("[Cookie{name='foo', value='bar', path=Optional[/], domain=Optional[localhost.local], httpOnly=true, secure=false, maxAge=Optional[-1]}]",
                     httpRequest.getEffectiveCookies().toString());


    }

    @Test
    public void testCookiesWithoutRedirect() throws Exception {

        ResponseDescriptor responseDescriptor
          = new ResponseDescriptor.Builder()
              .withCookie( new ResponseDescriptor.Cookie.Builder("foo", "bar")
                             .withDomain("localhost.localdomain")
                             .withPath("/")
                             .withMaxAge(Integer.MAX_VALUE)
                             .build() )
              .build();

        String url = String.format( "http://localhost.localdomain:%s/evaluate?response=%s", webserverPort.getPort(), responseDescriptor.toParam() );

        HttpRequest httpRequest
          = httpRequestBuilder
              //.withProxy("http://localhost:8080")
              .get(url)
              .execute()
              .connect();

        String content = httpRequest.getContentWithEncoding();

        System.out.printf("%s\n", content);

        assertEquals("[Cookie{name='foo', value='bar', path=Optional[/], domain=Optional[localhost.localdomain], httpOnly=true, secure=false, maxAge=Optional[2147483646]}]",
                     httpRequest.getEffectiveCookies().toString());

    }

    @Test
    public void testRedirectToSeparateSite() throws Exception {

        // test with the redirect at the beginning of the chain
        // which is the biggest thing we need to verify

        ResponseDescriptor responseDescriptor
          = new ResponseDescriptor.Builder()
              .withStatus(301)
              .withHeader("Location", "http://httpbin.org/cookies/set?cat=dog")
              .withCookie( new ResponseDescriptor.Cookie.Builder("foo", "bar")
                             .build() )
              .build();

        String url = String.format( "http://localhost.localdomain:%s/evaluate?response=%s", webserverPort.getPort(), responseDescriptor.toParam() );

        HttpRequest httpRequest
          = httpRequestBuilder
              .get(url)
              .execute()
              .connect();

        assertEquals("[Cookie{name='foo', value='bar', path=Optional[/], domain=Optional[localhost.localdomain], httpOnly=true, secure=false, maxAge=Optional[-1]}, Cookie{name='cat', value='dog', path=Optional[/], domain=Optional[httpbin.org], httpOnly=true, secure=false, maxAge=Optional[-1]}]",
                     httpRequest.getEffectiveCookies().toString());

        assertEquals("{\n" +
                       "  \"cookies\": {\n" +
                       "    \"cat\": \"dog\"\n" +
                       "  }\n" +
                       "}\n",
                     httpRequest.getContentWithEncoding());

    }

    @Test
    public void testRequestMetaWithCookies() throws Exception {

        String url = String.format( "http://localhost:%s/request-meta", webserverPort.getPort() );

        HttpRequest httpRequest
          = httpRequestBuilder
              .get(url)
              .withCookie("foo", "bar")
              .execute()
              .connect();

        String content = httpRequest.getContentWithEncoding();

        System.out.printf("%s\n", content);

        content = content.replaceAll("\"Host\" : \"localhost:[0-9]+\"",
                                     "\"Host\" : \"localhost:xxxx\"");

        assertEquals("{\n" +
                       "  \"pathInfo\" : null,\n" +
                       "  \"queryString\" : null,\n" +
                       "  \"headers\" : {\n" +
                       "    \"User-Agent\" : \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.71 Safari/537.36\",\n" +
                       "    \"Connection\" : \"close\",\n" +
                       "    \"Cookie\" : \"$Version=\\\"1\\\"; foo=\\\"bar\\\";$Path=\\\"/\\\"\",\n" +
                       "    \"Host\" : \"localhost:xxxx\",\n" +
                       "    \"Accept-Encoding\" : \"gzip\",\n" +
                       "    \"Accept\" : \"text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\"\n" +
                       "  },\n" +
                       "  \"parameters\" : { }\n" +
                       "}",
                     content);


    }

}
