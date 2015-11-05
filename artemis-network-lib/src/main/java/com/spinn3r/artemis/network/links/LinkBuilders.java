package com.spinn3r.artemis.network.links;

/**
 *
 */
public class LinkBuilders {

    public static LinkBuilder parse( String url ) {

        Link link = Links.parse( url );
        return new LinkBuilder( link.getUri(), link.getParameters() );

    }

}
