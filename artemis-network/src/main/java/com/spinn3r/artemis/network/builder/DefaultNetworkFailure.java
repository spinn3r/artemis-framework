package com.spinn3r.artemis.network.builder;

import com.spinn3r.artemis.network.NetworkFailure;
import com.spinn3r.artemis.network.ResourceRequest;
import com.spinn3r.log5j.Logger;

import java.net.SocketTimeoutException;
import java.net.URLConnection;

/**
 *
 */
public class DefaultNetworkFailure implements NetworkFailure {

    private static Logger log = Logger.getLogger();

    private int responseCode = Integer.MIN_VALUE;

    private ResourceRequest request = null;

    private URLConnection _urlConnection = null;

    // The status string in the HTTP response. Example: HTTP/1.1 200 OK
    private String status = null;

    public DefaultNetworkFailure(Exception e,
                                 ResourceRequest request,
                                 URLConnection _urlConnection) {

        this.request = request;
        this._urlConnection = _urlConnection;

        boolean timeout = e instanceof SocketTimeoutException;

        // do not attempt to read the status if we timed out...

        if ( _urlConnection != null && ! timeout ) {
            this.status = _urlConnection.getHeaderField( null );
        }

    }

    public DefaultNetworkFailure(ResourceRequest request,
                                 URLConnection _urlConnection) {

        this.request = request;
        this._urlConnection = _urlConnection;

        if ( _urlConnection != null ) {
            this.status = _urlConnection.getHeaderField( null );
        }

    }

    @Override
    public String getResource() {
        return request.getResource();
    }

    /**
     *
     * Get the HTTP response code form this error.
     */
    public int getResponseCode() {

        if ( responseCode == Integer.MIN_VALUE ) {

            if ( request != null && ( request.getResponseCode() == HttpRequest.STATUS_CONNECT_TIMEOUT || request.getResponseCode() == HttpRequest.STATUS_READ_TIMEOUT ) ) {

                // we have a connect or read timeout so yield to this value.

                responseCode = request.getResponseCode();

            } else if ( status == null ) {

                // some other type of error happened, set it to an unknown
                // status code.

                responseCode = -1;

            } else {

                // now parse it from the HTTP response directly.  I don't like
                // this here but it's the only way to do it with this
                // java.net.URL

                int begin = "HTTP/1.1 ".length();
                int offset = "200".length();
                int end = begin + offset;

                try {

                    responseCode = Integer.parseInt( status.substring( begin, end ) );

                } catch ( NumberFormatException e ) {

                    log.warn( "Unable to parse response code in header: " + status );
                    responseCode = -1;

                }

            }

        }

        return responseCode;

    }

    @Override
    public boolean isProxyError() {

        String squidError =_urlConnection.getHeaderField( "X-Squid-Error" );

        if ( squidError != null )
            return true;

        return false;

    }

}
