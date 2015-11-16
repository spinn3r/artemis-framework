package com.spinn3r.artemis.network.builder;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.network.URLResourceRequest;
import com.spinn3r.artemis.network.builder.proxies.Proxies;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;
import com.spinn3r.artemis.network.builder.proxies.ProxyRegistry;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsRegistry;
import com.spinn3r.log5j.Logger;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public HttpRequestBuilder withProxy(Proxy proxy) {

        ProxyReference proxyReference = new ProxyReference( "default", 1, ".*", proxy );

        List<ProxyReference> proxyReferences = Lists.newArrayList( proxyReference );
        ProxyRegistry proxyRegistry = new ProxyRegistry( proxyReferences );
        withProxyRegistry( proxyRegistry );

        return this;

    }

    @Override
    public HttpRequestBuilder withProxy(String type, String host, int port) {

        checkNotNull( type );

        Proxy proxy = Proxies.create(type, host, port);

        log.info( "Now using proxy type %s on %s:%s", type, host, port );

        return withProxy( proxy );

    }

    @Override
    public HttpRequestBuilder withProxy(String proxy) {

        withProxy( Proxies.create( proxy ) );
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

}
