package com.spinn3r.artemis.network;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Encodes HTTP POST parameters for a request.
 */
public class PostEncoder {

    @SuppressWarnings( "deprecation" )
    public static String encode( Map<String,?> map ) {

        StringBuilder buff = new StringBuilder();

        for (Map.Entry<String, ?> entry : map.entrySet()) {

            if ( buff.length() != 0 )
                buff.append( "&" );

            buff.append( entry.getKey() );
            buff.append( "=" );
            buff.append( URLEncoder.encode( entry.getValue().toString() ) );

        }

        return buff.toString();

    }

}
