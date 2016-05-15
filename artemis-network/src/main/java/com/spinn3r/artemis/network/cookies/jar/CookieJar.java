package com.spinn3r.artemis.network.cookies.jar;

import com.google.common.collect.ImmutableList;
import com.spinn3r.artemis.network.cookies.Cookie;
import com.spinn3r.artemis.network.cookies.CookieMap;
import com.spinn3r.artemis.network.cookies.CookieValueMap;

/**
 * For a given site, we hold the cookies we're supposed to use for this site.
 */
public interface CookieJar {

    /**
     * Get the cookies we're supposed to be using for this request.
     *
     * @return
     */
    CookieValueMap getCookies();

    enum Type {

        /**
         *
         */
        ROTATED
    }

}
