package com.spinn3r.artemis.network.validators;

import com.spinn3r.artemis.network.NetworkException;
import com.spinn3r.artemis.network.builder.HttpRequest;

/**
 *
 */
public interface HttpResponseValidator {

    void validate( HttpRequest httpRequest ) throws NetworkException;

}
