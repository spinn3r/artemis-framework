package com.spinn3r.artemis.network;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.builder.HttpRequestMethod;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 *
 */
public class RequestSettingsTest extends BaseLauncherTest {

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp( DirectNetworkService.class ) ;
    }
    @Test
    public void testFetchingContentWithDefaultFollowContent() throws Exception {

        String link = "http://cnn.com";

        // this isn't actually working.. I'm not sure what they could be rejecting
        // my requests on... I'm not sending cookies but neither is curl...

        HttpRequestMethod httpRequestMethod = httpRequestBuilder .get( link );

        HttpRequest httpRequest = httpRequestMethod.execute();

        assertTrue( httpRequestMethod.getFollowContentRedirects() );

        String contentWithEncoding = httpRequest.getContentWithEncoding();

    }

    @Test
    public void testFetchingContentWithoutFollowContent() throws Exception {

        String link = "http://vk.com/cliqque";

        // this isn't actually working.. I'm not sure what they could be rejecting
        // my requests on... I'm not sending cookies but neither is curl...

        HttpRequestMethod httpRequestMethod = httpRequestBuilder .get( link );

        HttpRequest httpRequest = httpRequestMethod.execute();

        assertFalse( httpRequestMethod.getFollowContentRedirects() );

        String contentWithEncoding = httpRequest.getContentWithEncoding();

    }

}
