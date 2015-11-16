package com.spinn3r.artemis.network.builder.settings.requests;

import com.google.common.collect.ImmutableList;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class SettingsRegistry {

    private final ImmutableList<SettingsReference> settingsReferenceIndex;

    public SettingsRegistry(List<SettingsReference> settingsReferenceIndex) {
        Collections.sort( settingsReferenceIndex, (o1, o2) -> o2.getPriority() - o1.getPriority() );
        this.settingsReferenceIndex = ImmutableList.copyOf( settingsReferenceIndex );
    }

    public ImmutableList<SettingsReference> getSettingsReferenceIndex() {
        return settingsReferenceIndex;
    }

    /**
     * Find a ProxyReference that supports the given URL or null if no proxy is found.
     *
     */
    public SettingsReference find( String link ) {

        for (SettingsReference settingsReference : settingsReferenceIndex) {

            if ( settingsReference.supports( link ) ) {
                return settingsReference;
            }

        }

        return null;

    }

    @Override
    public String toString() {
        return "SettingsRegistry{" +
                 "settingsIndex=" + settingsReferenceIndex +
                 '}';
    }

}
