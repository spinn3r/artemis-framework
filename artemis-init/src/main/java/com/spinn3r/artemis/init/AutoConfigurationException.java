package com.spinn3r.artemis.init;

import java.io.IOException;

/**
 *
 */
public class AutoConfigurationException extends IOException {

    public AutoConfigurationException() {
        super();
    }

    public AutoConfigurationException(String message) {
        super(message);
    }

    public AutoConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutoConfigurationException(Throwable cause) {
        super(cause);
    }

    public static class InvalidPathException extends AutoConfigurationException {

        public InvalidPathException() {
        }

        public InvalidPathException(String message) {
            super(message);
        }

        public InvalidPathException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class MissingConfigException extends AutoConfigurationException {

        public MissingConfigException() {
        }

        public MissingConfigException(String message) {
            super(message);
        }

        public MissingConfigException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
