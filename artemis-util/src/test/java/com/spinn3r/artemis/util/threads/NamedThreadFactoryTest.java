package com.spinn3r.artemis.util.threads;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class NamedThreadFactoryTest {

    @Test
    public void testNewThread() throws Exception {

        String threadName = "my-named-thread";
        NamedThreadFactory namedThreadFactory = new NamedThreadFactory( threadName, Thread.MIN_PRIORITY );

        ExecutorService executorService = Executors.newFixedThreadPool( 1, namedThreadFactory );

        final CountDownLatch countDownLatch = new CountDownLatch( 1 );

        final AtomicReference<String> actualThreadName = new AtomicReference<>( null );
        final AtomicInteger actualThreadPriority = new AtomicInteger( Integer.MIN_VALUE );

        executorService.submit( new Runnable() {
            @Override
            public void run() {

                actualThreadName.set( Thread.currentThread().getName() );
                actualThreadPriority.set( Thread.currentThread().getPriority() );

                countDownLatch.countDown();


            }

        } );

        countDownLatch.await();;
        assertEquals( Thread.MIN_PRIORITY, actualThreadPriority.get() );
        assertEquals( "my-named-thread-thread-1", actualThreadName.get() );

    }

}