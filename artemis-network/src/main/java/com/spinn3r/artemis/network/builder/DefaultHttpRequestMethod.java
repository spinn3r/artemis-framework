package com.spinn3r.artemis.network.builder;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.ResourceRequest;
import com.spinn3r.artemis.network.ResourceRequestFactory;
import com.spinn3r.artemis.network.builder.cookies.ThreadLocalCookies;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsReference;
import com.spinn3r.artemis.network.builder.settings.requests.RequestSettingsRegistry;
import com.spinn3r.artemis.network.init.RequestSettings;
import com.spinn3r.artemis.network.validators.HttpResponseValidator;

import java.net.HttpCookie;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
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

    @Override
    public HttpRequest execute() throws NetworkException {

        Preconditions.checkNotNull( resource );

        // *** apply proxies...

        if ( defaultHttpRequestBuilder.proxyRegistry != null ) {

            ProxyReference proxyReference = defaultHttpRequestBuilder.proxyRegistry.find( resource );

            if ( proxyReference != null ) {
                proxy = proxyReference.getProxy();
            }

        }

        if ( defaultHttpRequestBuilder.requireProxy && proxy == null ) {
            throw new NetworkException( "Proxy required while fetching URL: " + resource );
        }

        // *** now set site specific options...

        RequestSettingsRegistry requestSettingsRegistry = defaultHttpRequestBuilder.requestSettingsRegistry;

        if ( requestSettingsRegistry != null ) {

            RequestSettingsReference requestSettingsReference = requestSettingsRegistry.find( resource );

            if ( requestSettingsReference != null ) {

                RequestSettings requestSettings = requestSettingsReference.getRequestSettings();

                if ( requestSettings.getFollowContentRedirects() != null ) {
                    withFollowContentRedirects( requestSettings.getFollowContentRedirects() );
                }

                withRequestHeaders(requestSettings.getRequestHeaders());
                withCookies(requestSettings.getCookies());

                // TODO: user agent, etc.

            }

        }

        ResourceRequest resourceRequest = ResourceRequestFactory.getResourceRequest( resource, modifiedSince, etag, proxy, true );

        registerCookies();

        resourceRequest.setCookies(cookies);
        resourceRequest.setRequestMethod( method );
        resourceRequest.setUserAgent( defaultHttpRequestBuilder.userAgent );
        resourceRequest.setMaxContentLength( maxContentLength );
        resourceRequest.setReadTimeout( readTimeout );
        resourceRequest.setConnectTimeout( connectTimeout );
        resourceRequest.setFollowRedirects( followRedirects );
        resourceRequest.setFollowContentRedirects( followContentRedirects );

        // set HTTP request headers.
        for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
            resourceRequest.setRequestHeader( header.getKey(), header.getValue() );
        }

        if ( requiresOutputContent() ) {

            resourceRequest.setOutputContent( outputContent, outputContentEncoding, outputContentType );
        }

        DefaultHttpRequest httpRequest = new DefaultHttpRequest( defaultHttpRequestBuilder, this, resourceRequest );

        validate( httpRequest );

        return httpRequest;

    }

    // Define cookies in used for the current request/thread.
    // Do not call setCookies
    private void registerCookies() throws NetworkException {

        try {

            ThreadLocalCookies threadLocalCookies = defaultHttpRequestBuilder.threadLocalCookies;

            URI uri = new URI(resource);

            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                HttpCookie httpCookie = new HttpCookie(entry.getKey(), entry.getValue());
                httpCookie.setPath("/");

                threadLocalCookies.add(uri, httpCookie);
            }

        } catch (URISyntaxException e) {
            throw new NetworkException("Unable to register cookies: ", e);
        }

    }

    private boolean requiresOutputContent() {

        if ( Strings.isNullOrEmpty(outputContent)) {
            return false;
        }

        if ( ! (method.equals(HttpRequestMethods.GET) || method.equals(HttpRequestMethods.POST) || method.equals(HttpRequestMethods.PUT) ) ) {
            return false;
        }

        return true;

    }

    private void validate( HttpRequest httpRequest ) throws NetworkException {

        ImmutableList<HttpResponseValidator> responseValidators = defaultHttpRequestBuilder.httpResponseValidators.getResponseValidators();

        for (HttpResponseValidator responseValidator : responseValidators) {
            responseValidator.validate( httpRequest );
        }

    }

    @Override
    public String getResource() {
        return resource;
    }

    @Override
    public HttpRequestMethod withProxy(ProxyReference proxyReference) {
        this.proxy = proxyReference.getProxy();
        return this;
    }

    @Override
    @VisibleForTesting
    public Proxy getProxy() {
        return proxy;
    }

}
