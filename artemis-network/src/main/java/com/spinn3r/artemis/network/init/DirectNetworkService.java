package com.spinn3r.artemis.network.init;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.Config;
import com.spinn3r.artemis.network.builder.*;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsRegistry;
import com.spinn3r.artemis.network.fetcher.ContentFetcher;
import com.spinn3r.artemis.network.fetcher.DefaultContentFetcher;

/**
 * Provides network bindings but without proxy support.
 */

@Config( path = "network.conf",
         required = false,
         implementation = NetworkConfig.class )
public class DirectNetworkService extends BaseService {

    private final NetworkConfig networkConfig;

    private final DefaultHttpRequestBuilder defaultHttpRequestBuilder;

    @Inject
    DirectNetworkService(NetworkConfig networkConfig, DefaultHttpRequestBuilder defaultHttpRequestBuilder) {
        this.networkConfig = networkConfig;
        this.defaultHttpRequestBuilder = defaultHttpRequestBuilder;
    }

    @Override
    public void init() {

        RequestSettingsRegistry requestSettingsRegistry = new RequestSettingsRegistry( networkConfig.getRequests() );
        defaultHttpRequestBuilder.withRequestSettingsRegistry( requestSettingsRegistry );

        advertise( HttpRequestBuilder.class, defaultHttpRequestBuilder );
        advertise( DirectHttpRequestBuilder.class, defaultHttpRequestBuilder );
        advertise( ContentFetcher.class, DefaultContentFetcher.class );

    }

}
