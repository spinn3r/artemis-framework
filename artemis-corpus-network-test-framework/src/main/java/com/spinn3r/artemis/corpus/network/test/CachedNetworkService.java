package com.spinn3r.artemis.corpus.network.test;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.network.builder.DirectHttpRequestBuilder;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;

/**
 * A service for keeping requests local for testing.
 */
public class CachedNetworkService extends BaseService {

    @Override
    public void init() {
        replace( HttpRequestBuilder.class, CachedHttpRequestBuilder.class );
    }

}
