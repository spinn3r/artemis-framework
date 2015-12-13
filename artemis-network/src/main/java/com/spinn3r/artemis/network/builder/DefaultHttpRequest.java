package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.network.Cookies;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.ResourceRequest;
import com.spinn3r.artemis.network.URLResourceRequest;
import com.spinn3r.artemis.network.builder.listener.RequestListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class DefaultHttpRequest implements HttpRequest {

    private final DefaultHttpRequestBuilder defaultHttpRequestBuilder;

    private final DefaultHttpRequestMethod method;

    private final ResourceRequest resourceRequest;

    private String contentWithEncoding = null;

    public DefaultHttpRequest( DefaultHttpRequestBuilder defaultHttpRequestBuilder,
                               DefaultHttpRequestMethod method,
                               ResourceRequest resourceRequest) {

        this.defaultHttpRequestBuilder = defaultHttpRequestBuilder;
        this.method = method;
        this.resourceRequest = resourceRequest;

    }

    @Override
    public String getContentWithEncoding() throws NetworkException {

        if ( contentWithEncoding == null ) {

            contentWithEncoding = resourceRequest.getContentWithEncoding();

            if (defaultHttpRequestBuilder.requestListeners != null) {

                for (RequestListener requestListener : defaultHttpRequestBuilder.requestListeners) {
                    requestListener.onContentWithEncoding( method.getHttpRequestMeta(), contentWithEncoding );
                }

            }

        }

        return contentWithEncoding;

    }

    @Override
    public void disconnect() throws NetworkException {
        resourceRequest.disconnect();
    }

    @Override
    public InputStream getInputStream() throws NetworkException {

        try {

            return resourceRequest.getInputStream();

        } catch (IOException e) {
            throw new NetworkException( e );
        }

    }

    @Override
    public InputStream getDirectInputStream() throws NetworkException {
        return ((URLResourceRequest)resourceRequest).getBackendInputStream();
    }

    @Override
    public void connect() throws NetworkException {
        resourceRequest.connect();
    }

    @Override
    public int getResponseCode() {
        return resourceRequest.getResponseCode();
    }


    @Override
    public String getRequestHeader(String name) {
        return resourceRequest.getRequestHeader( name );
    }

    @Override
    public Set<String> getRequestHeaderNames() {
        return resourceRequest.getRequestHeaders();
    }

    @Override
    public Map<String,String> getRequestHeadersMap() {
        return resourceRequest.getRequestHeadersMap();
    }

    @Override
    public String getResponseHeader(String name) {
        return resourceRequest.getResponseHeader( name );
    }

    @Override
    public Set<String> getResponseHeaderNames() {
        return resourceRequest.getResponseHeaders();
    }

    @Override
    public ImmutableMap<String,ImmutableList<String>> getResponseHeadersMap() {

        Map<String, List<String>> responseHeadersMap = resourceRequest.getResponseHeadersMap();

        Map<String,ImmutableList<String>> tmp = Maps.newHashMap();
        for (Map.Entry<String, List<String>> entry : responseHeadersMap.entrySet()) {

            if ( entry.getKey() == null ) {
                // the HTTP status line is included here as a null key which is
                // really ugly and we should hide it.  It also yields bugs
                // for example Jackson refuses to encode it.
                continue;
            }

            tmp.put( entry.getKey(), ImmutableList.copyOf( entry.getValue() ) );
        }

        return ImmutableMap.copyOf( tmp );

    }

    @Override
    public String getResource() {
        return resourceRequest.getResource();
    }

    @Override
    public String getResourceFromRedirect() {
        return resourceRequest.getResourceFromRedirect();
    }

    @Override
    public long getDuration() {
        return resourceRequest.getDuration();
    }

    @Override
    public InetAddress getInetAddress() {
        return resourceRequest.getInetAddress();
    }

    @Override
    public int getContentLength() {
        return resourceRequest.getContentLength();
    }

    @Override
    public HttpRequestMethod getMethod() {
        return method;
    }

    @Override
    public NetworkException getCause() {
        return resourceRequest.getCause();
    }

    @Override
    public void setCause( NetworkException cause ) {
        resourceRequest.setCause( cause );
    }

    @Override
    public Class<?> getExecutor() {
        return method.executor;
    }

    @Override
    public HttpResponseMeta getHttpResponseMeta() throws NetworkException {

        connect();

        return new DefaultHttpResponseMeta( getResource(), getResourceFromRedirect(), getResponseCode(), getResponseHeadersMap(), Cookies.fromHttpRequest(this) );

    }

    @Override
    public HttpContentResponseMeta getHttpContentResponseMeta() throws NetworkException {

        String contentWithEncoding = getContentWithEncoding();

        return new DefaultHttpContentResponseMeta( getResource(), getResourceFromRedirect(), getResponseCode(), getResponseHeadersMap(), Cookies.fromHttpRequest(this), contentWithEncoding );

    }

}
