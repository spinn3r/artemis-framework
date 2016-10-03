package com.spinn3r.artemis.network;

import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.log5j.Logger;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

/**
 *
 */
public class NetworkException extends IOException {

    private static Logger log = Logger.getLogger();

    private ResourceRequest request = null;

    public Exception e = null;

    private URLConnection _urlConnection = null;

    private int responseCode = Integer.MIN_VALUE;

    // The status string in the HTTP response. Example: HTTP/1.1 200 OK
    private String status = null;

    private NetworkFailure networkFailure = null;

    public NetworkException( String message ) {
        super( message );
    }

    public NetworkException( String message, int responseCode ) {
        super( message );
        this.responseCode = responseCode;
    }

    public NetworkException( String message, Throwable t ) {
        super( message + ": " + t.getClass().getName() + ": " + t.getMessage() );
        initCause( t );
    }

    public NetworkException( Throwable t ) {
        super( t.getClass().getName() + ": " + t.getMessage() );
        initCause( t );
    }

    /**
     */
    public NetworkException( String message,
                             Exception e,
                             ResourceRequest request,
                             URLConnection _urlConnection ) {

        super( request.getResource() + ": " + getMessageFromCause( e, message ) );

        this.e = e;
        this.request = request;
        this._urlConnection = _urlConnection;

        boolean timeout = e instanceof SocketTimeoutException;

        // do not attempt to read the status if we timed out...

        if ( _urlConnection != null && ! timeout ) {
            this.status = _urlConnection.getHeaderField( null );
        }

        initCause( e );

    }

    public NetworkException(String message,
                            ResourceRequest request,
                            URLConnection _urlConnection) {

        //why doesn't java.io.IOException support nesting?
        super( request.getResource() + ": " + message );
        this.request = request;
        this._urlConnection = _urlConnection;

        if ( _urlConnection != null ) {
            this.status = _urlConnection.getHeaderField( null );
        }

    }

    /**
     *
     * Create a new <code>NetworkException</code> instance.
     *
     *
     */
    public NetworkException( Exception e,
                             ResourceRequest request,
                             URLConnection _urlConnection ) {

        this( e.getMessage(), e, request, _urlConnection );

    }

    public NetworkException(Exception cause, NetworkFailure networkFailure) {
        super( networkFailure.getResource() + ": " + getMessageFromCause( cause, cause.getMessage() ) );
        this.networkFailure = networkFailure;
    }

    /**
     * If the cause is specified, and we can provide more metadata, go for it...
     *
     */
    private static String getMessageFromCause( Exception cause, String defaultMessage ) {

        if ( cause != null ) {

            if (cause instanceof UnknownHostException) {
                return String.format( "Unknown host: %s", cause.getMessage() );
            }

        }

        return defaultMessage;

    }

    /**
     * Get the HTTP response code form this error.
     */
    public int getResponseCode() {

        if (networkFailure != null) {
            return networkFailure.getResponseCode();
        }

        return -1;

    }

    /**
     * Return true if this NetworkException is transient and may be resolve
     * in the future.  This includes connect and read timeouts, HTTP 5xx, etc.
     */
    public boolean isTransient() {

        int responseCode = getResponseCode();

        return responseCode == HttpRequest.STATUS_CONNECT_TIMEOUT ||
               responseCode == HttpRequest.STATUS_READ_TIMEOUT ||
               (getResponseCode() >= 500 && getResponseCode() <= 599);

    }

    /**
     * Return true if this is an exception from a proxy server, vs the origin server.
     * This isn't always authoritative, but if we return true we're CERTAIN that
     * it's a proxy error.  Returning false might STILL be a proxy error though.
     */
    public boolean isProxyError() {

        if (networkFailure != null){
            return networkFailure.isProxyError();
        }

        return false;

    }

    public static class RequestBlockedDueToCaptcha extends NetworkException {

        public RequestBlockedDueToCaptcha(String link) {
            super( "Request blocked due to captcha: " + link, 599 );
        }

    }

}
