package com.spinn3r.artemis.network.cookies.jar;

import java.util.regex.Pattern;

/**
 *
 */
public class CookieJarHolder {

    private final CookieJarReference cookieJarReference;

    private final Pattern pattern;

    private final CookieJar cookieJar;

    public CookieJarHolder(CookieJarReference cookieJarReference, Pattern pattern, CookieJar cookieJar) {
        this.cookieJarReference = cookieJarReference;
        this.pattern = pattern;
        this.cookieJar = cookieJar;
    }

    public CookieJarReference getCookieJarReference() {
        return cookieJarReference;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public CookieJar getCookieJar() {
        return cookieJar;
    }

    @Override
    public String toString() {
        return "CookieJarHolder{" +
                 "cookieJarReference=" + cookieJarReference +
                 ", pattern=" + pattern +
                 ", cookieJar=" + cookieJar +
                 '}';
    }

}
