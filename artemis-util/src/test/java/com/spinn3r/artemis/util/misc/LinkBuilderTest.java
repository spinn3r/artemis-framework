package com.spinn3r.artemis.util.misc;

import com.spinn3r.artemis.network.links.LinkBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class LinkBuilderTest {

    @Test
    public void test1() throws Exception {

        LinkBuilder builder;

        builder = new LinkBuilder( "cnn.com", "/foo/bar" );
        assertEquals( "http://cnn.com/foo/bar", builder.toString() );

        builder = new LinkBuilder( "cnn.com", "/foo/bar" );
        builder.put( "cat", "dog" );

        assertEquals( "http://cnn.com/foo/bar?cat=dog", builder.toString() );

        builder = new LinkBuilder( "cnn.com", "/foo/bar" );
        builder.put( "cat", "dog" );
        builder.put( "mazda", "toyota" );

        assertEquals( "http://cnn.com/foo/bar?cat=dog&mazda=toyota", builder.toString() );

    }

}