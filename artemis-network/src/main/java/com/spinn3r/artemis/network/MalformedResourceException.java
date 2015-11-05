package com.spinn3r.artemis.network;

/**
 * An exception which resulted from a malformed URL.
 */
public class MalformedResourceException extends NetworkException {

    public MalformedResourceException( String message ) {
        super( message );
    }

}
