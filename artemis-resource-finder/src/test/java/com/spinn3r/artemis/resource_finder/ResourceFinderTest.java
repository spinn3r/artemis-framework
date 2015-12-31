package com.spinn3r.artemis.resource_finder;

import com.google.common.collect.ImmutableList;
import com.spinn3r.artemis.util.text.CollectionFormatter;
import org.junit.Ignore;
import org.junit.Test;

import java.net.URL;
import java.util.Set;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ResourceFinderTest {

    @Test
    @Ignore
    public void testFindResources() throws Exception {

        ResourceFinder resourceFinder = new ResourceFinder();

        ImmutableList<URL> resources = resourceFinder.findResources( getClass().getPackage().getName(), "*.txt" );

        assertEquals( 1, resources.size() );

        System.out.printf( "%s", CollectionFormatter.table( resources ) );

    }

    @Test
    public void testGetResourceListing() throws Exception {

        ResourceFinder resourceFinder = new ResourceFinder();
        Set<String> resourceListing = resourceFinder.getResourceListing( ResourceFinderTest.class, "com/spinn3r/artemis/resource_finder" );

        System.out.printf( "%s\n", resourceListing );

        assertEquals( 3, resourceListing.size() );
        assertEquals( "[com/spinn3r/artemis/resource_finder/fakedir, com/spinn3r/artemis/resource_finder/first.txt, com/spinn3r/artemis/resource_finder/ResourceFinderTest.class]",
                      resourceListing.toString() );

    }

}
