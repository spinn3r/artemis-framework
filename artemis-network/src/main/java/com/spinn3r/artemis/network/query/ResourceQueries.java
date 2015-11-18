package com.spinn3r.artemis.network.query;

/**
 *
 */
public class ResourceQueries {

    public static ResourceQuery resource() {
        return resource(null);
    }

    public static ResourceQuery resource(String value) {
        return new ResourceQuery( value );
    }

}
