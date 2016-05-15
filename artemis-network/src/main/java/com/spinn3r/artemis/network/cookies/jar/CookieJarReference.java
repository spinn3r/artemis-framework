package com.spinn3r.artemis.network.cookies.jar;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Reference to a cookie jar backing cookies.
 */
public class CookieJarReference {

    private String path = "";

    private String regex = "";

    public CookieJarReference(@JsonProperty("path") String path,
                              @JsonProperty("regex") String regex) {
        this.path = path;
        this.regex = regex;
    }

    public String getPath() {
        return path;
    }

    public String getRegex() {
        return regex;
    }

    @Override
    public String toString() {
        return "CookieJarReference{" +
                 "path='" + path + '\'' +
                 ", regex='" + regex + '\'' +
                 '}';
    }

}
