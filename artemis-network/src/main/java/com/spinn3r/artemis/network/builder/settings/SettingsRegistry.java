package com.spinn3r.artemis.network.builder.settings;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.spinn3r.artemis.network.builder.proxies.ProxyReference;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class SettingsRegistry {

    private final ImmutableList<Settings> settingsIndex;

    public SettingsRegistry(List<Settings> settingsIndex) {
        Collections.sort( settingsIndex, (o1, o2) -> o2.getPriority() - o1.getPriority() );
        this.settingsIndex = ImmutableList.copyOf( settingsIndex );
    }

    public ImmutableList<Settings> getSettingsIndex() {
        return settingsIndex;
    }

    /**
     * Find a ProxyReference that supports the given URL or null if no proxy is found.
     *
     */
    public Settings find( String link ) {

        for (Settings settings : settingsIndex) {

            if ( settings.supports( link ) ) {
                return settings;
            }

        }

        return null;

    }

    @Override
    public String toString() {
        return "SettingsRegistry{" +
                 "settingsIndex=" + settingsIndex +
                 '}';
    }

}
