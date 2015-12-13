package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableMap;

/**
 *
 */
public class DefaultHttpRequestMeta implements HttpRequestMeta {

    private final String resource;

    private final ImmutableMap<String,String> requestHeaders;

    public DefaultHttpRequestMeta(String resource, ImmutableMap<String, String> requestHeaders) {
        this.resource = resource;
        this.requestHeaders = requestHeaders;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public ImmutableMap<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    @Override
    public String toString() {
        return "DefaultHttpRequestMeta{" +
                 "resource='" + resource + '\'' +
                 ", requestHeaders=" + requestHeaders +
                 '}';
    }

}
