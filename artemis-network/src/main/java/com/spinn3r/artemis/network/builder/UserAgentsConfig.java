package com.spinn3r.artemis.network.builder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.ImmutableList;
import com.google.inject.Singleton;
import com.spinn3r.artemis.init.AutoConfiguration;

/**
 *
 */
@AutoConfiguration( path = "user-agents.conf", required = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Singleton
public class UserAgentsConfig {

    private ImmutableList<String> userAgents = ImmutableList.of();

    public ImmutableList<String> getUserAgents() {
        return userAgents;
    }

    @Override
    public String toString() {
        return "UserAgentsConfig{" +
                 "userAgents=" + userAgents +
                 '}';
    }

}
