package com.spinn3r.artemis.network;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Map;

/**
 * Encodes HTTP POST parameters for a request.
 */
public class PostEncoder {

    @SuppressWarnings( "deprecation" )
    public static String encode(Map<String,?> map ) {

        StringBuilder buff = new StringBuilder();

        for (Map.Entry<String, ?> entry : map.entrySet()) {

            if ( buff.length() != 0 )
                buff.append( "&" );

            buff.append( entry.getKey() );
            buff.append( "=" );
            buff.append( URLEncoder.encode(entry.getValue().toString(), Charsets.UTF_8) );

        }

        return buff.toString();

    }

    public static String encode(ImmutableMultimap<String,?> map) {

        StringBuilder buff = new StringBuilder();

        for (String key : map.keySet()) {

            Collection<?> values = map.get(key);

            for (Object value : values) {

                if ( buff.length() != 0 )
                    buff.append( "&" );

                buff.append( URLEncoder.encode(key, Charsets.UTF_8));
                buff.append( "=" );
                buff.append( URLEncoder.encode(value.toString(), Charsets.UTF_8) );

            }

        }

        return buff.toString();

    }

}
