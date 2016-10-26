package com.spinn3r.artemis.network.builder;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.PostEncoder;
import com.spinn3r.artemis.network.URLResourceRequest;
import com.spinn3r.artemis.network.builder.proxies.PrioritizedProxyReference;
import com.spinn3r.artemis.network.builder.proxies.ProxyReferences;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;
import com.spinn3r.artemis.network.builder.proxies.ProxyRegistry;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsRegistry;
import com.spinn3r.log5j.Logger;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public abstract class BaseHttpRequestBuilder implements HttpRequestBuilder {

    private static final Logger log = Logger.getLogger();

    protected boolean requireProxy = false;

    protected ProxyRegistry proxyRegistry = null;

    protected RequestSettingsRegistry requestSettingsRegistry;

    protected String userAgent = URLResourceRequest.USER_AGENT;

    @Override
    public HttpRequestBuilder withRequireProxy( boolean requireProxy ) {
        this.requireProxy = requireProxy;
        return this;
    }

    @Override
    public HttpRequestBuilder withProxyRegistry(ProxyRegistry proxyRegistry) {
        this.proxyRegistry = proxyRegistry;
        return this;
    }

    @Override
    public HttpRequestBuilder withRequestSettingsRegistry(RequestSettingsRegistry requestSettingsRegistry) {
        this.requestSettingsRegistry = requestSettingsRegistry;
        return this;
    }

    @Override
    public HttpRequestBuilder withProxy(ProxyReference proxyReference) {

        PrioritizedProxyReference prioritizedProxyReference
          = new PrioritizedProxyReference( "default", 1, ".*", proxyReference.getHost(), proxyReference.getPort(), proxyReference.getProxy());

        List<PrioritizedProxyReference> prioritizedProxyReferences = Lists.newArrayList( prioritizedProxyReference );
        ProxyRegistry proxyRegistry = new ProxyRegistry( prioritizedProxyReferences );
        withProxyRegistry( proxyRegistry );

        return this;

    }

    @Override
    public HttpRequestBuilder withProxy(String type, String host, int port) {

        checkNotNull( type );

        ProxyReference proxyReference = ProxyReferences.create(type, host, port);

        log.info( "Now using proxy type %s on %s:%s", type, host, port );

        return withProxy( proxyReference );

    }

    @Override
    public HttpRequestBuilder withProxy(String proxy) {

        withProxy(ProxyReferences.create(proxy ) );
        return this;

    }

    @Override
    public HttpRequestBuilder withUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    @Override
    public ProxyRegistry getProxyRegistry() {
        return proxyRegistry;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public HttpRequestMethod post(String resource, Map<String, ?> parameters) throws NetworkException {
        String outputContent = PostEncoder.encode( parameters );
        return post( resource, outputContent, "UTF-8", "application/x-www-form-urlencoded" );
    }

}
