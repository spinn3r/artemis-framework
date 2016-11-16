package com.spinn3r.artemis.network.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.network.ResourceRequestFactory;
import com.spinn3r.artemis.network.URLResourceRequest;
import com.spinn3r.artemis.network.cookies.Cookie;
import com.spinn3r.artemis.network.cookies.CookieMap;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 *
 */
public abstract class BaseHttpRequestMethod implements HttpRequestMethod {

    protected String etag = null;

    protected long modifiedSince = -1;

    protected int maxContentLength = URLResourceRequest.MAX_CONTENT_LENGTH;

    protected long readTimeout = ResourceRequestFactory.DEFAULT_READ_TIMEOUT;

    protected long connectTimeout = ResourceRequestFactory.DEFAULT_CONNECT_TIMEOUT;

    protected List<Cookie> cookies = Lists.newArrayList();

    protected Map<String,String> properties = new LinkedHashMap<>( 2 );

    protected Map<String,String> requestHeaders = new LinkedHashMap<>( 2 );

    protected Class<?> executor = null;

    protected boolean followRedirects = true;

    protected boolean followContentRedirects = true;

    protected String userAgent = URLResourceRequest.USER_AGENT;
    
    protected String acceptLanguage = URLResourceRequest.ACCEPT_LANGUAGE;

    @Override
    public HttpRequestMethod withUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    @Override
    public HttpRequestMethod withAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
        return this;
    }

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
    public HttpRequestMethod withProperties( Map<String,String> properties ) {
        checkNotNull( properties );
        this.properties.putAll( properties );
        return this;
    }

    @Override
    public HttpRequestMethod withMaxContentLength( int maxContentLength ) {
        this.maxContentLength = maxContentLength;
        return this;
    }

    @Override
    public HttpRequestMethod withCookie(String name, String value) {
        checkNotNull( name, "name" );
        checkNotNull( value, "value" );
        this.cookies.add( new Cookie(name, value) );
        return this;
    }

    @Override
    public HttpRequestMethod withCookie(Cookie cookie) {
        checkNotNull( cookie );
        this.cookies.add(cookie);
        return this;
    }

    @Override
    public HttpRequestMethod withCookies(Map<String,String> cookies) {

        checkNotNull( cookies );

        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            withCookie(entry.getKey(), entry.getValue());
        }

        return this;
    }

    @Override
    public HttpRequestMethod withCookies(CookieMap cookieMap) {
        checkNotNull( cookies );

        for (Cookie cookie : cookieMap.values()) {
            withCookie( cookie );
        }

        return this;

    }

    @Override
    public HttpRequestMethod withCookies(Collection<Cookie> cookies) {
        checkNotNull( cookies );

        for (Cookie cookie : cookies) {
            withCookie( cookie );
        }

        return this;

    }

    @Override
    public HttpRequestMethod withRequestHeader(String name, String value) {
        this.requestHeaders.put( name, value );
        return this;
    }

    @Override
    public HttpRequestMethod withRequestHeaders(Map<String, String> requestHeaders) {

        if ( requestHeaders != null )
            this.requestHeaders.putAll( requestHeaders );

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

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }

}
