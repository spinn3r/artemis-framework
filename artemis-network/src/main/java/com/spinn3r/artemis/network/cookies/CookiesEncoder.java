package com.spinn3r.artemis.network.cookies;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.util.misc.Strings;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static void updateCookies(Map<String, String> currentCookies, Map<String, List<String>> responseHeadersMap) {

        if (responseHeadersMap != null && responseHeadersMap.containsKey(Cookies.SET_COOKIE_HEADER_NAME)) {

            ImmutableMap<String, Cookie> setCookie = Cookies.fromResponseHeadersMap(responseHeadersMap);

            setCookie.entrySet().stream().forEach(stringCookieEntry -> {

                Cookie cookie = stringCookieEntry.getValue();
                String cookieName = stringCookieEntry.getKey();

                currentCookies.put(cookieName, cookie.getValue());

            });

        }
    }
}
