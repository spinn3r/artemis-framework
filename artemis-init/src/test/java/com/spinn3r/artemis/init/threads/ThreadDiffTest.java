package com.spinn3r.artemis.init.threads;

import org.junit.Test;

import static org.junit.Assert.*;

public class ThreadDiffTest {

    @Test
    public void test1() throws Exception {

        ThreadSnapshot older = createThreadSnapshot( 1, 2 );

        ThreadSnapshot newer = createThreadSnapshot( 1, 4 );

        ThreadDiff threadDiff = ThreadSnapshots.diff( older, newer );

        assertEquals( "[]", threadDiff.getMissing().toString() );
        assertEquals( "[0000000000000000000000003 : thread-3, 0000000000000000000000004 : thread-4]", threadDiff.getAdditional().toString() );

        System.out.printf( "missing: %s\n", threadDiff.getMissing() );
        System.out.printf( "additional: %s\n", threadDiff.getAdditional() );

    }


    @Test
    public void test2() throws Exception {

        ThreadSnapshot older = createThreadSnapshot( 1, 2 );

        ThreadSnapshot newer = createThreadSnapshot( 1, 2 );

        ThreadDiff threadDiff = ThreadSnapshots.diff( older, newer );

        assertEquals( "[]", threadDiff.getMissing().toString() );
        assertEquals( "[]", threadDiff.getAdditional().toString() );

        System.out.printf( "missing: %s\n", threadDiff.getMissing() );
        System.out.printf( "additional: %s\n", threadDiff.getAdditional() );

    }

    @Test
    public void test3() throws Exception {

        ThreadSnapshot older = createThreadSnapshot( 1, 4 );

        ThreadSnapshot newer = createThreadSnapshot( 1, 2 );

        ThreadDiff threadDiff = ThreadSnapshots.diff( older, newer );

        assertEquals( "[0000000000000000000000003 : thread-3, 0000000000000000000000004 : thread-4]", threadDiff.getMissing().toString() );
        assertEquals( "[]", threadDiff.getAdditional().toString() );

        System.out.printf( "missing: %s\n", threadDiff.getMissing() );
        System.out.printf( "additional: %s\n", threadDiff.getAdditional() );

    }

    private static ThreadReference createThreadReference( long id ) {
        return new ThreadReference( id , "thread-" + id );
    }

    private static ThreadSnapshot createThreadSnapshot( long startInclusive, long endInclusive ) {

        ThreadSnapshot result = new ThreadSnapshot();

        for (long i = startInclusive; i <= endInclusive; i++) {
            result.add( createThreadReference( i ) );
        }

        return result;

    }

}