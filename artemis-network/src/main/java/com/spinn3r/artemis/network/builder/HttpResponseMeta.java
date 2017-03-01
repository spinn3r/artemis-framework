package com.spinn3r.artemis.network.builder;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.network.NetworkException;
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


    default boolean isHTML() throws NetworkException {
        return isContentType("text/html") || isContentType("application/xhtml+xml");
    }

    /**
     * Return true if this is text.  Sometimes HTML is improperly served as text.
     */
    default boolean isText() throws NetworkException {
        return isContentType("text/plain");
    }

    default boolean isXML() throws NetworkException {
        return isContentType("text/xml") || isContentType("application/xml");
    }

    default boolean isContentType(String expectedContentType) throws NetworkException {

        ImmutableList<String> contentTypes = getContentTypes();

        return contentTypes
                .stream()
                .anyMatch(expectedContentType::equalsIgnoreCase);

    }

    default ImmutableList<String> getContentTypes() throws NetworkException {

        return getResponseHeadersMap().get("Content-Type");

    }
}
