
package com.spinn3r.artemis.network.links;

import com.google.common.net.UrlEscapers;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Build a URL from HTTP parameters.
 */
public class LinkBuilder {

    private String scheme = "http";

    private String host;

    private int port = 80;

    private String path;

    // TODO: this should be a linked multimap...
    private Map<String,String> parameters = new LinkedHashMap<>();

    public LinkBuilder(URI uri) {
        this.scheme = uri.getScheme();
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.path = uri.getPath();
    }

    public LinkBuilder(String scheme, String host, int port, String path) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public LinkBuilder(URI uri, LinkedHashMap<String,String> parameters) {
        this( uri );
        this.parameters = parameters;
    }

    public LinkBuilder(String host, String path) {
        this.host = host;
        this.path = path;
    }

    public LinkBuilder setPath( String path ) {
        this.path = path;
        return this;
    }

    /**
     * Add an HTTP parameter.
     *
     * @param key
     * @param value
     */
    public LinkBuilder put( String key, String value ) {
        parameters.put( key, value );
        return this;
    }

    public LinkBuilder put( String key, int value ) {
        parameters.put( key, "" + value );
        return this;
    }

    public LinkBuilder put( String key, long value ) {
        parameters.put( key, "" + value );
        return this;
    }

    public LinkBuilder remove( String key ) {
        parameters.remove( key );
        return this;
    }

    /**
     * Get an HTTP parameter.
     */
    public String get( String key ) {
        return parameters.get( key );
    }

    public String format() {

        StringBuilder buff = new StringBuilder();

        buff.append( scheme );
        buff.append( "://" );
        buff.append( host );

        if ( port != 80 && port >= 0 ) {
            buff.append( ":" );
            buff.append( port );
        }

        buff.append( path );

        // now add HTTP params

        if ( parameters.size() > 0 ) {
            buff.append( "?" );
            buff.append( join( parameters ) );
        }

        return buff.toString();

    }

    @Override
    public String toString() {
        return format();
    }

    protected static String join( Map<String,String> parameters ) {

        StringBuilder buff = new StringBuilder();

        for (Map.Entry<String, String> entry : parameters.entrySet()) {

            if ( buff.length() > 0 ) {
                buff.append( "&" );
            }

            buff.append( entry.getKey() );

            if ( entry.getValue() != null ) {

                String escapedValue = UrlEscapers
                                        .urlFormParameterEscaper()
                                        .escape((entry.getValue()));

                buff.append( "=" );
                buff.append(escapedValue);

            }

        }

        return buff.toString();

    }

}
