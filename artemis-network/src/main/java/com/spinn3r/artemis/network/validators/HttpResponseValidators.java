package com.spinn3r.artemis.network.validators;

import com.google.common.collect.ImmutableList;

/**
 * Contains the list of HTTP validators we're using so that we can register additional
 * validators.
 */
public interface HttpResponseValidators {

    ImmutableList<HttpResponseValidator> getResponseValidators();

    void add( HttpResponseValidator httpResponseValidator );

}
