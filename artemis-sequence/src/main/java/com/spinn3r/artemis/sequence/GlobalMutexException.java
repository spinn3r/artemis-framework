package com.spinn3r.artemis.sequence;

/**
 *  Thrown in the event of being unable to work with global mutexes.
 */
public abstract class GlobalMutexException extends Exception {

    protected GlobalMutexException(String message) {
        super( message );
    }

    protected GlobalMutexException(String message, Throwable cause) {
        super( message, cause );
    }

}
