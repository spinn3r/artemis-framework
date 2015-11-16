package com.spinn3r.artemis.network.builder.config;

import com.spinn3r.artemis.network.init.RequestSettings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wraps a request config so that we can cache regexp patterns, etc.
 */
public class NetworkConfigRequestHolder {

    private final RequestSettings networkConfigRequest;

    private final Pattern pattern;

    public NetworkConfigRequestHolder(RequestSettings networkConfigRequest) {
        this.networkConfigRequest = networkConfigRequest;
        this.pattern = Pattern.compile( networkConfigRequest.getRegex() );
    }

    public RequestSettings getNetworkConfigRequest() {
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
