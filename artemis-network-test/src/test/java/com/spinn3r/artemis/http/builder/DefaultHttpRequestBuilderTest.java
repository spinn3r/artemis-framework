package com.spinn3r.artemis.http.builder;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.spinn3r.artemis.http.init.DefaultWebserverReferencesService;
import com.spinn3r.artemis.http.init.WebserverPort;
import com.spinn3r.artemis.http.init.WebserverService;
import com.spinn3r.artemis.http.servlets.RequestMeta;
import com.spinn3r.artemis.http.servlets.evaluate.ResponseDescriptor;
import com.spinn3r.artemis.init.Launcher;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.init.config.ConfigLoader;
import com.spinn3r.artemis.init.config.ResourceConfigLoader;
import com.spinn3r.artemis.logging.init.ConsoleLoggingService;
import com.spinn3r.artemis.metrics.init.MetricsService;
import com.spinn3r.artemis.metrics.init.uptime.UptimeMetricsService;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.PostEncoder;
import com.spinn3r.artemis.network.builder.DefaultHttpRequestBuilder;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestMethod;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;
import com.spinn3r.artemis.network.builder.proxies.ProxyReferences;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.time.init.UptimeService;
import com.spinn3r.artemis.util.misc.HitIndex;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Map;

import static com.spinn3r.artemis.init.Services.*;
import static com.spinn3r.artemis.network.builder.HttpRequest.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class DefaultHttpRequestBuilderTest {

    Launcher launcher = null;

    @Inject
    protected DefaultHttpRequestBuilder httpRequestBuilder;

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
    @Ignore
    public void testBrokenCookies() throws Exception {

        String link = "http://www.elitesecurity.org/p3681726";

        HttpRequest httpRequest = httpRequestBuilder.get(link).execute();

        httpRequest.getContentWithEncoding();

        httpRequest.getHttpContentResponseMeta();

    }

    @Test
    public void testCorrectInstanceForRequestBuilder() throws Exception {

        // make sure the methods return the right 'this' instance

        assertTrue( httpRequestBuilder == httpRequestBuilder.withProxy( "http://localhost:9997" ) );
        assertTrue( httpRequestBuilder == httpRequestBuilder.withUserAgent( null ) );

    }

    @Test
    public void testWithProxy() throws Exception {

        httpRequestBuilder.withProxy( "http://127.0.0.1:9997" );

        assertEquals( "ProxyRegistry{prioritizedProxyReferences=[PrioritizedProxyReference{name='default', priority=1, regex='.*', proxy=HTTP @ /127.0.0.1:9997} ProxyReference{host='127.0.0.1', port=9997, proxy=HTTP @ /127.0.0.1:9997}]}",
                      httpRequestBuilder.getProxyRegistry().toString() );

    }

    @Test
    public void testCorrectInstanceForRequestMethod() throws Exception {

        // make sure the methods return the right 'this' instance

        HttpRequestMethod method = httpRequestBuilder.get( "http://cnn.com" );

        assertTrue( method == method.ifModifiedSince( -1 ) );
        assertTrue( method == method.withConnectTimeout( -1 ) );
        assertTrue( method == method.withCookies( Maps.newHashMap() ) );
        assertTrue( method == method.withEtag( null ) );
        assertTrue( method == method.withExecutor( null ) );
        assertTrue( method == method.withMaxContentLength( -1 ) );
        assertTrue( method == method.withProperty( "foo", "bar" ) );
        assertTrue( method == method.withReadTimeout( -1 ) );
        assertTrue( method == method.withRequestHeader( "foo", "bar" ) );
        assertTrue( method == method.withRequestHeaders( null ) );

    }

    @Test
    public void testWithDirectProxyReference() throws Exception {

        String url = String.format("http://localhost:%s/request-meta", webserverPort.getPort());

        ProxyReference proxyReference = new ProxyReference(null, -1, Proxy.NO_PROXY);

        HttpRequest httpRequest
          = httpRequestBuilder
              .withProxy(proxyReference)
              .get(url)
              .execute()
              .connect();

        assertEquals(200, httpRequest.getResponseCode());

    }

    @Test
    public void testGetWithHeaders() throws Exception {

        String url = String.format("http://localhost:%s/request-meta", webserverPort.getPort());

        String contentWithEncoding =
          httpRequestBuilder.get( url )
            .withRequestHeader( "X-Foo", "bar" )
            .execute()
            .getContentWithEncoding()
          ;

        RequestMeta requestMeta = RequestMeta.fromJSON(contentWithEncoding);

        System.out.printf( "result: %s\n", contentWithEncoding );

        assertTrue(requestMeta.getHeaders().containsKey("X-Foo"));

    }

    @Test
    public void testCustomHttpTimeouts() throws Exception {

        String url = new ResponseDescriptor.Builder()
                       .withStatus(200)
                       .withDelayMillis(5000)
                       .build()
                       .toURL("localhost", webserverPort.getPort());

        String result =
          httpRequestBuilder
            .get( url )
            .withConnectTimeout( 6_000 )
            .withReadTimeout( 6_000 )
            .execute()
            .getContentWithEncoding();

    }

    @Test
    public void testUserAgentRandomization() throws Exception {

        HitIndex<String> hitIndex = new HitIndex<>();

        for (int i = 0; i < 10; i++) {
            HttpRequestMethod httpRequestMethod = httpRequestBuilder.get("https://www.example.com");
            hitIndex.registerHit(httpRequestMethod.getUserAgent());
        }

        assertThat(hitIndex.size(), greaterThan(1));

        System.out.printf("%s\n", hitIndex.read());

    }

    @Test(expected = NetworkException.class)
    public void testCustomHttpTimeoutsWithFailure() throws Exception {

        String url = new ResponseDescriptor.Builder()
                       .withStatus(200)
                       .withDelayMillis(5000)
                       .build()
                       .toURL("localhost", webserverPort.getPort());

        String result =
          httpRequestBuilder
            .get( url )
            .withConnectTimeout( 1_000 )
            .withReadTimeout( 1_000 )
            .execute()
            .getContentWithEncoding();

    }

    @Test
    public void testHttpPost() throws Exception {

        Map<String,String> map = Maps.newLinkedHashMap();

        map.put( "foo", "bar" );

        String data = PostEncoder.encode( map );

        assertNotNull( data );

        String encoding = "UTF-8";
        String type = "application/x-www-form-urlencoded";

        String url = String.format( "http://localhost:%s/request-meta", webserverPort.getPort() );

        String contentWithEncoding
          = httpRequestBuilder
              .post(url, data, encoding, type)
              .execute()
              .getContentWithEncoding();

        System.out.printf("%s\n", contentWithEncoding);

        RequestMeta requestMeta = RequestMeta.fromJSON(contentWithEncoding);

        assertEquals("{foo=bar}", requestMeta.getParameters().toString());

    }

    @Test
    public void testHttpPut() throws Exception {

        String data = "testing123";

        String encoding = "UTF-8";
        String type = "text/plain";

        String url = new ResponseDescriptor.Builder()
                             .withStatus(200)
                             .withContent(data)
                             .build()
                             .toURL("localhost", webserverPort.getPort());

        HttpRequest httpRequest
          = httpRequestBuilder
              .put(url, data, encoding, type)
              .execute();

        String result = httpRequest.getContentWithEncoding();

        System.out.printf( "result: %s\n", result );

        assertTrue( result.contains( data ) );

        assertEquals("text/plain;charset=utf-8", httpRequest.getResponseHeader("Content-Type"));

    }

    @Test
    public void testHttpPut2() throws Exception {

        String data = "testing123 日本 here it is... and here's some more";

        String encoding = "UTF-8";
        String type = "text/plain";

        String url = String.format( "http://localhost:%s/echo", webserverPort.getPort() );

        String result =
          httpRequestBuilder.put( url, data, encoding, type )
            .execute()
            .getContentWithEncoding()
          ;

        System.out.printf( "result: %s\n", result );

        assertEquals( data, result );

    }

    @Test
    public void testGetUsingHTTPS() throws Exception {

        // TODO: refactor this to use our own internal embedded jetty for tests
        // and also to use the chaos servlet to test various HTTP requirements.

        HttpRequest request = httpRequestBuilder.get( "https://www.google.com/" )
            .withEtag( "hello world" )
            .execute();

        String contentWithEncoding = request.getContentWithEncoding();

        assertNotNull( contentWithEncoding );
        assertNotNull( request.getInetAddress() );
        assertTrue( request.getDuration() > 0 );
        assertTrue( request.getContentLength() > 0 );

    }

    @Test
    public void testGetUsingHTTP() throws Exception {

        HttpRequest request = httpRequestBuilder.get( "http://www.cnn.com/" )
                                .execute();

        String contentWithEncoding = request.getContentWithEncoding();

        assertNotNull( contentWithEncoding );
        assertNotNull( request.getInetAddress() );
        assertTrue( request.getDuration() > 0 );
        assertTrue( request.getContentLength() > 0 );

    }

    @Test
    public void testGetWithRedirectUsingHTTPS() throws Exception {

        String link = "https://twitter.com/asdf654321";

        HttpRequest httpRequest = httpRequestBuilder
                                .get(link)
                                .withFollowContentRedirects(false)
                                .execute();

        httpRequest.connect();

        String resourceFromRedirect = httpRequest.getResourceFromRedirect();
        System.out.printf("resourceFromRedirect: %s\n", resourceFromRedirect);

        assertNotEquals(link, resourceFromRedirect);
        assertEquals("https://twitter.com/account/suspended", resourceFromRedirect);

    }

    @Test
    public void testGetWith302RedirectUsingHTTP() throws Exception {

        String landingURL = "http://localhost:" + webserverPort.getPort();

        String requestURL = new ResponseDescriptor.Builder()
                              .withStatus(302)
                              .withHeader("Location", landingURL)
                              .build()
                              .toURL("localhost", webserverPort.getPort());

        HttpRequest httpRequest = httpRequestBuilder
                                    .get(requestURL)
                                    .withFollowContentRedirects(false)
                                    .execute();

        httpRequest.connect();

        String resourceFromRedirect = httpRequest.getResourceFromRedirect();
        System.out.printf("resourceFromRedirect: %s\n", resourceFromRedirect);

        assertNotEquals(requestURL, resourceFromRedirect);
        assertEquals(landingURL, resourceFromRedirect);

    }

    @Test
    public void testGetUsingDirectInputStream() throws Exception {

        String url = String.format("http://localhost:%s/request-meta", webserverPort.getPort());

        InputStream inputStream =
          httpRequestBuilder.get( url )
            .execute()
            .getDirectInputStream();

        String content = new String( ByteStreams.toByteArray(inputStream), Charsets.UTF_8 );

        assertTrue( content.length() > 0 );

    }

    @Test
    public void testGetWithRedirectToHTTPS() throws Exception {

        assertEquals( DefaultHttpRequestBuilder.class, httpRequestBuilder.getClass() );

        HttpRequest request = httpRequestBuilder.get( "http://twitter.com/youtube" )
                                .withFollowContentRedirects( false )
                                .execute();

        String contentWithEncoding = request.getContentWithEncoding();

        assertEquals( "https://twitter.com/youtube", request.getResourceFromRedirect() );

        assertNotEquals( "", contentWithEncoding );
        assertTrue( contentWithEncoding.length() > 0 );

    }

    @Test(expected = NetworkException.class)
    public void testReadTimeout() throws Exception {

        HttpRequest request = null;

        try {

            request = httpRequestBuilder.get( "http://cnn.com:12345" )
                                        .execute();

            String contentWithEncoding = request.getContentWithEncoding();

        } catch (NetworkException e) {

            assertEquals( STATUS_READ_TIMEOUT, request.getResponseCode() );

            assertEquals( STATUS_READ_TIMEOUT, e.getResponseCode() );

            throw e;

        }

    }

    @Test
    public void testGetWithOneCookie() throws Exception {

        Map<String,String> cookies = Maps.newHashMap();

        cookies.put( "hello", "world" );

        String url = String.format("http://localhost:%s/request-meta", webserverPort.getPort());

        HttpRequest request =
          httpRequestBuilder.get(url)
            .withCookies( cookies )
            .execute();

        String contentWithEncoding = request.getContentWithEncoding();

        RequestMeta requestMeta = RequestMeta.fromJSON(contentWithEncoding);

        assertEquals("[Cookie{name='hello', value='world', path=Optional[/], domain=Optional.empty, httpOnly=false, secure=false, maxAge=Optional.empty}]",
                     requestMeta.getCookies().toString());

    }

    @Test
    public void testGetWithTwoCookies() throws Exception {

        Map<String,String> cookies = Maps.newHashMap();

        cookies.put( "hello", "world" );
        cookies.put( "cat", "dog" );

        String url = String.format("http://localhost:%s/request-meta", webserverPort.getPort());

        HttpRequest request =
          httpRequestBuilder
            .get( url )
            .withCookies( cookies )
            .execute();

        String contentWithEncoding = request.getContentWithEncoding();

        RequestMeta requestMeta = RequestMeta.fromJSON(contentWithEncoding);

        assertEquals("[Cookie{name='cat', value='dog', path=Optional[/], domain=Optional.empty, httpOnly=false, secure=false, maxAge=Optional.empty}, Cookie{name='hello', value='world', path=Optional[/], domain=Optional.empty, httpOnly=false, secure=false, maxAge=Optional.empty}]",
                     requestMeta.getCookies().toString());

    }

    @Test
    @Ignore
    public void testHEAD() throws Exception {

        String url = "http://t.co/s8FdCa0tq6";
        //String url = "http://cnn.com";

        HttpRequest httpRequest = httpRequestBuilder
                                .head(url)
                                .withFollowContentRedirects(false)
                                .withFollowRedirects(false)
                                .withUserAgent("curl")
                                .withProxy(ProxyReferences.create("http://localhost:8081"))
                                .execute()
                                .connect();

        //assertEquals("", httpRequest.getContentWithEncoding());
        assertNull(httpRequest.getResponseHeader("Location"));
        //assertEquals("", httpRequest.getResourceFromRedirect());

        System.out.printf("%s\n", httpRequest.getResponseHeaderNames());

        System.out.printf("resourceFromRedirect: %s\n", httpRequest.getResourceFromRedirect());

    }

    @Test
    @Ignore
    public void testGETWithoutData() throws Exception {

//        String url = "http://t.co/s8FdCa0tq6";
//        //String url = "http://cnn.com";
//
//        HttpRequest httpRequest = httpRequestBuilder
//                                    .head(url)
//                                    .withFollowContentRedirects(false)
//                                    .withFollowRedirects(false)
//                                    .withUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36")
//                                    .withProxy(ProxyReferences.create("http://localhost:8081"))
//                                    .execute()
//                                    .connect();
//
//        //assertEquals("", httpRequest.getContentWithEncoding());
//        assertNull(httpRequest.getResponseHeader("Location"));
//        //assertEquals("", httpRequest.getResourceFromRedirect());
//
//        System.out.printf("%s\n", httpRequest.getResponseHeaderNames());
//
//        System.out.printf("resourceFromRedirect: %s\n", httpRequest.getResourceFromRedirect());
//

        //String link = "https://t.co/s8FdCa0tq6";
        String link = "http://t.co/o13p5fPNDg";
        // https://t.co/o13p5fPNDg


        URL url = new URL(link);

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8081));

        HttpURLConnection myURLConnection = (HttpURLConnection)url.openConnection(proxy);
        //HttpURLConnection myURLConnection = (HttpURLConnection)url.openConnection();
        myURLConnection.setRequestMethod("GET");
        myURLConnection.setUseCaches(false);
        //myURLConnection.setDoInput(false);
        //myURLConnection.setDoOutput(false);
        myURLConnection.setInstanceFollowRedirects(true);
        myURLConnection.setChunkedStreamingMode(1024);
        myURLConnection.connect();
        InputStream inputStream = myURLConnection.getInputStream();
        System.out.printf("%s\n", inputStream.getClass().getName());
        //assertEquals(301, myURLConnection.getResponseCode());

        // we read 12k here.. thats horrible..
        System.out.printf("available: %s\n", inputStream.available() );

        //assertEquals("http://ofa.bo/2cZAI0q", myURLConnection.getHeaderField("Location"));

        //assertEquals("", myURLConnection.get);

        //sun.net.www.protocol.http.HttpURLConnection$HttpInputStream

    }


    @Test
    @Ignore
    public void testHEADDirectly() throws Exception {

        String link = "http://t.co/s8FdCa0tq6";

        URL url = new URL(link);

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8081));

        HttpURLConnection myURLConnection = (HttpURLConnection)url.openConnection(proxy);
        //HttpURLConnection myURLConnection = (HttpURLConnection)url.openConnection();
        myURLConnection.setRequestMethod("HEAD");
        myURLConnection.setUseCaches(false);
        myURLConnection.setChunkedStreamingMode(1024);
        //myURLConnection.setDoInput(false);
        //myURLConnection.setDoOutput(false);
        myURLConnection.setInstanceFollowRedirects(false);
        myURLConnection.connect();
        myURLConnection.getInputStream();
        assertEquals(301, myURLConnection.getResponseCode());
        assertEquals("http://ofa.bo/2cZAI0q", myURLConnection.getHeaderField("Location"));

    }


    @Test
    @Ignore
    public void testRequestWithNoDataReads() throws Exception {

        String url = "http://www.cnn.com";

        HttpRequest httpRequest = httpRequestBuilder
                                    .get(url)
                                    .withProxy(ProxyReferences.create("http://localhost:8081"))
                                    .execute()
                                    .connect();

        //assertEquals("", httpRequest.getContentWithEncoding());
        //assertNull(httpRequest.getResponseHeader("Location"));
        //assertEquals("", httpRequest.getResourceFromRedirect());

        //System.out.printf("%s\n", httpRequest.getResponseHeaderNames());

        //System.out.printf("resourceFromRedirect: %s\n", httpRequest.getResourceFromRedirect());

    }



    @Test(expected = NetworkException.class)
    public void testNotFound() throws Exception {

        String url = new ResponseDescriptor.Builder()
                       .withStatus(404)
                       .build()
                       .toURL("localhost", webserverPort.getPort());

        HttpRequest request =
          httpRequestBuilder.get( url )
                            .execute()
                            .connect();

    }

    @Test
    @Ignore
    public void testInvalidCookies() throws Exception {

        String link = "http://trib.al/qN1OsNP";

        httpRequestBuilder
          .get( link )
          .execute().getContentWithEncoding();

    }

}

