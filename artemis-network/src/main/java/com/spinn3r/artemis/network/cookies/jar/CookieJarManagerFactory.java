package com.spinn3r.artemis.network.cookies.jar;

import com.google.inject.Inject;
import com.spinn3r.artemis.init.config.ConfigLoader;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class CookieJarManagerFactory {

    private final ConfigLoader configLoader;

    @Inject
    CookieJarManagerFactory(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    public CookieJarManager create(List<CookieJarReference> cookieJarReferences) throws IOException {
        return new CookieJarManager(configLoader, cookieJarReferences);
    }

}
