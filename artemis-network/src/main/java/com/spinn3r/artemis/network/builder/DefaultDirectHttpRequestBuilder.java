package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsRegistry;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.network.validators.HttpResponseValidators;

/**
 *
 */
public class DefaultDirectHttpRequestBuilder extends DefaultHttpRequestBuilder implements DirectHttpRequestBuilder {

    @Inject
    public DefaultDirectHttpRequestBuilder(NetworkConfig networkConfig, HttpResponseValidators httpResponseValidators) {
        super( httpResponseValidators );

        RequestSettingsRegistry requestSettingsRegistry = new RequestSettingsRegistry( networkConfig.getRequests() );
        withRequestSettingsRegistry( requestSettingsRegistry );

    }

}
