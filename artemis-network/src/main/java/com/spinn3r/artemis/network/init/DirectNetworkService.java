package com.spinn3r.artemis.network.init;

import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.network.builder.ConfiguredHttpRequestBuilder;
import com.spinn3r.artemis.network.builder.DefaultHttpRequestBuilder;
import com.spinn3r.artemis.network.builder.DirectHttpRequestBuilder;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.fetcher.ContentFetcher;
import com.spinn3r.artemis.network.fetcher.DefaultContentFetcher;

/**
 * Provides network bindings but without proxy support.
 */

@Config( path = "network.conf",
         required = false,
         implementation = NetworkConfig.class )
public class DirectNetworkService extends BaseService {

    @Override
    public void init() {
        advertise( HttpRequestBuilder.class, DefaultHttpRequestBuilder.class );
        advertise( DirectHttpRequestBuilder.class, DefaultHttpRequestBuilder.class );
        advertise( ContentFetcher.class, DefaultContentFetcher.class );

    }

}
