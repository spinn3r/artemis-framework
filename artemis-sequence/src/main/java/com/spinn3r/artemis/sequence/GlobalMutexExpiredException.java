package com.spinn3r.artemis.sequence;

/**
 * Exception thrown if this instance is expired.
 */
public class GlobalMutexExpiredException extends GlobalMutexException {

    public GlobalMutexExpiredException(String message) {
        super( message );
    }

    public GlobalMutexExpiredException(String message, Throwable cause) {
        super( message, cause );
    }

}
