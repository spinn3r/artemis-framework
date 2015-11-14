package com.spinn3r.artemis.jcommander;

/**
 * Allows us to handle --help and with an Exception so we can handle it in one place.
 *
 * The message of the exception is the help/usage message.
 */
public class HelpException extends Exception {

    public HelpException(String message) {
        super( message );
    }

    public HelpException(String message, Throwable cause) {
        super( message, cause );
    }

}
