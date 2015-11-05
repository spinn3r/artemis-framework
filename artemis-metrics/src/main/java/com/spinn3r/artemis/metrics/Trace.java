package com.spinn3r.artemis.metrics;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Trace objects store name/value pairs about an operation.  This can then
 * be used for debugging, logging, and sending metrics.
 */
public class Trace {

    private Map<String,String> properties = new LinkedHashMap<>();

    public void put( String key, String value ) {
        properties.put( key, value );
    }

    public Set<String> keys() {
        return properties.keySet();
    }

    public String get( String key ) {
        return properties.get( key );
    }

}
