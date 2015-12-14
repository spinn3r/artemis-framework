package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.network.NetworkException;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Fluent interface for a HttpRequest.
 */
public interface HttpRequest {


    /**
     * Unknown status code.
     */
    int STATUS_UNKNOWN = -1;

    /**
     * Instead of using -1 as a status code for connect timeouts we use negative
     * values.  This represents a read timeout.
     */
    int STATUS_READ_TIMEOUT = -1024;

    /**
     * Instead of using -1 as a status code for connect timeouts we use negative
     * values.  This represents a connect timeout.
     */
    int STATUS_CONNECT_TIMEOUT = -1025;


    String getContentWithEncoding() throws NetworkException;

    void disconnect() throws NetworkException;

    InputStream getInputStream() throws NetworkException;

    /**
     * Get an input stream but without any local caching.  Our implementation
     * reads all the byte[] into memory first but this can be bad for large
     * documents and can blow up memory consumption
     *
     * @throws NetworkException
     */
    InputStream getDirectInputStream() throws NetworkException;

    /**
     * Make sure this HTTP request was executed which enables us to read the
     * status code and other response headers before getInputStream or
     * getContentWithEncoding.
     *
     * @throws NetworkException
     */
    void connect() throws NetworkException;

    int getResponseCode();

    String getRequestHeader(String name);

    Set<String> getRequestHeaderNames();

    ImmutableMap<String,String> getRequestHeadersMap();

    String getResponseHeader(String name);

    Set<String> getResponseHeaderNames();

    ImmutableMap<String,ImmutableList<String>> getResponseHeadersMap();

    String getResource();

    String getResourceFromRedirect();

    long getDuration();

    InetAddress getInetAddress();

    int getContentLength();

    /**
     * Get the method that created this request.
     *
     * @return
     */
    HttpRequestMethod getMethod();

    NetworkException getCause();

    void setCause( NetworkException ne );

    Class<?> getExecutor();

    HttpRequestMeta getHttpRequestMeta();

    HttpResponseMeta getHttpResponseMeta() throws NetworkException;

    HttpContentResponseMeta getHttpContentResponseMeta() throws NetworkException;

}
