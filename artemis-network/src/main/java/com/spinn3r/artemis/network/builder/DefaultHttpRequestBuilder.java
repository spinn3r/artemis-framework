package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.ResourceRequestFactory;
import com.spinn3r.artemis.network.URLResourceRequest;
import com.spinn3r.artemis.network.builder.listener.RequestListeners;
import com.spinn3r.artemis.network.events.NetworkEventListener;
import com.spinn3r.artemis.network.validators.HttpResponseValidators;

/**
 * Builder interface preferred over using the ResourceRequestFactory.
 */
public class DefaultHttpRequestBuilder extends BaseHttpRequestBuilder implements HttpRequestBuilder, DirectHttpRequestBuilder {

    private static final String GET_METHOD = "GET";
    private static final String POST_METHOD = "POST";
    private static final String PUT_METHOD = "PUT";
    private static final String OPTIONS_METHOD = "OPTIONS";

    protected NetworkEventListener listener = null;

    protected RequestListeners requestListeners;

    protected final HttpResponseValidators httpResponseValidators;

    private int defaultMaxContentLength = URLResourceRequest.MAX_CONTENT_LENGTH;

    private long defaultReadTimeout = ResourceRequestFactory.DEFAULT_READ_TIMEOUT;

    private long defaultConnectTimeout = ResourceRequestFactory.DEFAULT_CONNECT_TIMEOUT;

    @Inject
    DefaultHttpRequestBuilder(HttpResponseValidators httpResponseValidators) {
        this.httpResponseValidators = httpResponseValidators;
    }

    public HttpRequestBuilder withRequestListeners( RequestListeners requestListeners ) {
        this.requestListeners = requestListeners;
        return this;
    }

    public HttpRequestBuilder withDefaultMaxContentLength( int defaultMaxContentLength ) {
        this.defaultMaxContentLength = defaultMaxContentLength;
        return this;
    }

    public HttpRequestBuilder withDefaultReadTimeout( int defaultReadTimeout ) {
        this.defaultReadTimeout = defaultReadTimeout;
        return this;
    }

    public HttpRequestBuilder withDefaultConnectTimeout( int defaultConnectTimeout ) {
        this.defaultConnectTimeout = defaultConnectTimeout;
        return this;
    }

    @Override
    public HttpRequestMethod get(String resource) throws NetworkException {
        return configure( new DefaultHttpRequestMethod( this, resource, GET_METHOD ) );
    }

    @Override
    public HttpRequestMethod post(String resource, String outputContent, String outputContentEncoding, String outputContentType ) throws NetworkException {
        return configure( new DefaultHttpRequestMethod( this, resource, POST_METHOD, outputContent, outputContentEncoding, outputContentType ) );
    }

    @Override
    public HttpRequestMethod put(String resource, String outputContent, String outputContentEncoding, String outputContentType ) throws NetworkException {
        return configure( new DefaultHttpRequestMethod( this, resource, PUT_METHOD, outputContent, outputContentEncoding, outputContentType ) );
    }

    @Override
    public HttpRequestMethod options(String resource) throws NetworkException {
        return configure( new DefaultHttpRequestMethod( this, resource, OPTIONS_METHOD ) );
    }

    private DefaultHttpRequestMethod configure( DefaultHttpRequestMethod defaultHttpRequestMethod ) {
        defaultHttpRequestMethod.withMaxContentLength( defaultMaxContentLength );
        defaultHttpRequestMethod.withReadTimeout( defaultReadTimeout );
        defaultHttpRequestMethod.withConnectTimeout( defaultConnectTimeout );
        return defaultHttpRequestMethod;
    }

}
