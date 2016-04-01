package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.time.Clock;

/**
 *
 */
public class HttpRequestExecutorFactory {

    private final Clock clock;

    private final NetworkConfig networkConfig;

    @Inject
    public HttpRequestExecutorFactory(Clock clock, NetworkConfig networkConfig) {
        this.clock = clock;
        this.networkConfig = networkConfig;
    }

    public HttpRequestExecutor create() {
        return new HttpRequestExecutor( clock, networkConfig );
    }

}
