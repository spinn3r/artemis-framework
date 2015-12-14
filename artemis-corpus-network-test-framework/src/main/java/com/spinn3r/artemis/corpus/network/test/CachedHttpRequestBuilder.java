package com.spinn3r.artemis.corpus.network.test;

import com.google.inject.Inject;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.BaseHttpRequestBuilder;
import com.spinn3r.artemis.network.builder.HttpRequestBuilder;
import com.spinn3r.artemis.network.builder.HttpRequestMethod;
import com.spinn3r.artemis.network.builder.proxies.ProxyRegistry;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsRegistry;

import java.net.Proxy;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A HttpRequestBuilder that uses the NetworkCorporaCache.
 */
public class CachedHttpRequestBuilder extends BaseHttpRequestBuilder implements HttpRequestBuilder {

    protected final NetworkCorporaCache networkCorporaCache;

    @Inject
    CachedHttpRequestBuilder(NetworkCorporaCache networkCorporaCache) {
        this.networkCorporaCache = networkCorporaCache;
    }

    @Override
    public HttpRequestBuilder withRequireProxy(boolean requireProxy) {
        return this;
    }

    @Override
    public HttpRequestBuilder withProxyRegistry(ProxyRegistry proxyRegistry) {
        return this;
    }

    @Override
    public HttpRequestBuilder withRequestSettingsRegistry(RequestSettingsRegistry requestSettingsRegistry) {
        return this;
    }

    @Override
    public HttpRequestBuilder withProxy(Proxy proxy) {
        return this;
    }

    @Override
    public HttpRequestBuilder withProxy(String type, String host, int port) {
        return this;
    }

    @Override
    public HttpRequestBuilder withProxy(String proxy) {
        return this;
    }

    @Override
    public HttpRequestBuilder withUserAgent(String userAgent) {
        return this;
    }

    @Override
    public HttpRequestMethod get(String resource) throws NetworkException {
        checkNotNull( resource, "resource" );
        return new CachedHttpRequestMethod( this, HttpMethod.GET, resource );

    }

    @Override
    public HttpRequestMethod post(String resource, String outputContent, String outputContentEncoding, String outputContentType) throws NetworkException {
        checkNotNull( resource, "resource" );
        return new CachedHttpRequestMethod( this, HttpMethod.POST, resource, outputContent, outputContentEncoding, outputContentType );
    }

    @Override
    public HttpRequestMethod put(String resource, String outputContent, String outputContentEncoding, String outputContentType) throws NetworkException {
        checkNotNull( resource, "resource" );
        return new CachedHttpRequestMethod( this, HttpMethod.PUT, resource, outputContent, outputContentEncoding, outputContentType );
    }

    @Override
    public ProxyRegistry getProxyRegistry() {
        return null;
    }

    @Override
    public String getUserAgent() {
        return null;
    }
}
