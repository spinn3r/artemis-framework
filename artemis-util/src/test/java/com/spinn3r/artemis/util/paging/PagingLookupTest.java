package com.spinn3r.artemis.util.paging;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(Theories.class)
public class PagingLookupTest {

    @DataPoint public static final int PAGE_SIZE_1 = 1;
    @DataPoint public static final int PAGE_SIZE_2 = 2;
    @DataPoint public static final int PAGE_SIZE_3 = 3;
    @DataPoint public static final int PAGE_SIZE_4 = 4;
    @DataPoint public static final int PAGE_SIZE_5 = 5;
    @DataPoint public static final int PAGE_SIZE_6 = 6;
    @DataPoint public static final int PAGE_SIZE_7 = 7;
    @DataPoint public static final int PAGE_SIZE_8 = 8;
    @DataPoint public static final int PAGE_SIZE_9 = 9;

    @Theory
    public void testLookupWithNoHits( int pageSize ) throws Exception {

        Map<String,String> backing = Maps.newHashMap();

        PagingLookup<String, String> pagingLookup = constructFromBacking(pageSize, backing );
        Iterator<KeyValue<String,String>> iterator = pagingLookup.lookup( Lists.newArrayList( "foo", "bar" ) );

        assertFalse( iterator.hasNext() );

    }

    @Theory
    public <K> void testLookupWithOneHit( int pageSize ) throws Exception {

        Map<String,String> backing = Maps.newHashMap();
        backing.put( "foo", "foo" );

        PagingLookup<String, String> pagingLookup = constructFromBacking( pageSize, backing );
        Iterator<KeyValue<String,String>> iterator = pagingLookup.lookup( Lists.newArrayList( "foo", "bar" ) );

        assertTrue( iterator.hasNext() );
        assertEquals( "KeyValue{key=foo, value=foo}", iterator.next().toString() );
        assertFalse( iterator.hasNext() );

    }

    @Theory
    public <K> void testLookupWithTwoHits( int pageSize ) throws Exception {

        Map<String,String> backing = Maps.newHashMap();
        backing.put( "foo", "foo" );
        backing.put( "bar", "bar" );

        PagingLookup<String, String> pagingLookup = constructFromBacking( pageSize, backing );
        Iterator<KeyValue<String,String>> iterator = pagingLookup.lookup( Lists.newArrayList( "foo", "bar" ) );

        assertTrue( iterator.hasNext() );
        assertEquals( "KeyValue{key=foo, value=foo}", iterator.next().toString() );
        assertTrue( iterator.hasNext() );
        assertEquals( "KeyValue{key=bar, value=bar}", iterator.next().toString() );

    }

    @Theory
    public <K> void testLookupWithTwoHitsButMissingInnerItems( int pageSize ) throws Exception {

        Map<String,String> backing = Maps.newHashMap();
        backing.put( "foo", "foo" );
        backing.put( "bar", "bar" );

        PagingLookup<String, String> pagingLookup = constructFromBacking( pageSize, backing );
        Iterator<KeyValue<String,String>> iterator = pagingLookup.lookup( Lists.newArrayList( "foo", "cat", "dog", "bunny", "rabbit", "bar" ) );

        assertTrue( iterator.hasNext() );
        assertEquals( "KeyValue{key=foo, value=foo}", iterator.next().toString() );
        assertTrue( iterator.hasNext() );
        assertEquals( "KeyValue{key=bar, value=bar}", iterator.next().toString() );

    }

    private PagingLookup<String, String> constructFromBacking( int pageSize, Map<String, String> backing ) {

        System.out.printf( "Creating paging lookup with page size: %s\n", pageSize );

        return new PagingLookup<>( pageSize, (keys) -> {

                List<KeyValue<String,String>> result = Lists.newArrayList();

                for (String key : keys) {

                    String value = backing.get( key );

                    if ( value == null )
                        continue;

                    result.add( new KeyValue<>( key, value ) );

                }

                return result;

            } );

    }
}