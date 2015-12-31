package com.spinn3r.artemis.resource_finder;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import com.spinn3r.artemis.resource_finder.references.ResourceReference;
import com.spinn3r.artemis.util.text.CollectionFormatter;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
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
    @Ignore
    public void testGetResourceListing() throws Exception {

        ResourceFinder resourceFinder = new ResourceFinder();
        Set<String> resourceListing = resourceFinder.getResourceListing( ResourceFinder.class, "com/spinn3r/artemis/resource_finder" );

        System.out.printf( "%s\n", resourceListing );

        assertEquals( 3, resourceListing.size() );
        assertEquals( "[com/spinn3r/artemis/resource_finder/fakedir, com/spinn3r/artemis/resource_finder/first.txt, com/spinn3r/artemis/resource_finder/ResourceFinderTest.class]",
                      resourceListing.toString() );

    }

    @Test
    public void testGetClasspathResourceSpecific() throws Exception {

        System.out.printf( "%s\n", System.getProperty( "java.class.path" ) );

        ClasspathResourceFinder classpathResourceFinder = new ClasspathResourceFinder();

        Collection<ResourceReference> resources = classpathResourceFinder.getResources( Pattern.compile( "testing/.*\\.txt" ) );
        ImmutableList<String> paths = ClasspathResources.toPaths( resources );

        System.out.printf( "%s\n", CollectionFormatter.table( resources ) );

        assertThat( paths, hasItem( "testing/first.txt" ) );
        assertThat( paths, hasItem( "testing/fakedir/second.txt" ) );
        assertThat( paths, hasItem( "testing/fakedir/nested/third.txt" ) );

        // test that we can open them all...
        for (ResourceReference resource : resources) {
            try( InputStream inputStream = resource.getInputStream() ) {
                ByteStreams.toByteArray( inputStream );
            }
        }

    }

    @Test
    public void testGetClasspathResource() throws Exception {

        System.out.printf( "%s\n", System.getProperty( "java.class.path" ) );

        ClasspathResourceFinder classpathResourceFinder = new ClasspathResourceFinder();

        Collection<ResourceReference> resources = classpathResourceFinder.getResources( Pattern.compile( ".*\\.txt" ) );
        ImmutableList<String> paths = ClasspathResources.toPaths( resources );

        System.out.printf( "%s\n", CollectionFormatter.table( resources ) );

        assertThat( paths, hasItem( "testing/first.txt" ) );
        assertThat( paths, hasItem( "testing/fakedir/second.txt" ) );
        assertThat( paths, hasItem( "testing/fakedir/nested/third.txt" ) );

        // test that we can open them all...
        for (ResourceReference resource : resources) {
            try( InputStream inputStream = resource.getInputStream() ) {
                ByteStreams.toByteArray( inputStream );
            }
        }

    }

}
