package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableMap;

/**
 *
 */
public class DefaultHttpRequestMeta implements HttpRequestMeta {

    private final String resource;

    private final ImmutableMap<String,String> requestHeadersMap;

    public DefaultHttpRequestMeta(String resource, ImmutableMap<String, String> requestHeadersMap) {
        this.resource = resource;
        this.requestHeadersMap = requestHeadersMap;
    }

    @Override
    public String getResource() {
        return resource;
    }

    public ImmutableMap<String, String> getRequestHeadersMap() {
        return requestHeadersMap;
    }

    @Override
    public String toString() {
        return "DefaultHttpRequestMeta{" +
                 "resource='" + resource + '\'' +
                 ", requestHeadersMap=" + requestHeadersMap +
                 '}';
    }

}
