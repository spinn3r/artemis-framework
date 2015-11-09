package com.spinn3r.artemis.sequence.zookeeper;

import com.spinn3r.artemis.sequence.GlobalMutex;
import com.spinn3r.artemis.sequence.GlobalMutexExpiredException;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Global mutex backed by ZooKeeper.
 */
public class ZKGlobalMutex implements GlobalMutex {

    private AtomicBoolean expired = new AtomicBoolean( false );

    private int value;

    public ZKGlobalMutex(int value) {
        this.value = value;
    }

    protected AtomicBoolean getExpired() {
        return expired;
    }

    @Override
    public boolean isExpired() {
        return expired.get();
    }

    @Override
    public int getValue() throws GlobalMutexExpiredException {
        return value;
    }

}
