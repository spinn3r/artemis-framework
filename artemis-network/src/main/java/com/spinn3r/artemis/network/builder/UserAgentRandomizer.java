package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.util.Randomizer;

/**
 *
 */
public class UserAgentRandomizer {

    private final NetworkConfig networkConfig;

    private final Randomizer<String> randomizer;

    @Inject
    UserAgentRandomizer(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
        this.randomizer = new Randomizer<>(networkConfig.getUserAgents());
    }

    public String fetchRandomUserAgent() {
        return randomizer.fetch();
    }

}
