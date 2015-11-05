package com.spinn3r.artemis.network.links;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
public class Link {

    private URI uri;

    private LinkedHashMap<String,String> parameters;

    public Link(URI uri, LinkedHashMap<String, String> parameters) {
        this.uri = uri;
        this.parameters = parameters;
    }

    public URI getUri() {
        return uri;
    }

    public LinkedHashMap<String, String> getParameters() {
        return parameters;
    }

    public String get( String key ) {
        return parameters.get( key );
    }

    public String get( String key, String _default ) {

        String result = parameters.get( key );

        if ( result == null ) {
            return _default;
        }

        return result;

    }

}
