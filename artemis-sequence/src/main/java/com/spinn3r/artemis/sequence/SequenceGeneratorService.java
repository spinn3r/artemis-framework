package com.spinn3r.artemis.sequence;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.spinn3r.artemis.init.BaseService;
import com.spinn3r.artemis.time.Clock;

/**
 *
 */
public class SequenceGeneratorService extends BaseService {

    private final Clock clock;

    private final Provider<GlobalMutex> globalMutexProvider;

    @Inject
    SequenceGeneratorService(Clock clock, Provider<GlobalMutex> globalMutexProvider) {
        this.clock = clock;
        this.globalMutexProvider = globalMutexProvider;
    }

    @Override
    public void init() {
        advertise( SequenceGenerator.class,  new SequenceGenerator(clock, globalMutexProvider));
    }

}
