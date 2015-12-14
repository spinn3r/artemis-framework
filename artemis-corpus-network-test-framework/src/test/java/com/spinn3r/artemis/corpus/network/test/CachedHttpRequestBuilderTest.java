package com.spinn3r.artemis.corpus.network.test;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.spinn3r.artemis.corpus.test.CorporaAsserter;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestMeta;
import com.spinn3r.artemis.network.builder.HttpResponseMeta;
import com.spinn3r.artemis.network.init.DirectNetworkService;
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

    CorporaAsserter corporaAsserter = new CorporaAsserter( getClass() );
    NetworkCorporaCache networkCorporaCache;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp( DirectNetworkService.class,
                     CachedNetworkService.class );

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

        String link = "https://httpbin.org/get";
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

}