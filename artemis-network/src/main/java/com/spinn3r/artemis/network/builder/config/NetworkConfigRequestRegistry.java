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

    public NetworkConfigRequestRegistry(List<NetworkConfig.Request> networkConfigRequests) {

        if ( networkConfigRequests == null )
            return;

        for (NetworkConfig.Request networkConfigRequest : networkConfigRequests) {
            this.networkConfigRequestHolders.add( new NetworkConfigRequestHolder( networkConfigRequest ) );
        }

        Collections.sort( this.networkConfigRequestHolders, (o1, o2) -> {
            return o2.getNetworkConfigRequest().getPriority() - o1.getNetworkConfigRequest().getPriority();
        } );

    }

    /**
     * Find a ProxyReference that supports the given URL or null if no proxy
     * is found.
     *
     * @param link
     * @return
     */
    public NetworkConfig.Request find( String link ) {

        for (NetworkConfigRequestHolder networkConfigRequestHolder : networkConfigRequestHolders) {

            if ( networkConfigRequestHolder.supports( link ) ) {
                return networkConfigRequestHolder.getNetworkConfigRequest();
            }

        }

        return null;

    }


}
