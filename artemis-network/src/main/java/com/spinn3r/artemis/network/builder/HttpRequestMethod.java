package com.spinn3r.artemis.network.builder;

import com.google.common.annotations.VisibleForTesting;
import com.spinn3r.artemis.network.Cookie;
import com.spinn3r.artemis.network.NetworkException;

import java.net.Proxy;
import java.util.Map;

/**
 *
 */
public interface HttpRequestMethod {

    /**
     * Set an etag for conditional HTTP requests.  Ignored if null.
     */
    HttpRequestMethod withEtag(String etag);

    /**
     * Set the conditional get for 'if modified since'.  Ignored if -1.
     */
    HttpRequestMethod ifModifiedSince( long modifiedSince );

    /**
     * Set a property for this HTTP request.  This is NOT an HTTP request or
     * response parameter.  Properties are only visible to the method for use
     * with logging, instrumentation, authentication, etc.
     */
    HttpRequestMethod withProperty( String name, String value );

    /**
     * Set an HTTP request header by key and value.
     */
    HttpRequestMethod withRequestHeader( String name, String value );

    HttpRequestMethod withRequestHeaders( Map<String,String> requestHeaders );

    HttpRequestMethod withMaxContentLength( int maxContentLength );

    HttpRequestMethod withCookie( String name, String value );

    /**
     * Add the given cookies to the cookies we should use with the site.  By
     * default we start with an empty map of cookies.  This adds to the map.
     */
    HttpRequestMethod withCookies( Map<String,String> cookies );

    /**
     * Add the given cookies to the cookies we should use with the site.  By
     * default we start with an empty map of cookies.  This adds to the map.
     */
    HttpRequestMethod withCookieIndex( Map<String,Cookie> cookies );

    HttpRequestMethod withConnectTimeout( long timeout );

    HttpRequestMethod withReadTimeout( long timeout );

    HttpRequestMethod withFollowRedirects(boolean followRedirects);

    HttpRequestMethod withFollowContentRedirects( boolean followContentRedirects );

    HttpRequestMethod withProxy(Proxy proxy);

    /**
     * The class executing this HTTP request.
     */
    HttpRequestMethod withExecutor(Class<?> executor);

    HttpRequest execute() throws NetworkException;

    Map<String,String> getProperties();

    String getResource();

    boolean getFollowContentRedirects();

    Class<?> getExecutor();

    @VisibleForTesting
    Proxy getProxy();

}
