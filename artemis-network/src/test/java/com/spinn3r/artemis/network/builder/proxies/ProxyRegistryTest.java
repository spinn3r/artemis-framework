package com.spinn3r.artemis.network.builder.proxies;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ProxyRegistryTest {

    @Test
    public void testAdd() throws Exception {

        String host = "localhost";
        int port = 8080;

        PrioritizedProxyReference proxyReference0 = new PrioritizedProxyReference( "default", 1, ".*", host, port, null );
        PrioritizedProxyReference proxyReference1 = new PrioritizedProxyReference( "beta", 2, "https?://beta\\.com", host, port, null );
        PrioritizedProxyReference proxyReference2 = new PrioritizedProxyReference( "beta", 4, "https?://cappa\\.com", host, port, null );
        PrioritizedProxyReference proxyReference3 = new PrioritizedProxyReference( "beta", 3, "https?://delta\\.com", host, port, null );

        List<PrioritizedProxyReference> prioritizedProxyReferences = Lists.newArrayList();

        prioritizedProxyReferences.add( proxyReference0 );
        prioritizedProxyReferences.add( proxyReference1 );
        prioritizedProxyReferences.add( proxyReference2 );
        prioritizedProxyReferences.add( proxyReference3 );

        ProxyRegistry proxyRegistry = new ProxyRegistry( prioritizedProxyReferences );

        assertEquals( "[PrioritizedProxyReference{name='beta', priority=4, regex='https?://cappa\\.com', proxy=null} ProxyReference{host='localhost', port=8080, proxy=null}, PrioritizedProxyReference{name='beta', priority=3, regex='https?://delta\\.com', proxy=null} ProxyReference{host='localhost', port=8080, proxy=null}, PrioritizedProxyReference{name='beta', priority=2, regex='https?://beta\\.com', proxy=null} ProxyReference{host='localhost', port=8080, proxy=null}, PrioritizedProxyReference{name='default', priority=1, regex='.*', proxy=null} ProxyReference{host='localhost', port=8080, proxy=null}]",
                      proxyRegistry.getPrioritizedProxyReferences().toString() );

        assertEquals( "default", proxyRegistry.find( "http://cnn.com" ).getName() );
        assertEquals( "beta", proxyRegistry.find( "http://beta.com" ).getName() );
        assertEquals( "beta", proxyRegistry.find( "https://beta.com" ).getName() );
        assertEquals( "default", proxyRegistry.find( "https://beta2.com" ).getName() );

    }

}