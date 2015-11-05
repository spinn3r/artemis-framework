package com.spinn3r.artemis.http;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public class HttpServletRequests {

    public static String computeRequestURL(HttpServletRequest httpServletRequest) {

        String result = httpServletRequest.getRequestURL().toString();

        if ( httpServletRequest.getQueryString() != null ) {
            result += "?" + httpServletRequest.getQueryString();
        }

        return result;

    }

}
