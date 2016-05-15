package com.spinn3r.artemis.network.cookies.jar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spinn3r.artemis.network.cookies.CookieValueStore;

/**
 * Reference to a cookie jar backing cookies.  If this is backed by a path
 * we load cookies off disk.  We can also store cookies inline as a direct 'store'
 * within the original JSON for testing, etc.
 */
public class CookieJarReference {

    private String regex = null;

    private String path = null;

    private CookieValueStore store = null;

    public String getPath() {
        return path;
    }

    public String getRegex() {
        return regex;
    }

    public CookieValueStore getStore() {
        return store;
    }

    @Override
    public String toString() {
        return "CookieJarReference{" +
                 "regex='" + regex + '\'' +
                 ", path='" + path + '\'' +
                 ", store=" + store +
                 '}';
    }

}
