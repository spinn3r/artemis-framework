package com.spinn3r.artemis.network.builder;

import com.google.common.collect.Maps;
import com.spinn3r.artemis.network.Cookie;
import com.spinn3r.artemis.network.ResourceRequestFactory;
import com.spinn3r.artemis.network.URLResourceRequest;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public abstract class BaseHttpRequestMethod implements HttpRequestMethod {

    protected String etag = null;

    protected long modifiedSince = -1;

    protected int maxContentLength = URLResourceRequest.MAX_CONTENT_LENGTH;

    protected long readTimeout = ResourceRequestFactory.DEFAULT_READ_TIMEOUT;

    protected long connectTimeout = ResourceRequestFactory.DEFAULT_CONNECT_TIMEOUT;

    protected Map<String,String> cookies = new HashMap<>();

    protected Map<String,String> properties = new LinkedHashMap<>( 2 );

    protected Map<String,String> requestHeaders = new LinkedHashMap<>( 2 );

    protected Class<?> executor = null;

    protected boolean followRedirects = true;

    protected boolean followContentRedirects = true;

    @Override
    public HttpRequestMethod withEtag(String etag) {
        this.etag = etag;
        return this;
    }

    @Override
    public HttpRequestMethod ifModifiedSince( long modifiedSince ) {
        this.modifiedSince = modifiedSince;
        return this;
    }

    @Override
    public HttpRequestMethod withProperty(String name, String value) {
        properties.put( name, value );
        return this;
    }

    @Override
    public HttpRequestMethod withMaxContentLength( int maxContentLength ) {
        this.maxContentLength = maxContentLength;
        return this;
    }

    @Override
    public HttpRequestMethod withCookies( Map<String,String> cookies ) {
        this.cookies = cookies;
        return this;
    }

    @Override
    public HttpRequestMethod withCookieIndex( Map<String,Cookie> cookies ) {

        Map<String,String> cookieMap = Maps.newHashMap();

        for (Map.Entry<String, Cookie> entry : cookies.entrySet()) {
            cookieMap.put( entry.getKey(), entry.getValue().getValue() );
        }

        this.cookies = cookieMap;
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
    public HttpRequestMethod withConnectTimeout(long timeout) {
        this.connectTimeout = timeout;
        return this;
    }

    @Override
    public HttpRequestMethod withReadTimeout(long timeout) {
        this.readTimeout = timeout;
        return this;
    }

    @Override
    public HttpRequestMethod withExecutor(Class<?> executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public HttpRequestMethod withFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    @Override
    public HttpRequestMethod  withFollowContentRedirects( boolean followContentRedirects ) {
        this.followContentRedirects = followContentRedirects;
        return this;
    }

    @Override
    public boolean getFollowContentRedirects() {
        return this.followContentRedirects;
    }

    @Override
    public Class<?> getExecutor() {
        return executor;
    }

}
