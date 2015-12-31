package com.spinn3r.artemis.resource_finder;

import com.google.common.collect.ImmutableList;
import com.spinn3r.artemis.util.text.CollectionFormatter;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ResourceFinderTest {

    @Test
    public void testFindResources() throws Exception {

        ResourceFinder resourceFinder = new ResourceFinder();

        ImmutableList<URL> resources = resourceFinder.findResources( "*.txt" );

        System.out.printf( "%s", CollectionFormatter.table( resources ) );

    }

}