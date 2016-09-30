package com.spinn3r.artemis.network.builder;

import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;
import com.spinn3r.artemis.network.builder.proxies.ProxyRegistry;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsRegistry;

import java.net.Proxy;
import java.util.Map;

/**
 *
 */
public interface HttpRequestBuilder {

    HttpRequestBuilder withRequireProxy( boolean requireProxy );

    HttpRequestBuilder withProxyRegistry( ProxyRegistry proxyRegistry );

    HttpRequestBuilder withRequestSettingsRegistry(RequestSettingsRegistry requestSettingsRegistry);

    HttpRequestBuilder withProxy(ProxyReference proxyReference);

    /**
     * Configure with a proxy.  Only the the following types are supported:
     *
     * HTTP
     *
     */
    HttpRequestBuilder withProxy( String type, String host, int port );

    /**
     * Set a proxy in the form of scheme://host:port
     */
    HttpRequestBuilder withProxy( String proxy );

    HttpRequestBuilder withUserAgent( String userAgent) ;

    HttpRequestMethod get( String resource ) throws NetworkException;

    HttpRequestMethod get( String resource, String outputContent, String outputContentEncoding, String outputContentType ) throws NetworkException;

    HttpRequestMethod post( String resource, String outputContent, String outputContentEncoding, String outputContentType ) throws NetworkException;

    HttpRequestMethod post( String resource, Map<String,?> parameters ) throws NetworkException;

    HttpRequestMethod put( String resource, String outputContent, String outputContentEncoding, String outputContentType ) throws NetworkException;

    HttpRequestMethod options( String resource ) throws NetworkException;

    HttpRequestMethod head( String resource ) throws NetworkException;

    HttpRequestMethod delete( String resource ) throws NetworkException;

    HttpRequestMethod trace( String resource ) throws NetworkException;

    ProxyRegistry getProxyRegistry();

    String getUserAgent();

}
