package com.spinn3r.artemis.corpus.network.test;

import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.BaseHttpRequestMethod;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestMethod;

import java.net.Proxy;

/**
 *
 */
public class CachedHttpRequestMethod extends BaseHttpRequestMethod implements HttpRequestMethod {

    protected final CachedHttpRequestBuilder cachedHttpRequestBuilder;

    protected final HttpMethod httpMethod;

    protected final String resource;

    protected String outputContent;

    protected String outputContentEncoding;

    protected String outputContentType;

    protected String content;

    public CachedHttpRequestMethod(CachedHttpRequestBuilder cachedHttpRequestBuilder, HttpMethod httpMethod, String resource) {
        this.cachedHttpRequestBuilder = cachedHttpRequestBuilder;
        this.httpMethod = httpMethod;
        this.resource = resource;
    }

    public CachedHttpRequestMethod(CachedHttpRequestBuilder cachedHttpRequestBuilder, HttpMethod httpMethod, String resource, String outputContent, String outputContentEncoding, String outputContentType) {
        this.cachedHttpRequestBuilder = cachedHttpRequestBuilder;
        this.httpMethod = httpMethod;
        this.resource = resource;
        this.outputContent = outputContent;
        this.outputContentEncoding = outputContentEncoding;
        this.outputContentType = outputContentType;
    }

    @Override
    public HttpRequest execute() throws NetworkException {

        CachedContent cachedContent =
          cachedHttpRequestBuilder.networkCorporaCache
            .fetchCachedContent( httpMethod,
                                 resource,
                                 ImmutableMap.copyOf( requestHeaders ),
                                 ImmutableMap.copyOf( cookies ),
                                 outputContent,
                                 outputContentEncoding,
                                 outputContentType);

        this.content = cachedContent.getContent();

        return new CachedHttpRequest( this,
                                      cachedContent.getHttpRequestMeta(),
                                      cachedContent.getHttpResponseMeta(),
                                      ImmutableMap.copyOf( cookies ),
                                      executor );

    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public HttpRequestMethod withProxy(Proxy proxy) {
        return this;
    }

    @Override
    public Proxy getProxy() {
        return null;
    }

}
