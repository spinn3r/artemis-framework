package com.spinn3r.artemis.sequence.none.init;

import com.google.inject.Provider;
import com.spinn3r.artemis.init.AtomicReferenceProvider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.init.ServiceReference;
import com.spinn3r.artemis.sequence.GlobalMutex;

/**
 *
 */
public class NoGlobalMutexService extends BaseService {

    public static final ServiceReference REF = new ServiceReference( NoGlobalMutexService.class );

    private Provider<GlobalMutex> globalMutexProvider = new AtomicReferenceProvider<>( null );

    @Override
    public void init() {
        provider( GlobalMutex.class, globalMutexProvider );
    }

}
