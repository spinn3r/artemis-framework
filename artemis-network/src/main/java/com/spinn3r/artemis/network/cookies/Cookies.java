package com.spinn3r.artemis.network.cookies;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.network.builder.HttpRequest;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class Cookies {

    public static ImmutableMap<String,String> toMap(List<Cookie> cookies) {

        Map<String,String> result = Maps.newLinkedHashMap();

        for (Cookie cookie : cookies) {
            result.put(cookie.getName(), cookie.getValue());
        }

        return ImmutableMap.copyOf(result);

    }

    public static ImmutableMap<String,Cookie> toCookieMap(List<Cookie> cookies) {

        Map<String, Cookie> result = Maps.newLinkedHashMap();

        for (Cookie cookie : cookies) {
            result.put(cookie.getName(), cookie);
        }

        return ImmutableMap.copyOf(result);

    }

    public static ImmutableList<Cookie> toCookieList(Map<String,String> cookies) {

        checkNotNull( cookies );

        List<Cookie> result = Lists.newArrayList();

        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();

            checkNotNull( name, "name" );
            checkNotNull( value, "value" );
            result.add( new Cookie(name, value) );

        }

        return ImmutableList.copyOf(result);

    }

    public static ImmutableMap<String,Cookie> fromHttpRequest( HttpRequest httpRequest ) {
        return fromResponseHeadersMap( httpRequest.getResponseHeadersMap() );
    }

    public static ImmutableMap<String,Cookie> fromResponseHeadersMap( ImmutableMap<String,ImmutableList<String>> responseHeadersMap ) {
        return fromSetCookiesList( responseHeadersMap.get( "Set-Cookie" ) );
    }

    public static ImmutableMap<String,Cookie> fromSetCookiesList( List<String> setCookies ) {

        Map<String,Cookie> cookies = Maps.newHashMap();

        if ( setCookies == null || setCookies.size() == 0 ) {
            return ImmutableMap.copyOf( cookies );
        }

        for (String setCookie : setCookies) {

            Cookie cookie = CookieDecoder.decodeSingleResponseCookie(setCookie );

            if ( cookie == null )
                continue;


            cookies.put( cookie.getName(), cookie );

        }

        return ImmutableMap.copyOf( cookies );

    }

}
