package com.spinn3r.artemis.network;

import com.google.common.collect.Maps;
import com.spinn3r.artemis.network.builder.*;

import java.net.Proxy;
import java.util.Map;

/**
 *
 */
public class MockHttpRequestBuilder extends BaseHttpRequestBuilder implements HttpRequestBuilder, DirectHttpRequestBuilder {

    private Map<String,HttpRequestMethod> backing = Maps.newConcurrentMap();

    @Override
    public HttpRequestMethod get(String resource) throws NetworkException {
        return getHttpRequestMethod( HttpRequestMethods.GET, resource );
    }

    @Override
    public HttpRequestMethod get(String resource, String outputContent, String outputContentEncoding, String outputContentType) throws NetworkException {
        return getHttpRequestMethod( HttpRequestMethods.GET, resource , outputContent);
    }
    @Override
    public HttpRequestMethod post(String resource, String outputContent, String outputContentEncoding, String outputContentType) throws NetworkException {
        return getHttpRequestMethod( HttpRequestMethods.POST, resource , outputContent);
    }

    @Override
    public HttpRequestMethod post(String resource, Map<String, ?> parameters) throws NetworkException {
        throw new RuntimeException( "not implemented" );
    }

    @Override
    public HttpRequestMethod put(String resource, String outputContent, String outputContentEncoding, String outputContentType) throws NetworkException {
        return getHttpRequestMethod( HttpRequestMethods.PUT, resource , outputContent );
    }

    @Override
    public HttpRequestMethod options(String resource) throws NetworkException {
        return getHttpRequestMethod( HttpRequestMethods.OPTIONS, resource );
    }

    @Override
    public HttpRequestMethod head(String resource) throws NetworkException {
        return getHttpRequestMethod( HttpRequestMethods.HEAD, resource );
    }

    @Override
    public HttpRequestMethod delete(String resource) throws NetworkException {
        return getHttpRequestMethod( HttpRequestMethods.DELETE, resource );
    }

    @Override
    public HttpRequestMethod trace(String resource) throws NetworkException {
        return getHttpRequestMethod( HttpRequestMethods.TRACE, resource );
    }

    private HttpRequestMethod getHttpRequestMethod(String method, String resource) {

        HttpRequestMethod result = backing.get( key( method, resource ) );

        if ( result == null ) {
            throw new RuntimeException( "Mock method not registered for: " + resource + " with method " + method );
        }

        return result;
    }

    private HttpRequestMethod getHttpRequestMethod(String method, String resource, String requestBody) {

        HttpRequestMethod result = backing.get( key( method, resource, requestBody ) );

        if ( result == null ) {
            return getHttpRequestMethod( method, resource );
        }

        return result;
    }

    /**
     * Map a mock HTTP request to a given HTTP method and resource URL.
     */
    public void register(String method, MockHttpRequestMethod httpRequestMethod) {

        backing.put( key( method, httpRequestMethod.getResource() ), httpRequestMethod );

    }

    /**
     * Map a mock HTTP request to a given HTTP method, resource URL and request body.
     */
    public void register(String method, MockHttpRequestMethod httpRequestMethod, String requestBody) {

        backing.put( key( method, httpRequestMethod.getResource(), requestBody ), httpRequestMethod );

    }

    private String key( String method, String resource ) {
        return String.format( "%s:%s", method, resource );
    }

    private String key( String method, String resource , String requestBody) {
        return String.format( "%s:%s:%s", method, resource , requestBody);
    }

}
