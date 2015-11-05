package com.spinn3r.artemis.network.builder.listener;

import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.HttpRequestMeta;
import com.spinn3r.artemis.network.builder.HttpRequestMethod;

/**
 *
 */
public interface RequestListener {

    /**
     * Called for each request to see if we need to perform an special handling
     * of the request.  We can throw customer HTTP exceptions here.
     *
     * @throws NetworkException
     */
    void onContentWithEncoding( HttpRequestMeta httpRequestMeta, String content ) throws NetworkException;

}
