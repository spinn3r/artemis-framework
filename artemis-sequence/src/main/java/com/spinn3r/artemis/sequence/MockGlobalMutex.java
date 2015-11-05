package com.spinn3r.artemis.sequence;

import com.spinn3r.artemis.sequence.GlobalMutex;
import com.spinn3r.artemis.sequence.GlobalMutexExpiredException;

/**
 *
 */
public class MockGlobalMutex implements GlobalMutex {

    @Override
    public int getValue() throws GlobalMutexExpiredException {
        return 0;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

}
