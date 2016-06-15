package com.spinn3r.artemis.tracepoints;

/**
 *
 */
public class ThreadInfo {

    private final long id;

    private final String name;

    private final int priority;

    public ThreadInfo(long id, String name, int priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                 "id=" + id +
                 ", name='" + name + '\'' +
                 ", priority=" + priority +
                 '}';
    }

}
