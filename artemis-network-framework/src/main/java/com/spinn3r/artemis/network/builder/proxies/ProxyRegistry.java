package com.spinn3r.artemis.network.builder.proxies;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Creates a registry of multiple HTTP proxies that we can toggle over based on the given URL.
 *
 */
public class ProxyRegistry {

    private List<PrioritizedProxyReference> prioritizedProxyReferences = Lists.newCopyOnWriteArrayList();

    public ProxyRegistry(List<PrioritizedProxyReference> prioritizedProxyReferences) {
        Collections.sort( prioritizedProxyReferences, (o1, o2) -> o2.getPriority() - o1.getPriority() );
        this.prioritizedProxyReferences = prioritizedProxyReferences;
    }

    /**
     * Get the list of proxy references so that we can determine which proxy to
     * use.
     */
    public List<PrioritizedProxyReference> getPrioritizedProxyReferences() {
        return prioritizedProxyReferences;
    }

    /**
     * Find a ProxyReference that supports the given URL or null if no proxy is found.
     *
     */
    public PrioritizedProxyReference find( String link ) {

        for (PrioritizedProxyReference prioritizedProxyReference : prioritizedProxyReferences) {

            if ( prioritizedProxyReference.supports( link ) ) {
                return prioritizedProxyReference;
            }

        }

        return null;

    }

    @Override
    public String toString() {
        return "ProxyRegistry{" +
                 "prioritizedProxyReferences=" + prioritizedProxyReferences +
                 '}';
    }

}
