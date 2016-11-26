package com.spinn3r.artemis.init;

/**
 *
 */

public class AutoServiceException extends RuntimeException {

    protected AutoServiceException() {
    }

    protected AutoServiceException(String message) {
        super(message);
    }

    protected AutoServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AutoServiceException(Throwable cause) {
        super(cause);
    }

    public static class StartFailedException extends AutoServiceException {

        public StartFailedException() {
        }

        public StartFailedException(String message) {
            super(message);
        }

        public StartFailedException(String message, Throwable cause) {
            super(message, cause);
        }

        public StartFailedException(Throwable cause) {
            super(cause);
        }
    }

    public static class StopFailedException extends AutoServiceException {

        public StopFailedException() {
        }

        public StopFailedException(String message) {
            super(message);
        }

        public StopFailedException(String message, Throwable cause) {
            super(message, cause);
        }

        public StopFailedException(Throwable cause) {
            super(cause);
        }
    }

}
