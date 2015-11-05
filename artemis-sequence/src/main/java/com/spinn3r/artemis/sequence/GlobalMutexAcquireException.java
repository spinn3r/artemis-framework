package com.spinn3r.artemis.sequence;

/**
 * Thrown if we're unable to acquire a mutex.
 */
public class GlobalMutexAcquireException extends GlobalMutexException {

    public GlobalMutexAcquireException(String message) {
        super( message );
    }

    public GlobalMutexAcquireException(String message, Throwable cause) {
        super( message, cause );
    }

}
