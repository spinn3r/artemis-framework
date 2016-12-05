package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.util.Randomizer;

/**
 *
 */
public class UserAgentRandomizer {

    private final Randomizer<String> randomizer;

    @Inject
    UserAgentRandomizer(UserAgentsConfig userAgentsConfig) {
        this.randomizer = new Randomizer<>(userAgentsConfig.getUserAgents());
    }

    public String fetchRandomUserAgent() {
        return randomizer.fetch();
    }

}
