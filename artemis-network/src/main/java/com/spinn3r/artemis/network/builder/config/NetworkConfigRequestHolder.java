package com.spinn3r.artemis.network.builder.config;

import com.spinn3r.artemis.network.init.NetworkConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wraps a request config so that we can cache regexp patterns, etc.
 */
public class NetworkConfigRequestHolder {

    private final NetworkConfig.RequestSettings networkConfigRequest;

    private final Pattern pattern;

    public NetworkConfigRequestHolder(NetworkConfig.RequestSettings networkConfigRequest) {
        this.networkConfigRequest = networkConfigRequest;
        this.pattern = Pattern.compile( networkConfigRequest.getRegex() );
    }

    public NetworkConfig.RequestSettings getNetworkConfigRequest() {
        return networkConfigRequest;
    }

    /**
     * Return true if this config works with the given link.
     *
     * @param link
     * @return
     */
    public boolean supports( String link ) {

        Matcher matcher = pattern.matcher( link );
        return matcher.find();

    }

}
