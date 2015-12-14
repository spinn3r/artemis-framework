package com.spinn3r.artemis.corpus.network.test;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Set;

/**
 *
 */
public class CachedHttpRequest implements HttpRequest {

    private final CachedHttpRequestMethod cachedHttpRequestMethod;

    private final HttpRequestMeta httpRequestMeta;

    private final HttpResponseMeta httpResponseMeta;

    private final ImmutableMap<String, String> cookies;

    private final Class<?> executor;

    public CachedHttpRequest(CachedHttpRequestMethod cachedHttpRequestMethod, HttpRequestMeta httpRequestMeta, HttpResponseMeta httpResponseMeta, ImmutableMap<String, String> cookies, Class<?> executor) {
        this.cachedHttpRequestMethod = cachedHttpRequestMethod;
        this.httpRequestMeta = httpRequestMeta;
        this.httpResponseMeta = httpResponseMeta;
        this.cookies = cookies;
        this.executor = executor;
    }

    @Override
    public String getContentWithEncoding() throws NetworkException {
        return cachedHttpRequestMethod.content;
    }

    @Override
    public InputStream getInputStream() throws NetworkException {
        return new ByteArrayInputStream( getContentWithEncoding().getBytes( Charsets.UTF_8) );
    }

    @Override
    public InputStream getDirectInputStream() throws NetworkException {
        return getInputStream();
    }

    @Override
    public void connect() throws NetworkException {

    }

    @Override
    public int getResponseCode() {
        return 200;
    }

    @Override
    public String getRequestHeader(String name) {
        return null;
    }

    @Override
    public void disconnect() throws NetworkException {

    }

    @Override
    public Set<String> getRequestHeaderNames() {
        return Sets.newHashSet();
    }

    @Override
    public ImmutableMap<String, String> getRequestHeadersMap() {
        return httpRequestMeta.getRequestHeadersMap();
    }

    @Override
    public ImmutableMap<String, String> getCookies() {
        return cookies;
    }

    @Override
    public String getResponseHeader(String name) {
        return null;
    }

    @Override
    public Set<String> getResponseHeaderNames() {
        return getResponseHeadersMap().keySet();
    }

    @Override
    public ImmutableMap<String,ImmutableList<String>> getResponseHeadersMap() {

        if ( httpResponseMeta != null ) {
            return httpResponseMeta.getResponseHeadersMap();
        }

        return null;
    }

    @Override
    public String getResource() {
        return cachedHttpRequestMethod.getResource();
    }

    @Override
    public String getResourceFromRedirect() {

        if ( httpResponseMeta != null ) {
            return httpResponseMeta.getResourceFromRedirect();
        }

        return getResource();

    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public InetAddress getInetAddress() {
        return null;
    }

    @Override
    public int getContentLength() {
        return cachedHttpRequestMethod.content.getBytes(Charsets.UTF_8).length;
    }

    @Override
    public HttpRequestMethod getMethod() {
        return null;
    }

    @Override
    public NetworkException getCause() {
        return null;
    }

    @Override
    public void setCause(NetworkException ne) {

    }

    @Override
    public Class<?> getExecutor() {
        return executor;
    }

    @Override
    public HttpRequestMeta getHttpRequestMeta() {
        return httpRequestMeta;
    }

    @Override
    public HttpResponseMeta getHttpResponseMeta() throws NetworkException {
        return httpResponseMeta;
    }

    @Override
    public HttpContentResponseMeta getHttpContentResponseMeta() throws NetworkException {
        return new DefaultHttpContentResponseMeta( getResource(), httpResponseMeta, getContentWithEncoding() );
    }

}
