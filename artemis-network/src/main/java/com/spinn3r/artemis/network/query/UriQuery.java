package com.spinn3r.artemis.network.query;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A URI query for a fluent API on top of java.net.URI.
 */
public class UriQuery {

    private URI uri;

    private UriQuery( String url ) {

        try {

            this.uri = new URI( url );

        } catch (URISyntaxException e) {
            throw new RuntimeException( e );
        }

    }

    public String host() {
        return uri.getHost();
    }

    public static UriQuery uri( String value ) {
        return new UriQuery( value );
    }

}

