package com.spinn3r.artemis.resource_finder;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class TestResourceFinderFromDependencyModule {

    @Test
    public void testGetResourceListing() throws Exception {

        ResourceFinder resourceFinder = new ResourceFinder();
        Set<String> resourceListing = resourceFinder.getResourceListing( ResourceFinder.class, "com/spinn3r/artemis/resource_finder" );

        System.out.printf( "%s\n", resourceListing );

        assertEquals( 3, resourceListing.size() );
        assertEquals( "[com/spinn3r/artemis/resource_finder/fakedir, com/spinn3r/artemis/resource_finder/first.txt, com/spinn3r/artemis/resource_finder/ResourceFinderTest.class]",
                      resourceListing.toString() );

    }

}
