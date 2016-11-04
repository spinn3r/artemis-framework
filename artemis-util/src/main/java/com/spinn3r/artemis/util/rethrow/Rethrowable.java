package com.spinn3r.artemis.util.rethrow;

/**
 *
 */
public interface Rethrowable<E extends Exception> {

    void exec() throws E;

}
