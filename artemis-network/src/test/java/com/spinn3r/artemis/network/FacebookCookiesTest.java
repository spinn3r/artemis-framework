package com.spinn3r.artemis.network;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.network.init.NetworkConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class FacebookCookiesTest extends BaseLauncherTest {

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Inject
    NetworkConfig networkConfig;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp(DirectNetworkService.class);
    }

    @Test
    @Ignore
    public void testCookieJarUsage() throws Exception {

        HttpRequest httpRequest
          = httpRequestBuilder
              .withUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36")
              .get("https://www.facebook.com/barackobama")
              .withFollowContentRedirects(false)
              .execute();

        assertEquals("{datr=bd04V9F4B64dMqvqlehNmNe6}", httpRequest.getCookies().toString() );

    }

}
