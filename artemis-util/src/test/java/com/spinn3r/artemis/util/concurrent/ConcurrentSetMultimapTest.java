package com.spinn3r.artemis.util.concurrent;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class ConcurrentSetMultimapTest {

    @Test
    public void testGetAndPut() throws Exception {

        ConcurrentSetMultimap<String,Integer> sut = new ConcurrentSetMultimap<>();

        assertNotNull( sut.get( "foo" ) );
        assertThat( sut.get( "foo" ).size(), equalTo( 0 ) );

        assertTrue( sut.put( "foo", 1 ) );

        assertThat( sut.get( "foo" ).size(), equalTo( 1 ) );

        assertTrue( sut.put( "foo", 2 ) );

        assertThat( sut.get( "foo" ).size(), equalTo( 2 ) );

        assertFalse( sut.put( "foo", 2 ) );

        sut.clear();

        assertThat( sut.get( "foo" ).size(), equalTo( 0 ) );

    }

}