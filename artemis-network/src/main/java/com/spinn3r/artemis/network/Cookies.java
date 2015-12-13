package com.spinn3r.artemis.network;

import com.google.common.collect.Maps;
import com.spinn3r.artemis.network.builder.HttpRequest;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class Cookies {

    public static Map<String,Cookie> fromHttpRequest(HttpRequest httpRequest ) {
        return fromResponseHeadersMap( httpRequest.getResponseHeadersMap() );
    }

    public static Map<String,Cookie> fromResponseHeadersMap(Map<String,List<String>>  responseHeadersMap ) {

        Map<String,Cookie> cookies = Maps.newHashMap();

        List<String> setCookies = responseHeadersMap.get( "Set-Cookie" );

        if ( setCookies == null ) {
            return cookies;
        }

        for (String setCookie : setCookies) {

            Cookie cookie = CookieDecoder.decode( setCookie );

            if ( cookie == null )
                continue;


            cookies.put( cookie.getName(), cookie );
        }

        return cookies;

    }

}
