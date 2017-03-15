package com.spinn3r.artemis.network.cookies.jar;

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

    /**
     * The number of cookies backing this cookie jar.
     */
    int size();

    enum Type {

        /**
         *
         */
        ROTATED
    }

}
