package com.spinn3r.artemis.network;

import com.spinn3r.log5j.Logger;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Get a ResourceRequest for a given URL.  The request is handled based on the
 * URL.
 *
 * @author <a href="mailto:burton@openprivacy.org">Kevin A. Burton</a>
 * @version $Id: ResourceRequestFactory.java 159217 2005-03-27 23:47:14Z burton $
 */
public class ResourceRequestFactory {

    private static Logger log = Logger.getLogger();

    /**
     * Specified in java.security to indicate the caching policy for successful
     * name lookups from the name service.. The value is specified as as integer
     * to indicate the number of seconds to cache the successful lookup.
     *
     *
     * sun.net.inetaddr.ttl:
     *
     * This is a sun private system property which corresponds to
     * networkaddress.cache.ttl. It takes the same value and has the same meaning,
     * but can be set as a command-line option. However, the preferred way is to
     * use the security property mentioned above.
     *
     * A value of -1 indicates "cache forever".
     */
    public static int NETWORKADDRESS_CACHE_TTL = 5 * 60;

    /**
     * These properties specify the default connect and read timeout (resp.) for
     * the protocol handler used by java.net.URLConnection.
     *
     * sun.net.client.defaultConnectTimeout specifies the timeout (in
     * milliseconds) to establish the connection to the host. For example for
     * http connections it is the timeout when establishing the connection to
     * the http server. For ftp connection it is the timeout when establishing
     * the connection to ftp servers.
     *
     * sun.net.client.defaultReadTimeout specifies the timeout (in milliseconds)
     * when reading from input stream when a connection is established to a
     * resource.
     */
    public static int DEFAULT_CONNECT_TIMEOUT = 30 * 1000;

    public static int DEFAULT_READ_TIMEOUT = DEFAULT_CONNECT_TIMEOUT;

    /**
     * The maximum read timeout.  This is done so that the real/effective time
     * of an HTTP connection is bounded.  Otherwise it would be possible to give
     * us 1 byte ever 29 seconds and avoid throwing an exception.
     *
     */
    public static int MAX_READ_TIMEOUT = 70 * 1000;

    /**
     * Specify the maximum number of redirects to use.
     */
    public static int DEFAULT_MAX_REDIRECTS = 10;

    /**
     * Whether we should use HTTP Keep Alive in java.net.URL.  We default to
     * false here because MOST of our TCP connections won't ever be re-used and
     * we're just wasting file handles on the robot and keeping a connection
     * open to the remote host which uses one of their threads.  I also think
     * this yields a bug in Tailrank's robot where numerous threads continually
     * access thousands of hosts and then we start to run out of available file
     * handles.
     */
    public static boolean DEFAULT_HTTP_KEEPALIVE = false;

    private static Map<String,Class> schemeMap = null;

    private static boolean isInitialized = false;

    public static Object INIT_MUTEX = new Object();

    /**
     * Get a ResourceRequest for the protocol represented in the resource URL.
     * It is important that we use a ResourceRequest implementation that supports
     * fetching the URL.
     *
     * @param init When init is specified we perform initialization when true.
     * If you specify false you can set additional connection settings but you
     * must perform your own manual init afterwards.
     */
    public static ResourceRequest getResourceRequest( String resource,
                                                      long ifModifiedSince, /* default: -1    */
                                                      String etag,          /* default: null  */
                                                      Proxy proxy,          /* default: null  */
                                                      boolean init          /* default: true  */ ) throws NetworkException {

        //make sure we are initialized correctly.
        ResourceRequestFactory.init();

        //make sure we have an index..

        int schemeIndex = resource.indexOf( ":" );

        if ( schemeIndex == -1 )
            throw new NetworkException( "Unknown scheme: '" + resource + "'" );

        String scheme = resource.substring( 0, schemeIndex );

        if ( scheme.equals( "" ) )
            throw new MalformedResourceException( "Not supported: " + resource );

        Class clazz = schemeMap.get( scheme );

        if ( clazz == null ) {
            throw new MalformedResourceException( String.format( "Scheme not supported: '%s'", scheme ) );
        }

        try {

            ResourceRequest request = (ResourceRequest) clazz.newInstance();

            request.setResource( resource );

            //setup resource request options.
            request.setIfModifiedSince( ifModifiedSince );

            //set the etag... when its null nothing will happen
            request.setEtag( etag );

            request.setProxy( proxy );

            if (init) {
                request.init();
            }

            return request;

        } catch ( NetworkException ne ) {
            throw ne;
        } catch ( Exception t ) {
            throw new NetworkException( t );
        }

    }

    /**
     * Make sure the factory is initialized.  Called once per JVM instance.
     *
     *
     */
    private static void init() {

        if ( isInitialized )
            return;

        synchronized( INIT_MUTEX ) {

            //using double check idiom.
            if ( isInitialized )
                return;

            //set the authenticator to use

            //FIXME: remove this until we figure out how to do proxy authentication.
            //java.net.Authenticator.setDefault ( new Authenticator() );

            // A full list of properties is available here:

            // http://java.sun.com/j2se/1.4.2/docs/guide/net/properties.html

            //NOTE: Thu Aug 10 2006 05:17 PM (burton@tailrank.com): its not a good
            //idea to set these values since they modify the defaults for ALL IO
            //applications. It would be BETTER to select more realistic values
            //instead of infinity though.

            defineStandardSystemProperties();

            if ( schemeMap == null ) {

                schemeMap = new HashMap<>();

                schemeMap.put( "file", URLResourceRequest.class );
                schemeMap.put( "http", URLResourceRequest.class );
                schemeMap.put( "https", URLResourceRequest.class );

            }

            isInitialized = true;

        }

    }

    public static void defineStandardSystemProperties() {

        System.setProperty( "http.keepAlive", Boolean.toString( DEFAULT_HTTP_KEEPALIVE ) );

        System.setProperty( "http.maxRedirects",
                            Integer.toString( DEFAULT_MAX_REDIRECTS ) );

        System.setProperty( "networkaddress.cache.ttl",
                            Integer.toString( NETWORKADDRESS_CACHE_TTL ) );

        System.setProperty( "sun.net.client.defaultConnectTimeout",
                            Integer.toString( DEFAULT_CONNECT_TIMEOUT ) );

        System.setProperty( "sun.net.client.defaultReadTimeout",
                            Integer.toString( DEFAULT_READ_TIMEOUT ) );

        System.setProperty( "sun.net.inetaddr.ttl",
                            Integer.toString( NETWORKADDRESS_CACHE_TTL ) );

    }

}
