package com.spinn3r.artemis.network.builder;

import com.google.inject.Inject;
import com.spinn3r.artemis.network.init.NetworkConfig;
import com.spinn3r.artemis.util.Randomizer;

/**
 *
 */
public class UserAgentRandomizer {

    private static final String INSTAGRAM_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36";
    private final Randomizer<String> randomizer;

    @Inject
    UserAgentRandomizer(UserAgentsConfig userAgentsConfig) {
        this.randomizer = new Randomizer<>(userAgentsConfig.getUserAgents());
    }

    public String fetchRandomUserAgent(String resource) {

        if(isInstagram(resource)) {
            return INSTAGRAM_USER_AGENT;
        }

         return randomizer.fetch();

    }

    private boolean isInstagram(String resource) {

        if (resource == null) {
            return false;
        }

        if (resource.startsWith("https://www.instagram.com")) {
            return true;
        }

        if (resource.startsWith("https://instagram.com")) {
            return true;
        }

        if (resource.startsWith("http://www.instagram.com")) {
            return true;
        }

        if (resource.startsWith("http://instagram.com")) {
            return true;
        }

        return false;

    }

}
