package com.spinn3r.artemis.network.builder.proxies;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ProxyRegistryTest {

    @Test
    public void testAdd() throws Exception {

        ProxyReference proxyReference0 = new ProxyReference( "default", 1, ".*", null );
        ProxyReference proxyReference1 = new ProxyReference( "beta", 2, "https?://beta\\.com", null );
        ProxyReference proxyReference2 = new ProxyReference( "beta", 4, "https?://cappa\\.com", null );
        ProxyReference proxyReference3 = new ProxyReference( "beta", 3, "https?://delta\\.com", null );

        List<ProxyReference> proxyReferenceList = Lists.newArrayList();

        proxyReferenceList.add( proxyReference0 );
        proxyReferenceList.add( proxyReference1 );
        proxyReferenceList.add( proxyReference2 );
        proxyReferenceList.add( proxyReference3 );

        ProxyRegistry proxyRegistry = new ProxyRegistry( proxyReferenceList );

        assertEquals( "[ProxyReference{name='beta', priority=4, regex='https?://cappa\\.com', proxy=null}, ProxyReference{name='beta', priority=3, regex='https?://delta\\.com', proxy=null}, ProxyReference{name='beta', priority=2, regex='https?://beta\\.com', proxy=null}, ProxyReference{name='default', priority=1, regex='.*', proxy=null}]",
                      proxyRegistry.getProxyReferences().toString() );

        assertEquals( "default", proxyRegistry.find( "http://cnn.com" ).getName() );
        assertEquals( "beta", proxyRegistry.find( "http://beta.com" ).getName() );
        assertEquals( "beta", proxyRegistry.find( "https://beta.com" ).getName() );
        assertEquals( "default", proxyRegistry.find( "https://beta2.com" ).getName() );

    }

}