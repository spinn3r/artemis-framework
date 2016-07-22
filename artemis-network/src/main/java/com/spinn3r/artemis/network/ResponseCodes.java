package com.spinn3r.artemis.network;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Optional;

import static com.spinn3r.artemis.network.builder.HttpRequest.*;


/**
 *
 */
public class ResponseCodes {

    public static Optional<Integer> calculateFromException(Throwable cause ) {

        if ( cause instanceof SocketTimeoutException) {
            // TODO: are there write timeouts?
            return Optional.of(STATUS_READ_TIMEOUT);
        } else if ( cause instanceof ConnectException) {
            return Optional.of(STATUS_CONNECT_TIMEOUT);
        }

        return Optional.empty();

    }

}
