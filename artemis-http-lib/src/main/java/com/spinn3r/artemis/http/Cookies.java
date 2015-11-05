package com.spinn3r.artemis.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Cookies {

    public static Map<String,Cookie> buildCookieMap(HttpServletRequest req) {

        Map<String,Cookie> result = new HashMap<>();

        if ( req.getCookies() == null ) {
            return result;
        }

        for (Cookie cookie : req.getCookies()) {
            result.put( cookie.getName(), cookie );
        }

        return result;

    }

}
