package com.spinn3r.artemis.init.resource_mutexes;

/**
 *
 */
@SuppressWarnings( "serial" )
public class ResourceMutexException extends Exception {

    public ResourceMutexException() {
    }

    public ResourceMutexException(String message) {
        super( message );
    }

    public ResourceMutexException(String message, Throwable cause) {
        super( message, cause );
    }

    public ResourceMutexException(Throwable cause) {
        super( cause );
    }

    /**
     * Thrown when we have no more mutexes available to use.
     */
    static class ExhaustedException extends ResourceMutexException {

        public ExhaustedException() {
        }

        public ExhaustedException(String message) {
            super( message );
        }

        public ExhaustedException(String message, Throwable cause) {
            super( message, cause );
        }

        public ExhaustedException(Throwable cause) {
            super( cause );
        }
    }

    /**
     * Thrown on general failure.
     */
    static class FailureException extends ResourceMutexException {

        public FailureException() {
        }

        public FailureException(String message) {
            super( message );
        }

        public FailureException(String message, Throwable cause) {
            super( message, cause );
        }

        public FailureException(Throwable cause) {
            super( cause );
        }

    }

}
