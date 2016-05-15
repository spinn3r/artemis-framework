package com.spinn3r.artemis.network.cookies.jar;

import com.spinn3r.artemis.network.cookies.jar.config.CookieJarConfig;

/**
 * Allows us to map URLs to cookie jars that support cookies for this URL.
 */
public class CookieJarManager {

    private final CookieJarConfig cookieJarConfig;

    public CookieJarManager(CookieJarConfig cookieJarConfig) {
        this.cookieJarConfig = cookieJarConfig;
    }

    public CookieJar getCookieJar(String link ) {

        return new EmptyCookieJar();

    }

}
