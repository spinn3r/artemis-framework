package com.spinn3r.artemis.sequence.init;

import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.sequence.*;

/**
 *
 */
public class MockGlobalMutexService extends BaseService {

    private AtomicReferenceProvider<GlobalMutex> globalMutexProvider = new AtomicReferenceProvider<>( null );

    private AtomicReferenceProvider<GlobalMutexFactory> globalMutexFactoryProvider = new AtomicReferenceProvider<>( null );

    private AtomicReferenceProvider<NamedMutexFactory> namedMutexFactoryProvider = new AtomicReferenceProvider<>( null );

    @Override
    public void init() {
        provider( GlobalMutex.class, globalMutexProvider );
        provider( GlobalMutexFactory.class, globalMutexFactoryProvider );
        provider( NamedMutexFactory.class, namedMutexFactoryProvider );
    }

    @Override
    public void start() throws Exception {

        globalMutexProvider.set( new MockGlobalMutex() );
        globalMutexFactoryProvider.set( new MockGlobalMutexFactory() );
        namedMutexFactoryProvider.set( new MockNamedMutexFactory() );

    }

}
