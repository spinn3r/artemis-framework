package com.spinn3r.artemis.sequence;

import com.google.inject.Singleton;

/**
 *
 */
@Singleton
public class MockGlobalMutexFactory implements GlobalMutexFactory {

    @Override
    public GlobalMutex acquire() throws GlobalMutexAcquireException {
        return new MockGlobalMutex();
    }

    @Override
    public void close() throws Exception {

    }
}
