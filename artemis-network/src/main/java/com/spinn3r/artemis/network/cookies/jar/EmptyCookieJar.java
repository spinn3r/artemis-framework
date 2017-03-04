package com.spinn3r.artemis.network.cookies.jar;

import com.spinn3r.artemis.network.cookies.CookieValueMap;

/**
 * Null object pattern for CookieJar
 */
public class EmptyCookieJar implements CookieJar {

    @Override
    public CookieValueMap getCookies() {
        return new CookieValueMap();
    }

    @Override
    public int size() {
        return 0;
    }

}
