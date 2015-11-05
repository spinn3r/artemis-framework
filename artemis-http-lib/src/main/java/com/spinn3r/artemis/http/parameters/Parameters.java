package com.spinn3r.artemis.http.parameters;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fluent interface for dealing with HTTP parameters.
 */
public class Parameters {

    private Map<String,List<String>> params = new HashMap<>();

    public Parameters(Map<String,List<String>> params) {
        this.params = params;
    }

    public Parameter get( String key ) {
        return get( key, null );
    }

    public Parameter get( String key, String _default ) {

        List<String> values = params.get( key );

        if ( ( values == null || values.size() == 0 ) && _default != null ) {
            values = Lists.newArrayList( _default );
        }

        return new Parameter( key, values );
    }

    public boolean contains( String key ) {
        return params.containsKey( key );
    }

    /**
     * Require that all of the given parameters are specified.
     *
     * @param keys
     * @throws IllegalArgumentException
     */
    public void require( String... keys ) throws IllegalArgumentException {

        for (String key : keys) {
            if ( ! params.containsKey( key ) ) {
                throw new IllegalArgumentException( "Required parameter not specified: " + key );
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof Parameters )) return false;

        Parameters that = (Parameters) o;

        if (!params.equals( that.params )) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return params.hashCode();
    }

    @Override
    public String toString() {
        return params.toString();
    }

    public static Parameters fromMap( Map<String,List<String>> params ) {
        return new Parameters( params );
    }

    public static Parameters fromRequest( Map<String,String[]> params ) {

        Map<String, List<String>> wrapper = new HashMap<>();

        for (String key : params.keySet()) {

            List<String> entry = new ArrayList<>();

            String[] values = params.get( key );

            for (String value : values) {
                entry.add( value );
            }

            wrapper.put( key, entry );

        }

        return new Parameters( wrapper );

    }

}
