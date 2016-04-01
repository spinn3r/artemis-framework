package com.spinn3r.artemis.network.builder;

import com.spinn3r.artemis.network.NetworkException;

/**
 *
 */
public interface NetworkRequester {

    /**
     * Execute a request and throw an exception if it fails.
     */
    HttpRequest execute() throws NetworkException;

}
