package com.spinn3r.artemis.network.builder.proxies;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.BaseLauncherTest;
import com.spinn3r.artemis.init.config.TestResourcesConfigLoader;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.builder.HttpRequestMethod;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.network.init.NetworkService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ProxyRegistryWithNetworkConfigTest extends BaseLauncherTest {

    @Inject
    NetworkConfig networkConfig;

    @Inject
    HttpRequestBuilder httpRequestBuilder;

    @Inject
    Provider<ProxyRegistry> proxyRegistryProvider;

    @Override
    @Before
    public void setUp() throws Exception {

        System.setProperty( "waitforport.disabled", "true" );

        super.setUp( new TestResourcesConfigLoader(), NetworkService.class );
    }

    @Test
    public void testVerifyConfig() throws Exception {

        assertEquals( "NetworkConfig{userAgent='Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.130 Safari/537.36', blockSSL=false, defaultProxy='default', proxies={default=ProxySettings{host='127.0.0.1', port=9997, regex='.*', priority=1}, facebook=ProxySettings{host='127.0.0.1', port=9996, regex='https?://(([a-zA-Z0-9]+)\\.)?facebook\\.com', priority=10}}, defaultMaxContentLength=2000000, defaultReadTimeout=30000, defaultConnectTimeout=30000, requireProxy=true, requests={}}",
                      networkConfig.toString() );

    }

    @Test
    public void testUsingHttpRequestBuilderUsingFacebook() throws Exception {

        String link = "https://www.facebook.com/hashtag/TravelPics";

        HttpRequestMethod httpRequestMethod = httpRequestBuilder.get( link );
        HttpRequest httpRequest = httpRequestMethod.execute();

        assertEquals( "HTTP @ /127.0.0.1:9996", httpRequestMethod.getProxy().toString() );

    }

    @Test
    public void testUsingHttpRequestBuilderUsingDefault() throws Exception {

        String link = "https://www.cnn.com";

        HttpRequestMethod httpRequestMethod = httpRequestBuilder.get( link );
        HttpRequest httpRequest = httpRequestMethod.execute();

        assertEquals( "HTTP @ /127.0.0.1:9997", httpRequestMethod.getProxy().toString() );

        assertEquals( "http://127.0.0.1:9997", Proxies.format( httpRequestMethod.getProxy() ) );
    }

    @Test
    public void testFacebookProxyRegexp() throws Exception {

        assertEquals( "HTTP @ /127.0.0.1:9996",
                      proxyRegistryProvider.get().find( "http://facebook.com" ).getProxy().toString() );

        assertEquals( "HTTP @ /127.0.0.1:9996",
                      proxyRegistryProvider.get().find( "https://facebook.com" ).getProxy().toString() );

        assertEquals( "HTTP @ /127.0.0.1:9996",
                      proxyRegistryProvider.get().find( "http://www.facebook.com" ).getProxy().toString() );

        assertEquals( "HTTP @ /127.0.0.1:9996",
                      proxyRegistryProvider.get().find( "https://www.facebook.com" ).getProxy().toString() );

        assertEquals( "HTTP @ /127.0.0.1:9996",
                      proxyRegistryProvider.get().find( "https://www.facebook.com/hashtag/TravelPics" ).getProxy().toString() );

    }

}