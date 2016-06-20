package com.spinn3r.artemis.network.builder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;

/**
 *
 */
public class DefaultHttpRequestMeta implements HttpRequestMeta {

    private final String resource;

    private final ImmutableMap<String,String> requestHeadersMap;

    private final ImmutableMap<String,String> cookies;

    private final String outputContent;

    private final String outputContentEncoding;

    private final String outputContentType;

    public DefaultHttpRequestMeta(String resource, ImmutableMap<String, String> requestHeadersMap, ImmutableMap<String, String> cookies) {
        this.resource = resource;
        this.requestHeadersMap = requestHeadersMap;
        this.cookies = cookies;
        this.outputContent = null;
        this.outputContentEncoding = null;
        this.outputContentType = null;
    }

    public DefaultHttpRequestMeta( @JsonProperty("resource") String resource,
                                   @JsonProperty("requestHeadersMap") ImmutableMap<String, String> requestHeadersMap,
                                   @JsonProperty("cookies") ImmutableMap<String, String> cookies,
                                   @JsonProperty("outputContent") String outputContent,
                                   @JsonProperty("outputContentEncoding") String outputContentEncoding,
                                   @JsonProperty("outputContentType") String outputContentType) {
        this.resource = resource;
        this.requestHeadersMap = requestHeadersMap;
        this.cookies = cookies;
        this.outputContent = outputContent;
        this.outputContentEncoding = outputContentEncoding;
        this.outputContentType = outputContentType;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public ImmutableMap<String, String> getRequestHeadersMap() {
        return requestHeadersMap;
    }

    @Override
    public ImmutableMap<String, String> getCookies() {
        return cookies;
    }

    @Override
    public String getOutputContent() {
        return outputContent;
    }

    @Override
    public String getOutputContentEncoding() {
        return outputContentEncoding;
    }

    @Override
    public String getOutputContentType() {
        return outputContentType;
    }

    @Override
    public String toString() {
        return "DefaultHttpRequestMeta{" +
                 "resource='" + resource + '\'' +
                 ", requestHeadersMap=" + requestHeadersMap +
                 ", cookies=" + cookies +
                 ", outputContent='" + outputContent + '\'' +
                 ", outputContentEncoding='" + outputContentEncoding + '\'' +
                 ", outputContentType='" + outputContentType + '\'' +
                 '}';
    }

}
