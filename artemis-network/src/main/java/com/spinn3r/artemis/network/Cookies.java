package com.spinn3r.artemis.network;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.network.builder.HttpRequest;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class Cookies {

    public static ImmutableMap<String,Cookie> fromHttpRequest( HttpRequest httpRequest ) {
        return fromResponseHeadersMap( httpRequest.getResponseHeadersMap() );
    }

    public static ImmutableMap<String,Cookie> fromResponseHeadersMap( ImmutableMap<String,ImmutableList<String>> responseHeadersMap ) {

        Map<String,Cookie> cookies = Maps.newHashMap();

        List<String> setCookies = responseHeadersMap.get( "Set-Cookie" );

        if ( setCookies == null ) {
            return ImmutableMap.copyOf( cookies );
        }

        for (String setCookie : setCookies) {

            Cookie cookie = CookieDecoder.decode( setCookie );

            if ( cookie == null )
                continue;


            cookies.put( cookie.getName(), cookie );
        }

        return ImmutableMap.copyOf( cookies );

    }

}
