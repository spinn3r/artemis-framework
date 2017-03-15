package com.spinn3r.artemis.util.misc;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Getopt {

    private Map<String,String> params = new HashMap<>();

    private List<String> values = new ArrayList<>();

    public Getopt( String... args ) {
        this( Lists.newArrayList(args) );
    }

    /**
     * Parse command line arguments like --foo=bar where foo is the key and bar
     * is the value.
     *
     */
    public Getopt( List<String> args ) {

        Pattern pattern = Pattern.compile( "--([^=]+)=(.*)" );

        for( String arg : args ) {

            Matcher matcher = pattern.matcher( arg );

            if ( matcher.find() ) {

                String key = matcher.group( 1 );
                String value = matcher.group( 2 );

                params.put( key, value );

            } else {
                values.add( arg );
            }

        }

    }

    public void require( String... names ) {

        for( String name : names ) {

            if ( ! params.containsKey( name ))
                throw new IllegalArgumentException( "Required param not present: " + name );

        }

    }

    public boolean containsKey( String name ) {
        return params.containsKey( name );
    }

    public Map<String,String> getParams() {
        return params;
    }

    public List<String> getValues() {
        return values;
    }

    public String getString( String key ) {
        return getString( key, null );
    }

    public String getString( String key, String _default ) {

        if ( params.containsKey( key ) ) {
            return params.get( key );
        }

        return _default;

    }

    public boolean getBoolean( String key ) {
        return getBoolean( key, false );
    }

    public boolean getBoolean( String key, boolean _default ) {

        if ( params.containsKey( key ) ) {
            return "true".equals( params.get( key ) );
        }

        return _default;

    }

    public int getInt( String key ) {
        return getInt( key, 0 );
    }

    public Integer getInt( String key, Integer _default ) {

        if ( params.containsKey( key ) ) {
            return Integer.parseInt( params.get( key ) );
        }

        return _default;

    }

    public long getLong( String key ) {
        return getLong( key, 0 );
    }

    public long getLong( String key, long _default ) {

        if ( params.containsKey( key ) ) {
            return Long.parseLong( params.get( key ) );
        }

        return _default;

    }

    @Override
    public String toString() {
        return "Getopt{" +
                "params=" + params +
                ", values=" + values +
                '}';
    }
}
