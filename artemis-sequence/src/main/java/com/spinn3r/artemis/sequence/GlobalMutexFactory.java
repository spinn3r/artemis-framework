package com.spinn3r.artemis.sequence;

/**
 *
 */
public interface GlobalMutexFactory {

    GlobalMutex acquire() throws GlobalMutexAcquireException;

    /**
     * Close the factory and release any connection resources.
     *
     * @throws Exception
     */
    void close() throws Exception;

}