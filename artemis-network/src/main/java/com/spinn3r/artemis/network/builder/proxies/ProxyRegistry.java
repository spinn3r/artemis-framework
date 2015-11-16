package com.spinn3r.artemis.network.builder.proxies;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Creates a registry of multiple HTTP proxies that we can toggle over based on the given URL.
 *
 */
public class ProxyRegistry {

    private List<ProxyReference> proxyReferences = Lists.newCopyOnWriteArrayList();

    public ProxyRegistry(List<ProxyReference> proxyReferences) {
        Collections.sort( proxyReferences, (o1,o2) -> o2.getPriority() - o1.getPriority() );
        this.proxyReferences = proxyReferences;
    }

    /**
     * Get the list of proxy references so that we can determine which proxy to
     * use.
     */
    public List<ProxyReference> getProxyReferences() {
        return proxyReferences;
    }

    /**
     * Find a ProxyReference that supports the given URL or null if no proxy is found.
     *
     */
    public ProxyReference find( String link ) {

        for (ProxyReference proxyReference : proxyReferences) {

            if ( proxyReference.supports( link ) ) {
                return proxyReference;
            }

        }

        return null;

    }

    @Override
    public String toString() {
        return "ProxyRegistry{" +
                 "proxyReferences=" + proxyReferences +
                 '}';
    }

}
