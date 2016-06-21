package com.spinn3r.artemis.network;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.network.init.DirectNetworkService;
import com.spinn3r.artemis.network.init.NetworkConfig;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class FacebookCookiesWithCookieManagerTest extends BaseLauncherTest {

    @Inject
    DirectNetworkService directNetworkService;

    @Inject
    NetworkConfig networkConfig;

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp(DirectNetworkService.class);

        networkConfig.setCookieManagerEnabled(true);
        directNetworkService.start();
    }

    @Test
    public void testCookieJarUsageWithCookieManager() throws Exception {

        List<String> cookies = getCookies(URI.create("http://facebook.com"));

        assertEquals(1, cookies.size());
        assertEquals("datr=bd04V9F4B64dMqvqlehNmNe6", cookies.iterator().next());

    }

    private static List<String> getCookies(URI uri) throws IOException, URISyntaxException {

        CookieHandler cookieHandler = CookieHandler.getDefault();
        Map<String, List<String>> allCookies = cookieHandler.get(uri, new HashMap<>());
        return allCookies.get("Cookie");
    }
}
