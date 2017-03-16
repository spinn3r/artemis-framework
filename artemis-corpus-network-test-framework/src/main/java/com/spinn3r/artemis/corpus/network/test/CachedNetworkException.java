package com.spinn3r.artemis.corpus.network.test;

import com.spinn3r.artemis.network.NetworkException;

/**
 *
 */
public class CachedNetworkException extends NetworkException {

    private CachedNetworkException(String message) {
        super(message);
    }

    public static class NotCachedException extends CachedNetworkException {

        public NotCachedException(String message) {
            super(message);
        }

    }

}
