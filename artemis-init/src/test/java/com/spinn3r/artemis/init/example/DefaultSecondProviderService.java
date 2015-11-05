package com.spinn3r.artemis.init.example;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.spinn3r.artemis.init.BaseService;

/**
 *
 */
@Singleton
public class DefaultSecondProviderService extends BaseService implements Provider<Second> {

    private Second second;

    private final First first;

    @Inject
    public DefaultSecondProviderService(First first) {
        this.first = first;

    }

    @Override
    public void init() {
        provider( Second.class, this );
    }

    @Override
    public void start() throws Exception {
        second = new DefaultSecond( first );
    }

    @Override
    public void stop() throws Exception {
        second = null;
    }

    @Override
    public Second get() {
        return second;
    }

}
