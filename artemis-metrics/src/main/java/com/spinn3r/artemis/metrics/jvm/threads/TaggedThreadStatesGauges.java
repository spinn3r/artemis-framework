package com.spinn3r.artemis.metrics.jvm.threads;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.jvm.ThreadDeadlockDetector;
import com.google.common.collect.Lists;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * Similar to the ThreadStateGaugeSet used in the main metrics but we support
 * tags on the threads
 *
 */
public class TaggedThreadStatesGauges {

    public void getMetrics() {

        // TODO:
        //
        // - keep the metrics around for the ENTIRE JVM lifecyclce...
        //
        // - have the metrics read from an in memory index (I guess this is why it's backed)
        //

//        final Map<String, Metric> gauges = new HashMap<String, Metric>();
//
//        for (final Thread.State state : Thread.State.values()) {
//            gauges.put(name(state.toString().toLowerCase(), "count"),
//                        new Gauge<Object>() {
//                            @Override
//                            public Object getValue() {
//                                return getThreadCount(state);
//                            }
//                        });
//        }

    }
//
//    private int getThreadCount(Thread.State state) {
//
////        final ThreadInfo[] allThreads = threads.getThreadInfo(threads.getAllThreadIds());
////
////        int count = 0;
////        for (ThreadInfo info : allThreads) {
////            if (info != null && info.getThreadState() == state) {
////                count++;
////            }
////        }
////
////        return count;
//
//    }

    private static List<Thread> getAllThreads() {

        int activeCount = Thread.activeCount();
        Thread[] threads = new Thread[activeCount];

        Thread.enumerate( threads );

        return Arrays.asList( threads );

    }

    private static List<ThreadMeta> getThreadMetas( List<Thread> threads ) {

        List<ThreadMeta> threadMetas = Lists.newArrayList();

        for (Thread thread : threads) {

            ThreadMeta threadMeta = new ThreadMeta( thread.getState(),
                                                    thread.getThreadGroup().getName(),
                                                    thread.isDaemon(),
                                                    thread.getPriority() );

            threadMetas.add( threadMeta );

        }

        return threadMetas;

    }

}
