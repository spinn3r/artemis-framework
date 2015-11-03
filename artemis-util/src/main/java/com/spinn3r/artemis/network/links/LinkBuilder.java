package com.spinn3r.artemis.network.links;

import java.net.URI;
import java.net.URLEncoder;
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

    private Map<String,String> parameters = new LinkedHashMap<>();

    public LinkBuilder(URI uri) {
        this.scheme = uri.getScheme();
        this.host = uri.getHost();
        this.port = uri.getPort();
        this.path = uri.getPath();
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

    /**
     * Get an HTTP parameter.
     * @param key
     * @return
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

                buff.append( "=" );
                buff.append( URLEncoder.encode( entry.getValue() ) );

            }

        }

        return buff.toString();

    }

}
