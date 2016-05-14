package com.spinn3r.artemis.network.cookies.jar;

import com.spinn3r.artemis.network.cookies.Cookie;

import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
class CookieHolder {

    private final AtomicReference<Cookie> cookieReference;

    public CookieHolder(Cookie cookie){
        this.cookieReference = new AtomicReference<>(cookie);
    }

}
