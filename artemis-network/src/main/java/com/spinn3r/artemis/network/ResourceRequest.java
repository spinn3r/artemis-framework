package com.spinn3r.artemis.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A ResourceRequest is a generic interface to a network resource such as an
 * HTTP URL.
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id: ResourceRequest.java 159213 2005-03-27 23:32:01Z burton $
 */
public interface ResourceRequest {

    /**
     * Perform all initialization and connection to the remote server.  This
     * should always be called BEFORE network getInputStream() if you want to
     * perform other operations first.  When using a HEAD request this must be
     * used and not getInputStream()
     *
     *
     */
    void init() throws NetworkException;

    /**
     *  Opens a communications link to the resource referenced by this URL, if
     *  such a connection has not already been established.
     *
     */
    void connect() throws NetworkException;

    /**
     * Primary method for dealing with content.  Handles all content encodings
     * correctly.
     *
     */
    String getContentWithEncoding() throws NetworkException;

    /**
     * Get an input stream for this content.
     *
     */
    InputStream getInputStream() throws IOException;

    /**
     * One should generally use getContentWithEncoding for HTML and RSS but a
     * byte array is more appropriate for GIF/JPEG data.
     *
     */
    byte[] getInputStreamAsByteArray() throws IOException;

    /**
     * Set the resource for this request.
     *
     *
     */
    String getResource();
    void setResource( String resource );

    /**
     * Close any potentially open resources.
     *
     * @throws NetworkException
     */
    void disconnect() throws NetworkException;

    /**
     * Get the resource but make sure all redirects are taken into
     * consideration.
     *
     *
     */
    String getResourceFromRedirect();

    /**
     * Return the character set detected by getContentWithEncoding
     *
     */
    String getDetectedCharset();

    /**
     * Copy this input stream to an OutputStream
     *
     *
     */
    void toOutputStream( OutputStream out ) throws IOException;

    /**
     * Set the If-Modified-Since header for HTTP URL connections and protocols
     * that support similar operation.
     *
     * A value of -1 means do not use the If-Modified-Since header
     *
     * Fri Jun 06 2003 08:34 PM (burton@peerfear.org): Currently just URLResourceRequest
     *
     *
     */
    long getIfModifiedSince();
    void setIfModifiedSince( long ifModifiedSince );

    /**
     * The HTTP ETag to use with If-None-Match
     *
     *
     */
    String getEtag();
    void setEtag( String etag );

    /**
     * Get and set an HTTP style response code.  Only used with HTTP URLs.
     *
     *
     */
    int getResponseCode();
    void setResponseCode( int responseCode );

    /**
     * Return the content length of this request or -1 if no content has been
     * requested.
     */
    int getContentLength();

    /**
     * Get a given response header.
     *
     */
    String getResponseHeader(String name);

    Set<String> getResponseHeaders();

    Map<String,Collection<String>> getResponseHeadersMap();

    /**
     * Set a given request header such as UserAgent, ETag, etc.
     *
     */
    void setRequestHeader(String name, String value);

    String getRequestHeader(String name);

    /**
     * Get the names of all set request headers.
     *
     */
    Set<String> getRequestHeaders();

    Map<String,String> getRequestHeadersMap();

    void setRequestMethod( String method ) throws NetworkException;

    boolean getFollowRedirects();
    void setFollowRedirects( boolean v );

    /**
     * Set the user agent for this request.  We will use the default USER_AGENT
     * header otherwise.
     */
    void setUserAgent( String userAgent );
    String getUserAgent();

    /**
     * Return true if our status is NOT_NODIFIED
     */
    boolean isNotModified() throws NetworkException;

    boolean isModified() throws NetworkException;

    void setAuthPassword( String v ) ;
    void setAuthUsername( String v ) ;

    void setOutputContent( String content,
                                  String encoding,
                                  String type );

    void setOutputContent( byte[] content,
                                  String encoding,
                                  String type );

    boolean isRedirected();

    /**
     *
     * Set the value of <code>maxContentLength</code>.
     *
     */
    void setMaxContentLength( int maxContentLength );

    /**
     *
     * Get the value of <code>maxContentLength</code>.
     *
     */
    int getMaxContentLength() ;

    /**
     * When an HTTP error is handled, return the content associated with it.
     *
     */
    byte[] getError() throws NetworkException;

    /**
     * When an HTTP error is handled, return the content associated with it.
     *
     */
    String getErrorWithEncoding() throws NetworkException;

    boolean getUseExclusion();
    void setUseExclusion( boolean v );

    boolean getUseCompression();

    void setUseCompression( boolean v );

    void setProxy( Proxy proxy );

    void setReadTimeout( long v );

    long getReadTimeout();

    void setConnectTimeout( long v );

    long getConnectTimeout();

    int getDuration();

    InetAddress getInetAddress();

    /**
     * If a request failed, get the cause of the failure.
     *
     * @return
     */
    NetworkException getCause();

    void setCause(NetworkException cause);

    /**
     * The custom cookies being used with this request. This is only used
     * for logging purposes and you should be using a CookieStore for the JVM
     * but if you're using artemis-network you almost certainly are doing this
     * correctly.
     */
    Map<String, String> getCookies();

    void setCookies(Map<String, String> cookies);

    boolean getFollowContentRedirects();

    void setFollowContentRedirects(boolean followContentRedirects);

}
