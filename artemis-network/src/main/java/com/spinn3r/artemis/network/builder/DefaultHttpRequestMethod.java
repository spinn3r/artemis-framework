package com.spinn3r.artemis.network.builder;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.ResourceRequest;
import com.spinn3r.artemis.network.ResourceRequestFactory;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;

import java.net.Proxy;
import java.util.Map;

/**
 *
 */
public class DefaultHttpRequestMethod extends BaseHttpRequestMethod implements HttpRequestMethod {

    protected final DefaultHttpRequestBuilder defaultHttpRequestBuilder;

    protected final String resource;

    protected String method = HttpRequestMethods.GET;

    protected String outputContent = null;

    protected String outputContentEncoding = null;

    protected String outputContentType = null;

    protected Proxy proxy = null;

    DefaultHttpRequestMethod(DefaultHttpRequestBuilder defaultHttpRequestBuilder, String resource, String method) {
        this.defaultHttpRequestBuilder = defaultHttpRequestBuilder;
        this.resource = resource;
        this.method = method;

        // TODO: apply default values like proxies...

    }

    DefaultHttpRequestMethod(DefaultHttpRequestBuilder defaultHttpRequestBuilder, String resource, String method, String outputContent, String outputContentEncoding, String outputContentType) {
        this.defaultHttpRequestBuilder = defaultHttpRequestBuilder;
        this.resource = resource;
        this.method = method;
        this.outputContent = outputContent;
        this.outputContentEncoding = outputContentEncoding;
        this.outputContentType = outputContentType;

        // TODO: apply default values like proxies...

    }

    // set the
    private void setup() {

    }

    @Override
    public HttpRequest execute() throws NetworkException {

        Preconditions.checkNotNull( resource );

        if ( defaultHttpRequestBuilder.proxyRegistry != null ) {

            ProxyReference proxyReference = defaultHttpRequestBuilder.proxyRegistry.find( resource );

            if ( proxyReference != null ) {
                proxy = proxyReference.getProxy();
            }

        }

        if ( defaultHttpRequestBuilder.requireProxy && proxy == null ) {
            throw new NetworkException( "Proxy required while fetching URL: " + resource );
        }

        ResourceRequest resourceRequest = ResourceRequestFactory.getResourceRequest( resource, modifiedSince, etag, proxy, true );

        resourceRequest.setRequestMethod( method );
        resourceRequest.setUserAgent( defaultHttpRequestBuilder.userAgent );
        resourceRequest.setMaxContentLength( maxContentLength );
        resourceRequest.setCookies( cookies );
        resourceRequest.setReadTimeout( readTimeout );
        resourceRequest.setConnectTimeout( connectTimeout );
        resourceRequest.setFollowRedirects( followRedirects );
        resourceRequest.setFollowContentRedirects( followContentRedirects );

        // set HTTP request headers.
        for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
            resourceRequest.setRequestHeader( header.getKey(), header.getValue() );
        }

        if ( method.equals( HttpRequestMethods.POST ) || method.equals( HttpRequestMethods.PUT ) ) {
            resourceRequest.setOutputContent( outputContent, outputContentEncoding, outputContentType );
        }

        return new DefaultHttpRequest( defaultHttpRequestBuilder, this, resourceRequest );

    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public HttpRequestMeta getHttpRequestMeta() {
        return new HttpRequestMeta( getResource() );
    }

    @Override
    public HttpRequestMethod withProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    @Override
    @VisibleForTesting
    public Proxy getProxy() {
        return proxy;
    }


}
