package com.spinn3r.artemis.init;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.init.Advertised;

import java.util.*;

/**
 *
 */
class AdvertisedFormatter {

    private Advertised advertised;

    AdvertisedFormatter(Advertised advertised) {
        this.advertised = advertised;
    }

    public String format() {

        Map<String,String> formattedMap = new TreeMap<>();

        for (Class key : advertised.advertisements.keySet()) {

            Object value = advertised.advertisements.get( key );

            String valueFormatted = value.getClass().getName();

            if ( value instanceof Class ) {
                valueFormatted = ( (Class) value ).getName();
            }

            formattedMap.put( key.getName(), valueFormatted );

        }

        StringBuilder buff = new StringBuilder();

        int maxKeyLen = maxLen( formattedMap.keySet() );
        int maxValueLen = maxLen( formattedMap.values() );

        String format = fmt( maxKeyLen ) + " = " + fmt( maxValueLen );

        for ( String key : formattedMap.keySet() ) {

            String value = formattedMap.get( key );

            buff.append( "    " );
            buff.append( String.format( format, key, value ) );
            buff.append( "\n" );

        }

        return buff.toString();
    }

    private int maxLen( Collection<String> values ) {

        int result = 0;

        for (String value : values) {
            if ( value.length() > result ) {
                result = value.length();
            }
        }

        return result;

    }

    private String fmt( int len ) {
        return "%-" + len + "s";
    }

}
