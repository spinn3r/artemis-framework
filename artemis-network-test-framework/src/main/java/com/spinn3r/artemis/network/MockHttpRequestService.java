package com.spinn3r.artemis.network;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.network.builder.DirectHttpRequestBuilder;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;

/**
 *
 */
public class MockHttpRequestService extends BaseService {

    @Override
    public void init() {
        MockHttpRequestBuilder instance = new MockHttpRequestBuilder();
        advertise( HttpRequestBuilder.class, instance );
        advertise( DirectHttpRequestBuilder.class, instance );
        advertise( MockHttpRequestBuilder.class, instance );
    }
}
