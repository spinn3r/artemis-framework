package com.spinn3r.artemis.corpus.network.test;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class CachedHttpRequestBuilderTest extends BaseLauncherTest {

    @Inject
    CachedHttpRequestBuilder cachedHttpRequestBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp( DirectNetworkService.class,
                     CachedNetworkService.class );
    }

    @Test
    public void testGet() throws Exception {

        HttpRequest httpRequest = cachedHttpRequestBuilder.get( "http://cnn.com" ).execute();
        String contentWithEncoding = httpRequest.getContentWithEncoding();

    }

}