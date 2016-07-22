package com.spinn3r.artemis.network;

import com.spinn3r.artemis.network.builder.HttpRequest;
import com.spinn3r.log5j.Logger;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 *
 */
public class NetworkException extends IOException {

    private static Logger log = Logger.getLogger();

    public Exception e = null;

    private URL _url = null;

    private URLConnection _urlConnection = null;

    private final int responseCode;

    /**
     * The status string in the HTTP response.
     *
     * Example: HTTP/1.1 200 OK
     */
    public final String status;

    public NetworkException( String message ) {
        this( message , Integer.MIN_VALUE);
    }

    public NetworkException( String message, int responseCode ) {
        super( message );
        this.responseCode = responseCode;
        this.status = null;
    }

    public NetworkException( String message, Throwable t ) {
        super( message == null ?
                                    t.getClass().getName() + ": " + t.getMessage() :
                message + ": " +    t.getClass().getName() + ": " + t.getMessage() );

        Optional<Integer> responseCode = ResponseCodes.calculateFromException(t);
        if(responseCode.isPresent()){
            this.responseCode = responseCode.get();
        }else {
            this.responseCode = Integer.MIN_VALUE;
        }
        this.status = null;

        super.initCause(t);

    }

    public NetworkException( Throwable t ) {

        this( null, t );
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
        this._url = _url;
        this._urlConnection = _urlConnection;

        Optional<Integer> responseCodeFromException = ResponseCodes.calculateFromException(e);

        boolean timeout = responseCodeFromException.isPresent() && ResponseCodes.isTimeoutResponseCode(responseCodeFromException.get());

        // do not attempt to read the status if we timed out...

        if ( _urlConnection != null && ! timeout ) {
            this.status = _urlConnection.getHeaderField( null );
        } else {
            this.status = null;
        }
        Optional<Integer> parseResponseCodeFromStatus = parseResponseCodeFromStatus(status);


        if(parseResponseCodeFromStatus.isPresent()){
            this.responseCode = parseResponseCodeFromStatus.get();

        }else if(responseCodeFromException.isPresent()){
            this.responseCode = responseCodeFromException.get();

        }else {
            this.responseCode = Integer.MIN_VALUE;
        }

        super.initCause(e);

    }

    public NetworkException( String message,
                             ResourceRequest request,
                             URL _url,
                             URLConnection _urlConnection ) {

        super( request.getResource() + ": " + message );
        this._url = _url;
        this._urlConnection = _urlConnection;

        if ( _urlConnection != null ) {
            this.status = _urlConnection.getHeaderField( null );
            Optional<Integer> responseCodeFromStatus = parseResponseCodeFromStatus(status);
            if(responseCodeFromStatus.isPresent()){
                this.responseCode = responseCodeFromStatus.get();
            }else{
                this.responseCode = Integer.MIN_VALUE;
            }
        } else {
            this.status = null;
            this.responseCode = Integer.MIN_VALUE;

        }

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

        return responseCode;
    }

    private static Optional<Integer> parseResponseCodeFromStatus(String status) {

        if(status == null){
            return Optional.empty();
        }
        // now parse it from the HTTP response directly.  I don't like
        // this here but it's the only way to do it with this
        // java.net.URL

        int begin = "HTTP/1.1 ".length();
        int offset = "200".length();
        int end = begin + offset;

        try {

            return Optional.of(Integer.parseInt( status.substring( begin, end ) ));

        } catch ( NumberFormatException | IndexOutOfBoundsException e1) {

            log.warn( "Unable to parse response code in header: " + status );
            return Optional.empty();

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
