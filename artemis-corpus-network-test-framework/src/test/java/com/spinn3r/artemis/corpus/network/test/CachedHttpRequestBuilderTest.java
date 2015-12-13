package com.spinn3r.artemis.corpus.network.test;

import com.google.inject.Inject;
import com.spinn3r.artemis.corpus.test.CorporaAsserter;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpResponseMeta;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.util.text.MapFormatter;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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
        HttpResponseMeta httpResponseMeta = networkCorporaCache.meta( link );

        assertNotNull( httpRequest );
        corporaAsserter.assertEquals( "responseHeadersMap", MapFormatter.table( httpResponseMeta.getResponseHeadersMap() ) );

    }

}