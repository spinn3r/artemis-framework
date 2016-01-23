package com.spinn3r.artemis.sequence;

/**
 * Thrown if we're unable to acquire a named mutex.
 */
public class NamedMutexException extends Exception {

    private NamedMutexException(String message) {
        super( message );
    }

    private NamedMutexException(String message, Throwable cause) {
        super( message, cause );
    }

    public static class AcquireException extends NamedMutexException {

        public AcquireException(String message) {
            super( message );
        }

        public AcquireException(String message, Throwable cause) {
            super( message, cause );
        }
    }

    /**
     * General failure to acquire the lock.. this is not recoverable.
     */
    public static class FailureException extends NamedMutexException {

        public FailureException(String message) {
            super( message );
        }

        public FailureException(String message, Throwable cause) {
            super( message, cause );
        }
    }

    /**
     * True if we attempt to release a mutex that is already released.
     */
    public static class AlreadyReleasedException extends NamedMutexException {

        public AlreadyReleasedException(String message) {
            super( message );
        }

        public AlreadyReleasedException(String message, Throwable cause) {
            super( message, cause );
        }
    }

}
