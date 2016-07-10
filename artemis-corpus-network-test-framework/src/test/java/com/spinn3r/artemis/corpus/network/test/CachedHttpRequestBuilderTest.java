package com.spinn3r.artemis.corpus.network.test;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.spinn3r.artemis.corpus.test.CorporaAsserter;
import com.spinn3r.artemis.http.init.DefaultWebserverReferencesService;
import com.spinn3r.artemis.http.init.WebserverPort;
import com.spinn3r.artemis.http.init.WebserverService;
import com.spinn3r.artemis.http.servlets.evaluate.ResponseDescriptor;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.init.MockHostnameService;
import com.spinn3r.artemis.init.MockVersionService;
import com.spinn3r.artemis.metrics.init.MetricsService;
import com.spinn3r.artemis.metrics.init.uptime.UptimeMetricsService;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestMeta;
import com.spinn3r.artemis.network.builder.HttpResponseMeta;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.time.Uptime;
import com.spinn3r.artemis.time.init.SyntheticClockService;
import com.spinn3r.artemis.time.init.UptimeService;
import com.spinn3r.artemis.util.text.MapFormatter;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class CachedHttpRequestBuilderTest extends BaseLauncherTest {

    @Inject
    CachedHttpRequestBuilder cachedHttpRequestBuilder;

    @Inject
    NetworkCorporaCacheFactory networkCorporaCacheFactory;

    @Inject
    WebserverPort webserverPort;

    CorporaAsserter corporaAsserter = new CorporaAsserter( getClass() );
    NetworkCorporaCache networkCorporaCache;

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp( MockHostnameService.class,
                     MockVersionService.class,
                     SyntheticClockService.class,
                     UptimeService.class,
                     MetricsService.class,
                     DirectNetworkService.class,
                     CachedNetworkService.class,
                     DefaultWebserverReferencesService.class,
                     WebserverService.class );

        networkCorporaCache = networkCorporaCacheFactory.create( getClass() );

    }

    @Test
    public void testGet() throws Exception {

        String link = "http://cnn.com";
        HttpRequest httpRequest = cachedHttpRequestBuilder.get( link ).execute();
        String contentWithEncoding = httpRequest.getContentWithEncoding();
        HttpResponseMeta httpResponseMeta = httpRequest.getHttpResponseMeta();

        assertNotNull( httpRequest );
        assertNotNull( httpResponseMeta );
        assertNotNull( httpResponseMeta.getResponseHeadersMap() );
        corporaAsserter.assertEquals( "testGet#responseHeadersMap", MapFormatter.table( httpResponseMeta.getResponseHeadersMap() ) );

    }

    @Test
    public void testGetWithCustomHeaders() throws Exception {

        String link = new ResponseDescriptor.Builder()
          .build().toURL("localhost", webserverPort.getPort());

        //String link = "https://httpbin.org/get";

        HttpRequest httpRequest =
          cachedHttpRequestBuilder
            .get( link )
            .withRequestHeader( "X-foo", "bar" )
            .execute();

        String contentWithEncoding = httpRequest.getContentWithEncoding();
        HttpResponseMeta httpResponseMeta = httpRequest.getHttpResponseMeta();
        HttpRequestMeta httpRequestMeta = httpRequest.getHttpRequestMeta();

        assertNotNull( httpRequest );

        assertNotNull( httpResponseMeta );
        assertNotNull( httpResponseMeta.getResponseHeadersMap() );

        assertNotNull( httpRequestMeta );
        assertNotNull( httpRequestMeta.getRequestHeadersMap() );

        assertEquals( "bar", httpRequestMeta.getRequestHeadersMap().get( "X-foo" ) );

        corporaAsserter.assertEquals( "testGetWithCustomHeaders#requestHeadersMap", MapFormatter.table( httpRequest.getRequestHeadersMap() ) );
        corporaAsserter.assertEquals( "testGetWithCustomHeaders#responseHeadersMap", MapFormatter.table( httpResponseMeta.getResponseHeadersMap() ) );

    }

    @Test
    public void testWithPost1() throws Exception {

        Map<String,String> parameters = Maps.newHashMap();

        parameters.put( "hello", "world" );
        parameters.put( "cat", "dog" );

        String link = "https://httpbin.org/post";
        HttpRequest httpRequest =
          cachedHttpRequestBuilder
            .post( link, parameters )
            .withRequestHeader( "X-foo", "bar" )
            .execute();

        String contentWithEncoding = httpRequest.getContentWithEncoding();
        HttpResponseMeta httpResponseMeta = httpRequest.getHttpResponseMeta();
        HttpRequestMeta httpRequestMeta = httpRequest.getHttpRequestMeta();

        assertNotNull( httpRequest );

        assertNotNull( httpResponseMeta );
        assertNotNull( httpResponseMeta.getResponseHeadersMap() );

        assertNotNull( httpRequestMeta );
        assertNotNull( httpRequestMeta.getRequestHeadersMap() );

        assertEquals( "bar", httpRequestMeta.getRequestHeadersMap().get( "X-foo" ) );

        corporaAsserter.assertEquals( "testWithPost1#requestHeadersMap", MapFormatter.table( httpRequest.getRequestHeadersMap() ) );
        corporaAsserter.assertEquals( "testWithPost1#responseHeadersMap", MapFormatter.table( httpResponseMeta.getResponseHeadersMap() ) );

    }

    @Test
    public void testWithPostWithCookies1() throws Exception {

        Map<String,String> parameters = Maps.newHashMap();

        parameters.put( "hello", "world" );
        parameters.put( "cat", "dog" );

        Map<String,String> cookies = Maps.newHashMap();
        cookies.put( "cookie0", "value0" );

        String link = "https://httpbin.org/post";
        HttpRequest httpRequest =
          cachedHttpRequestBuilder
            .post( link, parameters )
            .withRequestHeader( "X-foo", "bar" )
            .withCookies( cookies )
            .execute();

        String contentWithEncoding = httpRequest.getContentWithEncoding();
        HttpResponseMeta httpResponseMeta = httpRequest.getHttpResponseMeta();
        HttpRequestMeta httpRequestMeta = httpRequest.getHttpRequestMeta();

        assertNotNull( httpRequest );

        assertNotNull( httpResponseMeta );
        assertNotNull( httpResponseMeta.getResponseHeadersMap() );

        assertNotNull( httpRequestMeta );
        assertNotNull( httpRequestMeta.getRequestHeadersMap() );
        assertNotNull( httpRequestMeta.getCookies() );

        assertEquals( "bar", httpRequestMeta.getRequestHeadersMap().get( "X-foo" ) );
        assertEquals( "{cookie0=value0}", httpRequestMeta.getCookies().toString() );

        corporaAsserter.assertEquals( "testWithPostWithCookies1#requestHeadersMap", MapFormatter.table( httpRequest.getRequestHeadersMap() ) );
        corporaAsserter.assertEquals( "testWithPostWithCookies1#responseHeadersMap", MapFormatter.table( httpResponseMeta.getResponseHeadersMap() ) );

    }

}