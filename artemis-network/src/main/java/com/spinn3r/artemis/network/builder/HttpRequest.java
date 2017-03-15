package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.cookies.Cookie;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.Optional;
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

    /**
     * SSL failed when connecting.
     */
    int SSL_FAILURE = -1026;

    // TODO: refactor the name of this method as it is confusing when compared
    // to the Content-Encoding HTTP header.  Technically this is something
    // like
    //   getContentWithCharsetEncodingApplied or
    //   getContentWithCharsetEncoding
    //
    // There are two HTTP headers involved here.  The first is:
    //
    // Content-Type:  includes the media type (AKA text/html or application/rss+xml)
    //                and an optional charset via ;charset=
    //
    //                Example: text/html; charset=utf-8
    //
    // Content-Encoding: specifies the encoding used for on the raw bytes to
    //                   compress the data.  Usually just gzip.
    //
    // Once we fix this then we could add a getContentEncoding method which
    // would return 'gzip' or 'null' and then we could have stats on this.

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
    HttpRequest connect() throws NetworkException;

    int getResponseCode();

    /**
     * @Deprecated use {@link HttpRequest#getHttpRequestMeta()}
     */
    @Deprecated
    String getRequestHeader(String name);

    /**
     * @Deprecated use {@link HttpRequest#getHttpRequestMeta()}
     */
    @Deprecated
    Set<String> getRequestHeaderNames();

    /**
     * @Deprecated use {@link HttpRequest#getHttpRequestMeta()}
     */
    @Deprecated
    ImmutableMap<String,String> getRequestHeadersMap();

    ImmutableList<Cookie> getCookies();

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

    /**
     * Get the actual cookies used with this request including the cookies
     * defined but he server as well as custom cookies set with withCookies()
     */
    ImmutableList<Cookie> getEffectiveCookies();


}
