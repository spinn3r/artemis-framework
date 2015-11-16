package com.spinn3r.artemis.network.builder.config;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.network.init.NetworkConfig;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class NetworkConfigRequestRegistry {

    private final List<NetworkConfigRequestHolder> networkConfigRequestHolders = Lists.newArrayList();

    public NetworkConfigRequestRegistry(List<NetworkConfig.RequestSettings> networkConfigRequests) {

        if ( networkConfigRequests == null )
            return;

        for (NetworkConfig.RequestSettings networkConfigRequest : networkConfigRequests) {
            this.networkConfigRequestHolders.add( new NetworkConfigRequestHolder( networkConfigRequest ) );
        }

        Collections.sort( this.networkConfigRequestHolders, (o1, o2) -> o2.getNetworkConfigRequest().getPriority() - o1.getNetworkConfigRequest().getPriority() );

    }

    /**
     * Find a ProxyReference that supports the given URL or null if no proxy
     * is found.
     *
     * @param link
     * @return
     */
    public NetworkConfig.RequestSettings find( String link ) {

        for (NetworkConfigRequestHolder networkConfigRequestHolder : networkConfigRequestHolders) {

            if ( networkConfigRequestHolder.supports( link ) ) {
                return networkConfigRequestHolder.getNetworkConfigRequest();
            }

        }

        return null;

    }


}
