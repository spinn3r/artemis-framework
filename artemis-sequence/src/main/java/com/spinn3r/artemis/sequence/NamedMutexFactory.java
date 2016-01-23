package com.spinn3r.artemis.sequence;

/**
 *
 */
public interface NamedMutexFactory {

    NamedMutex acquire( String name ) throws NamedMutexException;

}
