package com.spinn3r.artemis.util.misc;

import com.google.common.collect.Lists;
import org.hamcrest.Matcher;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
//import static org.hamcrest.Matchers.*;
import static com.spinn3r.artemis.util.misc.CollectionUtils.*;
import static org.hamcrest.Matchers.*;

public class CollectionUtilsTest {

    @Test
    public void testGroup() throws Exception {

        assertEquals( "[]",
                      group( Lists.newArrayList(), 2, true ).toString() );

        assertEquals( "[]",
                      group( Lists.newArrayList( 1 ), 2, false ).toString() );

        assertEquals( "[[1, 2], [3, 4], [5]]",
                      group( Lists.newArrayList( 1, 2, 3, 4, 5 ), 2, true ).toString() );

        assertEquals( "[[1, 2], [3, 4]]",
                      group( Lists.newArrayList( 1, 2, 3, 4, 5 ), 2, false ).toString() );

    }

    @Test
    public void testFilter() throws Exception {

        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean evaluate(String value) {
                return ! value.trim().equals( "" ) && ! value.startsWith( "#" );
            }
        };

        // *** test1

        List<String> test =  Lists.newArrayList( "" );

        filter( test, predicate );

        assertEquals( "[]", test.toString() );

        // *** test2

        test =  Lists.newArrayList( "Hello", "# Really" );

        filter( test, predicate );

        assertEquals( "[Hello]", test.toString() );

    }

    @Test
    public void testFilter2() throws Exception {

        Matcher<String> matcher = not( startsWith( "#" ) );
        List<String> test;

        // *** test1
        test =  Lists.newArrayList( "" );
        filter( test, matcher );
        assertEquals( "[]", test.toString() );

        // *** test2
        test =  Lists.newArrayList( "Hello", "# Really" );
        filter( test, matcher );
        assertEquals( "[Hello]", test.toString() );

    }

    @Test
    public void testHead() throws Exception {

        List<Integer> list;

        list = Lists.newArrayList();
        assertEquals( "[]", head( list, 2 ).toString() );

        list = Lists.newArrayList(1);
        assertEquals( "[1]", head( list, 2 ).toString() );

        list = Lists.newArrayList(1,2);
        assertEquals( "[1, 2]", head( list, 2 ).toString() );

        list = Lists.newArrayList(1,2,3);
        assertEquals( "[1, 2]", head( list, 2 ).toString() );

    }

    @Test
    public void testTail() throws Exception {

        List<Integer> list;

        list = Lists.newArrayList();
        assertEquals( "[]", tail( list, 2 ).toString() );

        list = Lists.newArrayList(1);
        assertEquals( "[1]", tail( list, 2 ).toString() );

        list = Lists.newArrayList(1,2);
        assertEquals( "[1, 2]", tail( list, 2 ).toString() );

        list = Lists.newArrayList(1,2,3);
        assertEquals( "[2, 3]", tail( list, 2 ).toString() );

        list = Lists.newArrayList(1,2,3,4);
        assertEquals( "[3, 4]", tail( list, 2 ).toString() );

    }

    @Test
    public void testFirst() throws Exception {

        List<Integer> list;

        list = Lists.newArrayList();
        assertEquals( null, first( list ) );

        list = Lists.newArrayList( 1 );
        assertEquals( new Integer( 1 ), first( list ) );

        list = Lists.newArrayList( 1, 2 );
        assertEquals( new Integer( 1 ), first( list ) );
    }

    @Test
    public void testlast() throws Exception {

        List<Integer> list;

        list = Lists.newArrayList();
        assertEquals( null, last( list ) );

        list = Lists.newArrayList( 1 );
        assertEquals( new Integer( 1 ), last( list ) );

        list = Lists.newArrayList( 1, 2 );
        assertEquals( new Integer( 2 ), last( list ) );
    }

}