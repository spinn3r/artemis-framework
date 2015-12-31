package com.spinn3r.artemis.resource_finder;

import com.google.common.collect.ImmutableList;
import com.spinn3r.artemis.resource_finder.references.ResourceReference;
import com.spinn3r.artemis.util.text.CollectionFormatter;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

import static org.hamcrest.Matchers.*;

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

    @Test
    public void testGetClasspathResources() throws Exception {

        System.out.printf( "%s\n", System.getProperty( "java.class.path" ) );

        ClasspathResourceFinder classpathResourceFinder = new ClasspathResourceFinder();

        Collection<ResourceReference> resources = classpathResourceFinder.getResources( Pattern.compile( "testing/.*\\.txt" ) );
        ImmutableList<String> paths = ClasspathResources.toPaths( resources );

        System.out.printf( "%s\n", CollectionFormatter.table( resources ) );

        assertThat( paths, hasItem( "testing/first.txt" ) );
        assertThat( paths, hasItem( "testing/fakedir/second.txt" ) );
        assertThat( paths, hasItem( "testing/fakedir/nested/third.txt" ) );

    }

}
