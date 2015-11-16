package com.spinn3r.artemis.network.builder.settings.requests;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.network.init.RequestSettings;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class RequestSettingsRegistry {

    private final ImmutableList<RequestSettingsReference> requestSettingsReferenceIndex;

    public RequestSettingsRegistry( Map<String, RequestSettings> requestSettingsMap ) {

        List<RequestSettingsReference> requestSettingsReferenceIndex = Lists.newArrayList();

        if ( requestSettingsMap == null ) {
            requestSettingsMap = Maps.newHashMap();
        }

        for (Map.Entry<String, RequestSettings> entry : requestSettingsMap.entrySet()) {
            RequestSettingsReference requestSettingsReference = new RequestSettingsReference( entry.getKey(), entry.getValue() );
            requestSettingsReferenceIndex.add( requestSettingsReference );
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
