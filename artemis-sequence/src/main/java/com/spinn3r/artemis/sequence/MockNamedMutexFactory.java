package com.spinn3r.artemis.sequence;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 */
public class MockNamedMutexFactory implements NamedMutexFactory {

    protected ConcurrentMap<String,Boolean> index = Maps.newConcurrentMap();

    @Override
    public NamedMutex acquire(String name) throws NamedMutexException {

        if ( index.putIfAbsent( name, true ) == null ) {
            return new MockNamedMutex( name );
        }

        throw new NamedMutexException.AcquireException( "Failed to acquire mutex: " + name );

    }

    class MockNamedMutex implements NamedMutex {

        private final String name;

        private AtomicBoolean released = new AtomicBoolean( false );

        public MockNamedMutex(String name) {
            this.name = name;
        }

        @Override
        public void close() throws NamedMutexException {

            if ( released.get() ) {
                throw new NamedMutexException.AlreadyReleasedException( "Mutex is already released: " + name );
            }

            index.remove( name );

            released.set( true );

        }

    }

}


