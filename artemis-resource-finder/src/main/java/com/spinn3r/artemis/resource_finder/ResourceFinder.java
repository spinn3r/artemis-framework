package com.spinn3r.artemis.resource_finder;

import com.google.common.collect.ImmutableList;
import net.sf.corn.cps.CPScanner;
import net.sf.corn.cps.ResourceFilter;

import java.net.URL;
import java.util.List;

/**
 * Allows us to find resources via a given filename pattern.  For example we
 * could search for *.json.
 *
 * We can then filter the resources by path.  This is the best way we've found
 * to do this so far and while expensive is currently reliable.
 */
public class ResourceFinder {

    public ImmutableList<URL> findResources( String pattern ) {

        List<URL> resources =
          CPScanner.scanResources( new ResourceFilter()
                                     .resourceName( pattern ) );

        return ImmutableList.copyOf( resources );

    }

}
