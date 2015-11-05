package com.spinn3r.artemis.metrics.jvm.threads;

/**
 * Metadata about a thread including the group and state.  This does NOT use
 * a safepoint since we don't need the stack trace info.
 */
public class ThreadMeta {

    private Thread.State state;

    private String group;

    private boolean daemon;

    private int priority;

    public ThreadMeta(Thread.State state, String group, boolean daemon, int priority) {
        this.state = state;
        this.group = group;
        this.daemon = daemon;
        this.priority = priority;
    }

    public Thread.State getState() {
        return state;
    }

    public String getGroup() {
        return group;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public int getPriority() {
        return priority;
    }

    public String toKey() {
        return String.format( "state=%s,group=%s,daemon=%s", state, group, priority );
    }

    @Override
    public String toString() {
        return "ThreadMeta{" +
                 "state=" + state +
                 ", group='" + group + '\'' +
                 ", daemon=" + daemon +
                 ", priority=" + priority +
                 '}';
    }

}
