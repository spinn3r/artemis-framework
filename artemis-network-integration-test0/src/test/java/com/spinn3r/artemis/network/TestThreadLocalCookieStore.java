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
import com.spinn3r.artemis.network.cookies.Cookie;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.time.init.SyntheticClockService;
import com.spinn3r.artemis.time.init.UptimeService;
import com.spinn3r.artemis.util.text.CollectionFormatter;
import org.junit.Before;
import org.junit.Ignore;
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

        threadLocalCookies.flush();
    }

    @Test
    public void testCookieManagerEnabled() throws Exception {

        assertTrue(networkConfig.isCookieManagerEnabled());

    }

    @Test
    public void testThreadLocalCookieStore() throws Exception {

        ResponseDescriptor responseDescriptor
          = new ResponseDescriptor.Builder()
              .withCookie("name", "test1")
              .build();

        String url = String.format( "http://localhost:%s/evaluate?response=%s", webserverPort.getPort(), responseDescriptor.toParam() );

        HttpRequest httpRequest = httpRequestBuilder.get(url).execute().connect();

        assertEquals("[Cookie{name='name', value='test1', version='VERSION_0_NETSCAPE', path=Optional[/], domain=Optional[localhost.local], httpOnly=true, secure=false, maxAge=Optional[-1]}]",
                     httpRequest.getEffectiveCookies().toString());

    }

    @Test
    public void testSecureThreadLocalCookieStore() throws Exception {

        ResponseDescriptor responseDescriptor
          = new ResponseDescriptor.Builder()
              .withCookie("name", "test1")
              .build();

        String url = String.format( "http://localhost:%s/evaluate?response=%s", webserverPort.getPort(), responseDescriptor.toParam() );

        HttpRequest httpRequest = httpRequestBuilder.get(url).execute().connect();

        assertEquals("[Cookie{name='name', value='test1', version='VERSION_0_NETSCAPE', path=Optional[/], domain=Optional[localhost.local], httpOnly=true, secure=false, maxAge=Optional[-1]}]",
                     httpRequest.getEffectiveCookies().toString());

    }

    @Test
    public void testNoCookiesInSameThreadBetweenRequests() throws Exception {

        ResponseDescriptor responseDescriptor
          = new ResponseDescriptor.Builder()
              .withCookie("name", "test1")
              .build();

        String url = String.format( "http://localhost:%s/evaluate?response=%s", webserverPort.getPort(), responseDescriptor.toParam() );

        HttpRequest httpRequest = httpRequestBuilder.get(url).execute().connect();

        assertEquals("[Cookie{name='name', value='test1', version='VERSION_0_NETSCAPE', path=Optional[/], domain=Optional[localhost.local], httpOnly=true, secure=false, maxAge=Optional[-1]}]",
                     httpRequest.getEffectiveCookies().toString());

        assertEquals("[]", threadLocalCookies.getCookies().toString());

        assertNotNull(threadLocalCookieStoreProvider.get());
        assertEquals(0, threadLocalCookieStoreProvider.get().get().getCookies().size());

    }

    @Test
    public void testThreadLocalCookieStoreInRedirectionChain() throws Exception {

        String landingUrl =
          new ResponseDescriptor.Builder()
            .withCookie("cat", "dog")
            .build().toURL( "localhost", webserverPort.getPort() );

        String url =
          new ResponseDescriptor.Builder()
            .withStatus(301)
            .withHeader("Location", landingUrl)
            .withCookie( new ResponseDescriptor.Cookie.Builder("foo", "bar")
                           .build() )
            .build()
            .toURL( "localhost.localdomain", webserverPort.getPort() );
        HttpRequest httpRequest = httpRequestBuilder.get(url).execute().connect();


        assertEquals("Cookie{name='foo', value='bar', version='VERSION_0_NETSCAPE', path=Optional[/], domain=Optional[localhost.localdomain], httpOnly=true, secure=false, maxAge=Optional[-1]}\n" +
                       "Cookie{name='cat', value='dog', version='VERSION_0_NETSCAPE', path=Optional[/], domain=Optional[localhost.local], httpOnly=true, secure=false, maxAge=Optional[-1]}\n",
                     CollectionFormatter.table(httpRequest.getEffectiveCookies()));

    }

    @Test
    @Ignore
    public void testNonSecureToSecureRedirect() throws Exception {

        // TODO: ignored as httpbin is basically broken and setting up this test
        // in the same  JVM would take a half day of work.

        // TODO: I think this is the only site we can use for this unfortunately

        // this is working but I think because the URI isn't using the scheme.

        String cookie = "name=test2";
        String lastURL = "https://httpbin.org/cookies/set?" + cookie;

        String firstURL = "http://httpbin.org/redirect-to?url=" + URLEncoder.encode(lastURL, "UTF-8");

        HttpRequest httpRequest
          = httpRequestBuilder
              //.withProxy("http://localhost:8080")
              .get(firstURL).execute().connect();

        assertEquals("[Cookie{name='name', value='test2', version='VERSION_0_NETSCAPE', path=Optional[/], domain=Optional[httpbin.org], httpOnly=true, secure=false, maxAge=Optional[-1]}]",
                     httpRequest.getEffectiveCookies().toString());

    }

    @Test
    public void testCookiesThenRedirect() throws Exception {

        // test with the redirect at the beginning of the chain
        // which is the biggest thing we need to verify

        String landingUrl = "http://localhost:" + webserverPort.getPort();

        ResponseDescriptor responseDescriptor
          = new ResponseDescriptor.Builder()
              .withStatus(301)
              .withHeader("Location", landingUrl)
              .withCookie("foo", "bar")
              .build();

        String url = String.format( "http://localhost:%s/evaluate?response=%s", webserverPort.getPort(), responseDescriptor.toParam() );

        HttpRequest httpRequest
          = httpRequestBuilder
              //.withProxy("http://localhost:8080")
              .get(url).execute().connect();

        assertEquals("[Cookie{name='foo', value='bar', version='VERSION_0_NETSCAPE', path=Optional[/], domain=Optional[localhost.local], httpOnly=true, secure=false, maxAge=Optional[-1]}]",
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
              .get(url)
              .execute()
              .connect();

        String content = httpRequest.getContentWithEncoding();

        System.out.printf("%s\n", content);



        assertEquals("[Cookie{name='foo', value='bar', version='VERSION_0_NETSCAPE', path=Optional[/], domain=Optional[localhost.localdomain], httpOnly=true, secure=false, maxAge=Optional[2147xxxx]}]",
                     httpRequest.getEffectiveCookies().toString().replaceAll("maxAge=Optional\\[2147[0-9]+\\]", "maxAge=Optional[2147xxxx]"));

    }

    @Test
    public void testRedirectToSeparateSite() throws Exception {

        // test with the redirect at the beginning of the chain
        // which is the biggest thing we need to verify

        String landingUrl =
          new ResponseDescriptor.Builder()
            .withCookie("cat", "dog")
            .build().toURL( "localhost", webserverPort.getPort() );

        String url =
          new ResponseDescriptor.Builder()
              .withStatus(301)
              .withHeader("Location", landingUrl)
              .withCookie( new ResponseDescriptor.Cookie.Builder("foo", "bar")
                             .build() )
              .build()
              .toURL( "localhost.localdomain", webserverPort.getPort() );

        HttpRequest httpRequest
          = httpRequestBuilder
              .get(url)
              .execute()
              .connect();

        assertEquals("Cookie{name='foo', value='bar', version='VERSION_0_NETSCAPE', path=Optional[/], domain=Optional[localhost.localdomain], httpOnly=true, secure=false, maxAge=Optional[-1]}\n" +
                       "Cookie{name='cat', value='dog', version='VERSION_0_NETSCAPE', path=Optional[/], domain=Optional[localhost.local], httpOnly=true, secure=false, maxAge=Optional[-1]}\n",
                     CollectionFormatter.table(httpRequest.getEffectiveCookies()));

    }

    @Test
    public void testRequestMetaWithCookies() throws Exception {

        String url = String.format( "http://localhost:%s/request-meta", webserverPort.getPort() );

        HttpRequest httpRequest = httpRequestBuilder
              .get(url)
              .withCookie("foo", "bar")
              .execute()
              .connect();

        String content = httpRequest.getContentWithEncoding();

        System.out.printf("%s\n", content);

        content = content.replaceAll("\"Host\" : \"localhost:[0-9]+\"",
                                     "\"Host\" : \"localhost:xxxx\"");

        content = content.replaceAll("\"User-Agent\" : \"[^\"]+\"",
                                     "\"User-Agent\" : \"XXXX\"");

        assertEquals("{\n" +
                       "  \"pathInfo\" : null,\n" +
                       "  \"queryString\" : null,\n" +
                       "  \"headers\" : {\n" +
                       "    \"User-Agent\" : \"XXXX\",\n" +
                       "    \"Connection\" : \"close\",\n" +
                       "    \"Cookie\" : \"$Version=\\\"1\\\"; foo=\\\"bar\\\";$Path=\\\"/\\\"\",\n" +
                       "    \"Host\" : \"localhost:xxxx\",\n" +
                       "    \"Accept-Encoding\" : \"gzip\",\n" +
                       "    \"Accept\" : \"text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\"\n" +
                       "  },\n" +
                       "  \"parameters\" : { },\n" +
                       "  \"cookies\" : [\n" +
                       "    {\n" +
                       "      \"name\" : \"foo\",\n" +
                       "      \"value\" : \"bar\",\n" +
                       "      \"path\" : \"/\",\n" +
                       "      \"domain\" : null,\n" +
                       "      \"httpOnly\" : false,\n" +
                       "      \"secure\" : false,\n" +
                       "      \"maxAge\" : null\n" +
                       "    }\n" +
                       "  ]\n" +
                       "}",
                     content);


    }

    @Test
    public void testTwoCookiesOnTwoDomainsInOriginalRequest() throws Exception {

        // test with the redirect at the beginning of the chain
        // which is the biggest thing we need to verify

        String landingUrl =
          new ResponseDescriptor.Builder()
            .withCookie("cat", "dog")
            .build().toURL( "localhost", webserverPort.getPort() );

        String url =
          new ResponseDescriptor.Builder()
              .withStatus(301)
              .withHeader("Location", landingUrl)
              .build()
              .toURL("localhost.localdomain", webserverPort.getPort());

        HttpRequest httpRequest
          = httpRequestBuilder
              .get(url)
              .withCookie(new Cookie.Builder("foo", "bar")
                            .setDomain("localhost.localdomain")
                            .build())
              .withCookie(new Cookie.Builder("cat", "dog")
                            .setDomain("example.org")
                            .build())
              .execute()
              .connect();

        assertEquals("Cookie{name='foo', value='bar', version='VERSION_1_RFC2965', path=Optional[/], domain=Optional[localhost.localdomain], httpOnly=true, secure=false, maxAge=Optional[-1]}\n" +
                       "Cookie{name='cat', value='dog', version='VERSION_1_RFC2965', path=Optional[/], domain=Optional[example.org], httpOnly=true, secure=false, maxAge=Optional[-1]}\n" +
                       "Cookie{name='cat', value='dog', version='VERSION_0_NETSCAPE', path=Optional[/], domain=Optional[localhost.local], httpOnly=true, secure=false, maxAge=Optional[-1]}\n",
                     CollectionFormatter.table(httpRequest.getEffectiveCookies()));

    }


}


