package com.spinn3r.artemis.network.builder.settings.requests;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class RequestSettingsRegistry {

    private final ImmutableList<RequestSettingsReference> requestSettingsReferenceIndex;

    public RequestSettingsRegistry(List<RequestSettingsReference> requestSettingsReferenceIndex) {

        if ( requestSettingsReferenceIndex == null ) {
            requestSettingsReferenceIndex = Lists.newArrayList();
        }

        Collections.sort( requestSettingsReferenceIndex, (o1, o2) -> o2.getPriority() - o1.getPriority() );
        this.requestSettingsReferenceIndex = ImmutableList.copyOf( requestSettingsReferenceIndex );
    }

    public ImmutableList<RequestSettingsReference> getRequestSettingsReferenceIndex() {
        return requestSettingsReferenceIndex;
    }

    /**
     * Find a ProxyReference that supports the given URL or null if no proxy is found.
     *
     */
    public RequestSettingsReference find( String link ) {

        for (RequestSettingsReference requestSettingsReference : requestSettingsReferenceIndex) {

            if ( requestSettingsReference.supports( link ) ) {
                return requestSettingsReference;
            }

        }

        return null;

    }

    @Override
    public String toString() {
        return "SettingsRegistry{" +
                 "settingsIndex=" + requestSettingsReferenceIndex +
                 '}';
    }

}
