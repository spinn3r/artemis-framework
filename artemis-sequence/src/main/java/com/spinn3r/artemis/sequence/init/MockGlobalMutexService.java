package com.spinn3r.artemis.sequence.init;

import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.sequence.GlobalMutex;
import com.spinn3r.artemis.sequence.GlobalMutexFactory;
import com.spinn3r.artemis.sequence.MockGlobalMutex;
import com.spinn3r.artemis.sequence.MockGlobalMutexFactory;

/**
 *
 */
public class MockGlobalMutexService extends BaseService {

    private AtomicReferenceProvider<GlobalMutex> globalMutexProvider = new AtomicReferenceProvider<>( null );

    private AtomicReferenceProvider<GlobalMutexFactory> globalMutexFactoryProvider = new AtomicReferenceProvider<>( null );

    @Override
    public void init() {
        provider( GlobalMutex.class, globalMutexProvider );
        provider( GlobalMutexFactory.class, globalMutexFactoryProvider );
    }

    @Override
    public void start() throws Exception {

        globalMutexProvider.set( new MockGlobalMutex() );
        globalMutexFactoryProvider.set( new MockGlobalMutexFactory() );

    }

}
