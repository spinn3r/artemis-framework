package com.spinn3r.artemis.test;

import com.google.common.base.Stopwatch;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 */
@Ignore
public class TestClockPerformance {

    private static final int COUNT = 10_000_000;

    private static long valueAsLong = 1;

    private static AtomicLong valueAsAtomicLong = new AtomicLong( 1 );

    @Test
    public void test1() throws Exception {

        Stopwatch stopwatch = Stopwatch.createStarted();

        long value = 0;

        for (int i = 0; i < COUNT; i++) {
            value += valueAsLong;
        }

        System.out.printf( "test1: %s (%s)\n", stopwatch.stop(), value );

    }

    @Test
    public void test2() throws Exception {

        Stopwatch stopwatch = Stopwatch.createStarted();

        long value = 0;

        for (int i = 0; i < COUNT; i++) {
            value += System.nanoTime();
        }

        System.out.printf( "test2: %s (%s)\n", stopwatch.stop(), value );

    }

    @Test
    public void test3() throws Exception {

        Stopwatch stopwatch = Stopwatch.createStarted();

        long val=0;

        for (int i = 0; i < COUNT; i++) {
            val += valueAsAtomicLong.get();
        }

        Thread.interrupted();

        System.out.printf( "test3: %s (%s)\n", stopwatch.stop(), val );

    }

}
