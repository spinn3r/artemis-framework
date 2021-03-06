package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.network.builder.cookies.ThreadLocalCookies;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsRegistry;
import com.spinn3r.artemis.network.cookies.jar.CookieJarManager;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.network.validators.HttpResponseValidators;

/**
 *
 */
public class DefaultDirectHttpRequestBuilder extends DefaultHttpRequestBuilder implements DirectHttpRequestBuilder {

    @Inject
    DefaultDirectHttpRequestBuilder(NetworkConfig networkConfig,
                                    UserAgentsConfig userAgentsConfig,
                                    UserAgentRandomizer userAgentRandomizer,
                                    HttpResponseValidators httpResponseValidators,
                                    Provider<CookieJarManager> cookieJarManagerProvider,
                                    ThreadLocalCookies threadLocalCookies) {
        super(networkConfig, userAgentsConfig, userAgentRandomizer, httpResponseValidators, cookieJarManagerProvider, threadLocalCookies);

        RequestSettingsRegistry requestSettingsRegistry = new RequestSettingsRegistry( networkConfig.getRequests() );
        withRequestSettingsRegistry( requestSettingsRegistry );

    }

}
