package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.network.builder.cookies.ThreadLocalCookies;
import com.spinn3r.artemis.network.builder.listener.RequestListeners;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;
import com.spinn3r.artemis.network.builder.proxies.ProxyRegistry;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsRegistry;
import com.spinn3r.artemis.network.cookies.jar.CookieJarManager;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.network.validators.HttpResponseValidators;

/**
 *
 */
public class ConfiguredHttpRequestBuilder extends DefaultHttpRequestBuilder {

    @Inject
    ConfiguredHttpRequestBuilder(NetworkConfig networkConfig,
                                 UserAgentsConfig userAgentsConfig,
                                 UserAgentRandomizer userAgentRandomizer,
                                 Provider<ProxyReference> proxyReferenceProvider,
                                 Provider<ProxyRegistry> proxyRegistryProvider,
                                 Provider<RequestSettingsRegistry> requestSettingsRegistryProvider,
                                 RequestListeners requestListeners,
                                 HttpResponseValidators httpResponseValidators,
                                 Provider<CookieJarManager> cookieJarManagerProvider,
                                 ThreadLocalCookies threadLocalCookies) {

        super(networkConfig, userAgentsConfig, userAgentRandomizer, httpResponseValidators, cookieJarManagerProvider, threadLocalCookies);

        if ( networkConfig.getUserAgent() == null ) {
            throw new RuntimeException( "No user agent: " + networkConfig );
        }

        if ( networkConfig.getDefaultProxy() != null && proxyReferenceProvider.get() == null ) {
            throw new RuntimeException( "Default proxy configured but no proxy definitions found: " + networkConfig );
        }

        withRequireProxy( networkConfig.getRequireProxy() );
        withUserAgent( networkConfig.getUserAgent() );

        if ( proxyReferenceProvider.get() != null ) {
            withProxy( proxyReferenceProvider.get() );
        }

        withProxyRegistry( proxyRegistryProvider.get() );
        withRequestSettingsRegistry( requestSettingsRegistryProvider.get() );
        withRequestListeners( requestListeners );
        withDefaultMaxContentLength( networkConfig.getDefaultMaxContentLength() );
        withDefaultConnectTimeout( networkConfig.getDefaultConnectTimeout() );
        withDefaultReadTimeout( networkConfig.getDefaultReadTimeout() );

    }

}
