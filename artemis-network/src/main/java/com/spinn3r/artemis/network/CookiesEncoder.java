package com.spinn3r.artemis.network;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.misc.Strings;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class CookiesEncoder {

    public static String encode( Map<String,String> cookies ) {

        StringBuilder buff = new StringBuilder();

        // foo=bar; cat=dog format.

        List<String> keyValuePairs = Lists.newArrayList();

        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            keyValuePairs.add( String.format( "%s=%s", entry.getKey(), entry.getValue() ) );
        }

        return Joiner.on( "; " ).join( keyValuePairs );

    }

}
