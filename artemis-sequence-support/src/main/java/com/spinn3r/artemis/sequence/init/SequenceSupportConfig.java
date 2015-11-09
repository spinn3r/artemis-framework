package com.spinn3r.artemis.sequence.init;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SequenceSupportConfig {

    private SequenceSupportProvider provider = SequenceSupportProvider.NONE;

    public SequenceSupportProvider getProvider() {
        return provider;
    }

    @Override
    public String toString() {
        return "SequenceSupportConfig{" +
                 "provider=" + provider +
                 '}';
    }
}
