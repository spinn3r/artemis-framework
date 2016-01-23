package com.spinn3r.artemis.sequence;

/**
 * Represents a named mutex with a unique name across all instances.
 */
public interface NamedMutex {

    void release() throws NamedMutexException;

}
