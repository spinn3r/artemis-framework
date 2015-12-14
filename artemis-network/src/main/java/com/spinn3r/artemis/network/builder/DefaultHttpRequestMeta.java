package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableMap;

/**
 *
 */
public class DefaultHttpRequestMeta implements HttpRequestMeta {

    private final String resource;

    private final ImmutableMap<String,String> requestHeadersMap;

    private final String outputContent;

    private final String outputContentEncoding;

    private final String outputContentType;

    public DefaultHttpRequestMeta(String resource, ImmutableMap<String, String> requestHeadersMap) {
        this.resource = resource;
        this.requestHeadersMap = requestHeadersMap;
        this.outputContent = null;
        this.outputContentEncoding = null;
        this.outputContentType = null;
    }

    public DefaultHttpRequestMeta(String resource, ImmutableMap<String, String> requestHeadersMap, String outputContent, String outputContentEncoding, String outputContentType) {
        this.resource = resource;
        this.requestHeadersMap = requestHeadersMap;
        this.outputContent = outputContent;
        this.outputContentEncoding = outputContentEncoding;
        this.outputContentType = outputContentType;
    }

    @Override
    public String getResource() {
        return resource;
    }

    public ImmutableMap<String, String> getRequestHeadersMap() {
        return requestHeadersMap;
    }

    public String getOutputContent() {
        return outputContent;
    }

    public String getOutputContentEncoding() {
        return outputContentEncoding;
    }

    public String getOutputContentType() {
        return outputContentType;
    }

    @Override
    public String toString() {
        return "DefaultHttpRequestMeta{" +
                 "resource='" + resource + '\'' +
                 ", requestHeadersMap=" + requestHeadersMap +
                 ", outputContent='" + outputContent + '\'' +
                 ", outputContentEncoding='" + outputContentEncoding + '\'' +
                 ", outputContentType='" + outputContentType + '\'' +
                 '}';
    }

}
