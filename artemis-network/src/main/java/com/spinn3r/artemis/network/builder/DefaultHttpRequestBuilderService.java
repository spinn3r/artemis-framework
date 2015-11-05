package com.spinn3r.artemis.network.builder;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.network.fetcher.ContentFetcher;
import com.spinn3r.artemis.network.fetcher.DefaultContentFetcher;

/**
 * Create a new DefaultHttpRequestBuilder that can be used.
 */
public class DefaultHttpRequestBuilderService extends BaseService {

    @Override
    public void start() throws Exception {
        advertise( HttpRequestBuilder.class, DefaultHttpRequestBuilder.class );
        advertise( ContentFetcher.class, DefaultContentFetcher.class );
    }

    @Override
    public void stop() throws Exception {

    }
}
