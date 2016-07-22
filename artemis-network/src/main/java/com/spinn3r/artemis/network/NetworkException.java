package com.spinn3r.artemis.network;

import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.log5j.Logger;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 *
 */
public class NetworkException extends IOException {

    private static Logger log = Logger.getLogger();

    private ResourceRequest request = null;

    public Exception e = null;

    private URL _url = null;

    private URLConnection _urlConnection = null;

    private int responseCode = Integer.MIN_VALUE;

    /**
     * The status string in the HTTP response.
     *
     * Example: HTTP/1.1 200 OK
     */
    public String status = null;

    public NetworkException( String message ) {
        super( message );
    }

    public NetworkException( String message, int responseCode ) {
        super( message );
        this.responseCode = responseCode;
    }

    public NetworkException( String message, Throwable t ) {
        super( message + ": " + t.getClass().getName() + ": " + t.getMessage() );
        init(t);
    }

    public NetworkException( Throwable t ) {
        super( t.getClass().getName() + ": " + t.getMessage() );
        init(t);
    }

    /**
     *
     * Create a new <code>NetworkException</code> instance.
     *
     *
     */
    public NetworkException( Exception e,
                             ResourceRequest request,
                             URL _url,
                             URLConnection _urlConnection ) {

        this( e.getMessage(), e, request, _url, _urlConnection );

    }

    /**
     */
    public NetworkException( String message,
                             Exception e,
                             ResourceRequest request,
                             URL _url,
                             URLConnection _urlConnection ) {

        super( request.getResource() + ": " + getMessageFromCause( e, message ) );

        this.e = e;
        this.request = request;
        this._url = _url;
        this._urlConnection = _urlConnection;

        // FIXME: this needs to look at the computed codes...
        boolean timeout = e instanceof SocketTimeoutException;

        // do not attempt to read the status if we timed out...

        if ( _urlConnection != null && ! timeout ) {
            this.status = _urlConnection.getHeaderField( null );
        }

        init(e);

    }

    public NetworkException( String message,
                             ResourceRequest request,
                             URL _url,
                             URLConnection _urlConnection ) {

        super( request.getResource() + ": " + message );
        this.request = request;
        this._url = _url;
        this._urlConnection = _urlConnection;

        if ( _urlConnection != null ) {
            this.status = _urlConnection.getHeaderField( null );
        }

    }

    private void init(Throwable t) {

        Optional<Integer> calculatedResponseCode = ResponseCodes.calculateFromException(t);

        if ( calculatedResponseCode.isPresent()) {
            this.responseCode = calculatedResponseCode.get();
        } else {

        }

        super.initCause(t);

    }

    /**
     * If the cause is specified, and we can provide more metadata, go for it...
     *
     */
    private static String getMessageFromCause( Exception cause, String message ) {

        if ( cause != null ) {

            if ( cause instanceof UnknownHostException) {
                return String.format( "Unknown host: %s", cause.getMessage() );
            }

        }

        return message;

    }

    public URL getURL() {
        return _url;
    }

    /**
     * Get the HTTP response code form this error.
     */
    public int getResponseCode() {

        if ( responseCode == Integer.MIN_VALUE ) {

            if ( status == null ) {

                // FIXME: this will keep re-defining it since we're always MIN_VALUE

                // some other type of error happened, set it to an unknown
                // status code.

                responseCode = Integer.MIN_VALUE;

            } else {
                responseCode = parseResponseCodeFromStatus(status);
            }

        }

        return responseCode;

    }

    private static int parseResponseCodeFromStatus(String status) {

        // now parse it from the HTTP response directly.  I don't like
        // this here but it's the only way to do it with this
        // java.net.URL

        int begin = "HTTP/1.1 ".length();
        int offset = "200".length();
        int end = begin + offset;

        try {

            return Integer.parseInt( status.substring( begin, end ) );

        } catch ( NumberFormatException e ) {

            log.warn( "Unable to parse response code in header: " + status );
            return Integer.MIN_VALUE;

        }

    }

    /**
     * Return true if this NetworkException is transient and may be resolve
     * in the future.  This includes connect and read timeouts, HTTP 5xx, etc.
     */
    public boolean isTransient() {

        int responseCode = getResponseCode();

        return responseCode == HttpRequest.STATUS_CONNECT_TIMEOUT ||
               responseCode == HttpRequest.STATUS_READ_TIMEOUT ||
               (responseCode >= 500 && responseCode <= 599);

    }

    /**
     * Return true if this is an exception from a proxy server, vs the origin server.
     * This isn't always authoritative, but if we return true we're CERTAIN that
     * it's a proxy error.  Returning false might STILL be a proxy error though.
     */
    public boolean isProxyError() {

        String squidError =_urlConnection.getHeaderField( "X-Squid-Error" );

        if ( squidError != null )
            return true;

        return false;

    }

    public static class RequestBlockedDueToCaptcha extends NetworkException {

        public RequestBlockedDueToCaptcha(String link ) {
            super( "Request blocked due to captcha: " + link, 599 );
        }

    }

}
