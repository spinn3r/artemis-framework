package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.network.builder.listener.RequestListeners;
import com.spinn3r.artemis.network.builder.proxies.ProxyRegistry;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsRegistry;
import com.spinn3r.artemis.network.init.NetworkConfig;

import java.net.Proxy;

/**
 *
 */
public class ConfiguredHttpRequestBuilder extends DefaultHttpRequestBuilder {

    @Inject
    ConfiguredHttpRequestBuilder(NetworkConfig config,
                                 Provider<Proxy> proxyProvider,
                                 Provider<ProxyRegistry> proxyRegistryProvider,
                                 Provider<RequestSettingsRegistry> requestSettingsRegistryProvider,
                                 RequestListeners requestListeners ) {

        if ( config.getUserAgent() == null ) {
            throw new RuntimeException( "No user agent: " + config );
        }

        if ( config.getDefaultProxy() != null && proxyProvider.get() == null ) {
            throw new RuntimeException( "Proxy configured but no proxy object: " + config );
        }

        withRequireProxy( config.getRequireProxy() );
        withUserAgent( config.getUserAgent() );
        withProxy( proxyProvider.get() );
        withProxyRegistry( proxyRegistryProvider.get() );
        withRequestListeners( requestListeners );
        withDefaultMaxContentLength( config.getDefaultMaxContentLength() );
        withDefaultConnectTimeout( config.getDefaultConnectTimeout() );
        withDefaultReadTimeout( config.getDefaultReadTimeout() );

    }

}
