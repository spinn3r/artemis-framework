package com.spinn3r.artemis.network;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.network.init.NetworkConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 */
@Ignore
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
    public void testCookies() throws Exception {

        // FIXME: how does facebook know that we don't support cookies yet... WTF .. is this just the default?

        // FIXME: why is facebook sending a noscript HTTP cookie.. I think it's
        // fingerprinting my client and realizing it's not a real browser
        //
        // FIXME: since I can't actually reply the full HTTP request

        System.out.printf("%s\n", networkConfig.getUserAgent());

        HttpRequest httpRequest
          = httpRequestBuilder
              .withUserAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36")
              .get("https://www.facebook.com/barackobama")
//              .withProxy("http://localhost:8080")
//              .get("http://www.cnn.com")
              .withFollowContentRedirects(false)
              .withRequestHeader("Upgrade-Insecure-Requests", "1")
              .withRequestHeader("Accept-Language", "en-US,en;q=0.8")
              .withRequestHeader("Authority", "www.facebook.com")
              .withRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
              .execute();

        httpRequest.getContentWithEncoding();

        System.out.printf("%s\n", httpRequest.getResponseHeadersMap());

        ImmutableList<String> cookies = httpRequest.getResponseHeadersMap().get("Set-Cookie");

        for (String cookie : cookies) {
            System.out.printf("%s\n", cookie);
        }

    }

}
