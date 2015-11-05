package com.spinn3r.artemis.sequence;

/**
 *
 */
public interface GlobalMutexFactory {

    public GlobalMutex acquire() throws GlobalMutexAcquireException;

    /**
     * Close the factory and release any connection resources.
     *
     * @throws Exception
     */
    public void close() throws Exception;

}