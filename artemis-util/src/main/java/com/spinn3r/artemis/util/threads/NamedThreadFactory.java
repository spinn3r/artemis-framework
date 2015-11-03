package com.spinn3r.artemis.util.threads;

/**
 *
 */

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple named thread factory.
 */
@SuppressWarnings("NullableProblems")
public class NamedThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    private final int priority;

    public NamedThreadFactory(Class<?> clazz) {
        this( clazz.getName() );
    }

    public NamedThreadFactory(String name) {
        this( name, Thread.NORM_PRIORITY );
    }
    public NamedThreadFactory(String name, int priority ) {
        final SecurityManager s = System.getSecurityManager();
        this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = name + "-thread-";
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {

        final Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);

        if (t.getPriority() != priority) {
            t.setPriority(priority);
        }

        return t;

    }

}