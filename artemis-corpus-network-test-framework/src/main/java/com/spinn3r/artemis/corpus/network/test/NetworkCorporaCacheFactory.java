package com.spinn3r.artemis.corpus.network.test;

import com.google.inject.Inject;
import com.spinn3r.artemis.network.builder.DefaultDirectHttpRequestBuilder;
import com.spinn3r.artemis.network.init.NetworkConfig;

/**
 *
 */
public class NetworkCorporaCacheFactory {

    private final NetworkConfig networkConfig;

    private final DefaultDirectHttpRequestBuilder directHttpRequestBuilder;

    @Inject
    NetworkCorporaCacheFactory(NetworkConfig networkConfig, DefaultDirectHttpRequestBuilder directHttpRequestBuilder) {
        this.networkConfig = networkConfig;
        this.directHttpRequestBuilder = directHttpRequestBuilder;
    }

    public NetworkCorporaCache create( Class<?> parent ) {
        return new NetworkCorporaCache(networkConfig, directHttpRequestBuilder, parent );
    }

}
