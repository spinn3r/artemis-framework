package com.spinn3r.artemis.network;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.spinn3r.artemis.network.builder.*;
import com.spinn3r.artemis.network.cookies.Cookie;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class MockHttpRequest implements HttpRequest {

    private final MockHttpRequestMethod method;

    public MockHttpRequest(MockHttpRequestMethod method) {
        this.method = method;
    }

    @Override
    public String getRequestHeader(String name) {
        return null;
    }

    @Override
    public Set<String> getRequestHeaderNames() {
        return null;
    }

    @Override
    public ImmutableMap<String, String> getRequestHeadersMap() {
        return null;
    }

    @Override
    public ImmutableMap<String,ImmutableList<String>> getResponseHeadersMap() {
        return null;
    }

    @Override
    public ImmutableList<Cookie> getCookies() {
        return ImmutableList.of();
    }

    @Override
    public String getResponseHeader(String name) {
        return null;
    }

    @Override
    public Set<String> getResponseHeaderNames() {
        return Sets.newHashSet();
    }

    public String getResource() {
        return method.resource;
    }

    @Override
    public String getResourceFromRedirect() {
        return method.resource;
    }

    public int getResponseCode() {
        return method.responseCode;
    }

    public long getDuration() {
        return method.duration;
    }

    public InetAddress getInetAddress() {
        return method.inetAddress;
    }

    public int getContentLength() {
        return method.contentLength;
    }

    public String getContentWithEncoding() {
        return method.contentWithEncoding;
    }

    @Override
    public void disconnect() throws NetworkException {

    }

    @Override
    public InputStream getInputStream() {
        return method.inputStream;
    }

    @Override
    public InputStream getDirectInputStream() throws NetworkException {
        return method.inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        method.inputStream = inputStream;
    }

    @Override
    public HttpRequest connect() throws NetworkException {
        return this;
    }

    @Override
    public HttpRequestMethod getMethod() {
        return method;
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
        return method.getExecutor();
    }

    @Override
    public HttpRequestMeta getHttpRequestMeta() {
        return null;
    }

    @Override
    public HttpResponseMeta getHttpResponseMeta() throws NetworkException {
        return null;
    }

    @Override
    public HttpContentResponseMeta getHttpContentResponseMeta() throws NetworkException {
        return null;
    }

    @Override
    public ImmutableList<Cookie> getEffectiveCookies() {
        return ImmutableList.of();
    }

}


