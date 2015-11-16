package com.spinn3r.artemis.network.init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

/**
 * Configurations for HTTP requests defined by regex.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestSettings {

    private String regex;

    private int priority;

    private String userAgent = null;

    private Map<String,String> cookies = null;

    private Map<String,String> requestHeaders = null;

    private Long connectTimeout = null;

    private Long readTimeout = null;

    private Boolean followRedirects = null;

    private Boolean followContentRedirects = null;

    public String getRegex() {
        return regex;
    }

    public int getPriority() {
        return priority;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public Long getConnectTimeout() {
        return connectTimeout;
    }

    public Long getReadTimeout() {
        return readTimeout;
    }

    public Boolean getFollowRedirects() {
        return followRedirects;
    }

    public Boolean getFollowContentRedirects() {
        return followContentRedirects;
    }

    @Override
    public String toString() {
        return "Request{" +
                 "regex='" + regex + '\'' +
                 ", userAgent='" + userAgent + '\'' +
                 ", cookies=" + cookies +
                 ", requestHeaders=" + requestHeaders +
                 ", connectTimeout=" + connectTimeout +
                 ", readTimeout=" + readTimeout +
                 ", followRedirects=" + followRedirects +
                 ", followContentRedirects=" + followContentRedirects +
                 '}';
    }

}
