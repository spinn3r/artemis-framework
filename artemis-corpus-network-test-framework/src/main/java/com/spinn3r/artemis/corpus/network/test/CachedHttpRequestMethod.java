package com.spinn3r.artemis.corpus.network.test;

import com.google.common.collect.Maps;
import com.spinn3r.artemis.network.Cookie;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.artemis.network.builder.HttpRequestMeta;
import com.spinn3r.artemis.network.builder.HttpRequestMethod;

import java.net.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class CachedHttpRequestMethod implements HttpRequestMethod {

    protected final CachedHttpRequestBuilder cachedHttpRequestBuilder;

    protected final String resource;

    protected String content;

    protected Map<String,String> cookies = null;

    protected Map<String,String> requestHeaders = new LinkedHashMap<>();

    protected Class<?> executor = null;

    public CachedHttpRequestMethod(CachedHttpRequestBuilder cachedHttpRequestBuilder, String resource) {
        this.cachedHttpRequestBuilder = cachedHttpRequestBuilder;
        this.resource = resource;
    }

    @Override
    public HttpRequestMethod withEtag(String etag) {
        return this;
    }

    @Override
    public HttpRequestMethod ifModifiedSince(long modifiedSince) {
        return this;
    }

    @Override
    public HttpRequestMethod withProperty(String name, String value) {
        return this;
    }

    @Override
    public HttpRequestMethod withRequestHeader(String name, String value) {
        this.requestHeaders.put( name, value );
        return this;
    }

    @Override
    public HttpRequestMethod withRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
        return this;
    }

    @Override
    public HttpRequestMethod withMaxContentLength(int maxContentLength) {
        return this;
    }

    @Override
    public HttpRequestMethod withCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    @Override
    public HttpRequestMethod withCookieIndex(Map<String, Cookie> cookies) {
        return this;
    }

    @Override
    public HttpRequestMethod withExecutor(Class<?> executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public HttpRequestMethod withConnectTimeout(long timeout) {
        return this;
    }

    @Override
    public HttpRequestMethod withReadTimeout(long timeout) {
        return this;
    }

    @Override
    public HttpRequestMethod withFollowRedirects(boolean followRedirects) {
        return this;
    }

    @Override
    public HttpRequestMethod withFollowContentRedirects(boolean followContentRedirects) {
        return this;
    }

    @Override
    public HttpRequest execute() throws NetworkException {

        this.content = cachedHttpRequestBuilder.networkCorporaCache.fetch( resource, requestHeaders, cookies );

        return new CachedHttpRequest( this );

    }

    @Override
    public Map<String, String> getProperties() {
        return Maps.newHashMap();
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public HttpRequestMeta getHttpRequestMeta() {
        return null;
    }

    @Override
    public boolean getFollowContentRedirects() {
        return true;
    }

    @Override
    public Class<?> getExecutor() {
        return executor;
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