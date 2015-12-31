package com.spinn3r.artemis.resource_finder;

import com.spinn3r.artemis.resource_finder.references.ResourceReference;
import com.spinn3r.artemis.util.text.CollectionFormatter;
import org.junit.Test;

import java.util.Collection;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ClasspathResourceFinderTest {

    @Test
    public void testGetResources() throws Exception {

        ClasspathResourceFinder classpathResourceFinder = new ClasspathResourceFinder();

        Collection<ResourceReference> resources = classpathResourceFinder.getResources( Pattern.compile( ".*/resource_finder/.*\\.txt" ) );

        System.out.printf( "%s\n", CollectionFormatter.table( resources ) );


    }

    @Test
    public void testGetResources2() throws Exception {

        ClasspathResourceFinder classpathResourceFinder = new ClasspathResourceFinder();

        Collection<ResourceReference> resources = classpathResourceFinder.getResources( Pattern.compile( ".*\\.txt" ) );

        System.out.printf( "%s\n", CollectionFormatter.table( resources ) );

    }

}