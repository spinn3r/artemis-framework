package com.spinn3r.artemis.util.misc;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class CyclicalConcurrentIteratorTest {

    @Test
    public void test1() throws Exception {

        List<String> list = Lists.newArrayList( "1" );

        CyclicalConcurrentIterator<String> iterator = new CyclicalConcurrentIterator<>( list );

        assertEquals( 0, iterator.computeIndex() );

        assertEquals( 1, iterator.size() );

        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );

        // now test overflow...

        iterator.position.set( Integer.MAX_VALUE );
        assertEquals( iterator.position.get(), Integer.MAX_VALUE );

        assertEquals( 0, iterator.computeIndex() );

        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );

    }

    @Test
    public void test3() throws Exception {

        List<String> list = Lists.newArrayList( "1", "2", "3" );

        CyclicalConcurrentIterator<String> iterator = new CyclicalConcurrentIterator<>( list );

        /*
        System.out.printf( "%s\n", iterator.computeIndex() );
        assertEquals( "1", iterator.next() );
        System.out.printf( "%s\n", iterator.computeIndex() );
        assertEquals( "2", iterator.next() );
        System.out.printf( "%s\n", iterator.computeIndex() );
        assertEquals( "3", iterator.next() );
        */

        assertEquals( "1", iterator.next() );
        assertEquals( "2", iterator.next() );
        assertEquals( "3", iterator.next() );

        assertEquals( "1", iterator.next() );
        assertEquals( "2", iterator.next() );
        assertEquals( "3", iterator.next() );

        iterator.position.set( Integer.MAX_VALUE );

        assertEquals( "1", iterator.next() );
        assertEquals( "1", iterator.next() );
        assertEquals( "2", iterator.next() );


    }

    @Test
    public void testOverflow() throws Exception {

        // test atomic int overflow.

        AtomicInteger value = new AtomicInteger( Integer.MAX_VALUE );
        assertEquals( value.get(), Integer.MAX_VALUE );

        assertEquals( 0, value.incrementAndGet() + Integer.MAX_VALUE + 1 );

    }

}