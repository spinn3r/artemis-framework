package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.network.cookies.Cookie;

/**
 *
 */
public interface HttpResponseMeta {

    /**
     * Get the original requested URL.
     *
     * @return
     */
    String getResource();

    /**
     * Get the URL that we were redirected to.
     *
     * @return
     */
    String getResourceFromRedirect();

    /**
     * Get the HTTP response code present from teh resource.
     *
     * @return
     */
    int getResponseCode();

    /**
     * Get the HTTP headers which were present in the HTTP response.
     *
     * @return
     */
    ImmutableMap<String, ImmutableList<String>> getResponseHeadersMap();

    /**
     * Get the HTTP cookies associated with this request.  Return an emtpy map
     * if there are no cookies.
     */
    ImmutableMap<String,Cookie> getCookies();

}
