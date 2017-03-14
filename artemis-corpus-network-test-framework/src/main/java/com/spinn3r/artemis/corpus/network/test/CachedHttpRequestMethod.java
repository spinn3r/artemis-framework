package com.spinn3r.artemis.corpus.network.test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.BaseHttpRequestMethod;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestMethod;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;
import com.spinn3r.artemis.network.cookies.Cookies;
import com.spinn3r.artemis.util.crypto.SHA1;
import com.spinn3r.artemis.util.misc.Base64;

import java.net.Proxy;
import java.util.Map;

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
                                 cookies,
                                 outputContent,
                                 outputContentEncoding,
                                 outputContentType);

        this.content = cachedContent.getContent();

        return new CachedHttpRequest( this,
                                      cachedContent.getHttpRequestMeta(),
                                      cachedContent.getHttpResponseMeta(),
                                      Cookies.toMap( cookies ),
                                      executor );

    }

    public boolean isCached() {
        return cachedHttpRequestBuilder.networkCorporaCache.contains(computeKey());
    }

    private String computeKey() {

        StringBuilder data = new StringBuilder();

        if ( ! httpMethod.equals( HttpMethod.GET ) ) {
            data.append( httpMethod.toString() );
        }

        data.append( resource );

        if ( requestHeaders != null && requestHeaders.size() > 0 ) {
            data.append( requestHeaders.toString() );
        }

        if ( cookies != null && cookies.size() > 0 ) {
            data.append( canonicalize(Cookies.toMap(cookies)).toString() );
        }

        if ( outputContent != null ) {
            data.append( outputContent );
            data.append( outputContentEncoding );
            data.append( outputContentType );
        }

        return Base64.encode(SHA1.encode(data.toString() ) );

    }

    private Map<String,String> canonicalize(ImmutableMap<String, String> cookies ) {
        Map<String,String> result = Maps.newTreeMap();
        result.putAll(cookies);
        return result;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public HttpRequestMethod withProxy(ProxyReference proxyReference) {
        return this;
    }

    @Override
    public Proxy getProxy() {
        return null;
    }

}
