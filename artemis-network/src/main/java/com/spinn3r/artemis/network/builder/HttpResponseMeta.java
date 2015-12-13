package com.spinn3r.artemis.network.builder;

import java.util.List;
import java.util.Map;

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
    Map<String, List<String>> getResponseHeaderMap();

}
