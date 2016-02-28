package com.spinn3r.artemis.network.query;


import com.spinn3r.artemis.network.ResourceExpander;
import com.spinn3r.artemis.network.ResourceTokenizer;
import com.spinn3r.artemis.util.hashcodes.Hashcodes;

/**
 * A query object to manipulate URLs.
 */
public class ResourceQuery {

    private final String value;

    public ResourceQuery(String value) {
        this.value = value;
    }

    public ResourceQuery tokenize() {

        if ( value == null )
            return ResourceQueries.resource();

        return ResourceQueries.resource( ResourceTokenizer.tokenize4( value ) );

    }

    public ResourceQuery removeScheme() {

        if ( value == null )
            return ResourceQueries.resource();

        return ResourceQueries.resource( value.replaceFirst( "https?://", "" ) );

    }

    public ResourceQuery removeNamedAnchor() {

        if ( value == null )
            return ResourceQueries.resource();

        return ResourceQueries.resource( value.replaceFirst( "#.*", "" ) );

    }

    /**
     * Expand a relative link.
     *
     * @param relative
     * @return
     */
    public ResourceQuery expand( String relative ) {

        if ( value == null )
            return ResourceQueries.resource();

        return ResourceQueries.resource( ResourceExpander.expand( value, relative ) );

    }

    /**
     *
     * @return
     */
    public ResourceQuery removePort() {

        // TODO: make this into a remove() method which then allows you to remove
        // parts of the URL like

        if ( value == null )
            return ResourceQueries.resource();

        return ResourceQueries.resource( value.replaceFirst( ":[0-9]{2,4}", "" ) );

    }

    public String scheme() {

        if ( value == null )
            return null;

        if ( value.startsWith( "http:" ) )
            return "http";

        if ( value.startsWith( "https:" ) )
            return "https";

        return null;

    }


    /**
     * Return the root domain. So if we have example
     * @return
     */
    public ResourceQuery domain() {

        if ( value == null )
            return ResourceQueries.resource();

        return ResourceQueries.resource( ResourceTokenizer.rootDomainTokenize( value ) );

    }

    public ResourceQuery host() {
        return site();
    }

    /**
     * Read the host part of the URL with just the host.
     *
     * http://www.cnn.com/index.html
     *
     * would return
     *
     * http://www.cnn.com
     *
     * @return
     */
    public ResourceQuery site() {

        if ( value == null )
            return ResourceQueries.resource();

        return ResourceQueries.resource( ResourceTokenizer.domainTokenize( value ) );

    }

    /**
     * Compute the request path which is just the URL with path and query string.
     * @return
     */
    public ResourceQuery pathAndQuery() {

        if ( value == null )
            return ResourceQueries.resource();

        String result = value;

        result = result.replaceFirst( "https?://", "" );

        int end = result.indexOf( "/" );

        if ( end > -1 ) {
            result = result.substring( end, result.length() );
        } else {
            return ResourceQueries.resource( "" );
        }

        return ResourceQueries.resource( result );

    }


    public String toHashcode() {

        if ( value == null )
            return null;

        return Hashcodes.getHashcode( value );

    }

    @Override
    public String toString() {
        return value;
    }

}
