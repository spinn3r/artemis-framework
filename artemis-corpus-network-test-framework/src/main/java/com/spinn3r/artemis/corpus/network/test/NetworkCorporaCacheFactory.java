package com.spinn3r.artemis.corpus.network.test;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.advertisements.Caller;
import com.spinn3r.artemis.network.builder.DirectHttpRequestBuilder;

/**
 *
 */
public class NetworkCorporaCacheFactory {

    private final DirectHttpRequestBuilder directHttpRequestBuilder;

    @Inject
    NetworkCorporaCacheFactory(DirectHttpRequestBuilder directHttpRequestBuilder) {
        this.directHttpRequestBuilder = directHttpRequestBuilder;
    }

    public NetworkCorporaCache create( Class<?> parent ) {
        return new NetworkCorporaCache( directHttpRequestBuilder, parent );
    }

}
