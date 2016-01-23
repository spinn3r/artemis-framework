package com.spinn3r.artemis.sequence;

/**
 * Represents a named mutex with a unique name across all instances.
 */
public interface NamedMutex extends AutoCloseable {

    @Override
    void close() throws NamedMutexException;

}
