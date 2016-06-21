package com.spinn3r.artemis.network;

import com.google.common.base.Charsets;
import com.spinn3r.artemis.network.builder.*;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class MockHttpRequestMethod extends BaseHttpRequestMethod implements HttpRequestMethod {

    Map<String,String> properties = new LinkedHashMap<>();

    protected String resource;

    protected int responseCode;

    protected long duration;

    protected InetAddress inetAddress;

    protected int contentLength;

    protected String contentWithEncoding;

    protected InputStream inputStream;

    public MockHttpRequestMethod(String resource) {
        this.resource = resource;
    }

    @Override
    public HttpRequest execute() throws NetworkException {
        return new MockHttpRequest( this );
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String getResource() {
        return resource;
    }

    public MockHttpRequestMethod setResource(String resource) {
        this.resource = resource;
        return this;
    }

    public MockHttpRequestMethod setResponseCode(int responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public MockHttpRequestMethod setContentLength(int contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public MockHttpRequestMethod setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public MockHttpRequestMethod setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
        return this;
    }

    public MockHttpRequestMethod setContentWithEncoding(String contentWithEncoding) {
        this.contentWithEncoding = contentWithEncoding;
        this.contentLength = contentWithEncoding.getBytes( Charsets.UTF_8 ).length;
        return this;
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

