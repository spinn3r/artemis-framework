package com.spinn3r.artemis.util.misc;

import com.spinn3r.artemis.util.text.MapFormatter;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;
//import static org.hamcrest.Matchers.*;

public class HistographTest {

    @Test
    public void testSort() throws Exception {

        Histograph<String> histograph = new Histograph<>();

        histograph.incr( "alice" , 1 );
        histograph.incr( "alice" , 1 );
        histograph.incr( "alice" , 1 );
        histograph.incr( "alice" , 1 );

        histograph.incr( "bob" , 1 );
        histograph.incr( "bob" , 1 );

        assertEquals( "{alice=4, bob=2}", MapFormatter.toString( histograph.delegate ) );

        assertEquals( "{alice=4, bob=2}", histograph.read().toString() );

        assertEquals( "{alice=4, bob=2}", histograph.toString() );

        assertEquals( "alice   4   \n" +
                        "bob     2   \n",
                      histograph.format() );
    }

    @Test
    public void testSort2() throws Exception {

        Histograph<String> histograph = new Histograph<>();

        histograph.incr( "bob" );
        histograph.incr( "bob" );
        histograph.incr( "bob" );
        histograph.incr( "bob" );

        histograph.incr( "alice" );
        histograph.incr( "alice" );

        assertEquals( "{alice=2, bob=4}", MapFormatter.toString( histograph.delegate ) );

        assertEquals( "{alice=2, bob=4}", MapFormatter.toString( histograph.read() ) );

        assertEquals( "{bob=4, alice=2}", histograph.toString() );

        assertEquals( "bob     4   \n" +
                        "alice   2   \n",
                      histograph.format() );
    }

    @Test
    public void testEnum() throws Exception {

        Histograph<Person> histograph = new Histograph<>();

        histograph.incr( Person.ALICE , 1 );
        histograph.incr( Person.ALICE , 1 );
        histograph.incr( Person.ALICE , 1 );
        histograph.incr( Person.ALICE , 1 );

        histograph.incr( Person.BOB , 1 );
        histograph.incr( Person.BOB , 1 );

        histograph.incr( Person.CAROL , 1 );

        histograph.incr( Person.DAN , 1 );

        //assertEquals( "{CAROL=1, ALICE=4, BOB=2, DAN=1}", histograph.delegate.toString() );

        assertEquals( "{ALICE=4, BOB=2, CAROL=1, DAN=1}", histograph.read().toString() );

        assertEquals( "{ALICE=4, BOB=2, CAROL=1, DAN=1}", histograph.toString() );

        assertEquals( "ALICE   4   \n" +
                        "BOB     2   \n" +
                        "CAROL   1   \n" +
                        "DAN     1   \n", histograph.format() );

    }

    @Test
    public void testEmpty() throws Exception {

        Histograph<String> histograph = new Histograph<>();

        assertEquals( "{}", histograph.toString() );

    }

    @Test
    public void testComparable() throws Exception {

        assertTrue( Person.ALICE instanceof Comparable );

    }

    @Test
    public void testReadWithLimit() throws Exception {

        Histograph<Integer> histograph = new Histograph<>();

        for (int i = 0; i < 100; i++) {
            histograph.incr( i );
        }

        int limit = 10;

        Map<Integer,AtomicInteger> result = histograph.read( limit );

        assertEquals( limit, result.size() );

    }

    enum Person {
        ALICE, BOB, CAROL, DAN;
    }

}