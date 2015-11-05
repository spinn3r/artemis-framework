package com.spinn3r.artemis.sequence;

/**
 * Provide mutual exclusion (mutex) support on a global customer basis.  This was
 * designed to support the SequenceGenerator and supports mutex values which are
 * 14 bits.
 */
public interface GlobalMutex {

    /**
     * Get the value of the global mutex for use with a SequenceGenerator or
     * throw an exception if it's expired.
     * @return
     * @throws GlobalMutexExpiredException
     */
    int getValue() throws GlobalMutexExpiredException;

    /**
     *
     * @return
     */
    boolean isExpired();

}
