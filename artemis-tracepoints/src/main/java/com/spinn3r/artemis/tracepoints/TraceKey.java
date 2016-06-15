package com.spinn3r.artemis.tracepoints;

/**
 *
 */
public class TraceKey {

    private final long threadId;

    private final String name;

    public TraceKey(long threadId, String name) {
        this.threadId = threadId;
        this.name = name;
    }

    public long getThreadId() {
        return threadId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TraceKey)) return false;

        TraceKey traceKey = (TraceKey) o;

        if (threadId != traceKey.threadId) return false;
        return name.equals(traceKey.name);

    }

    @Override
    public int hashCode() {
        int result = (int) (threadId ^ (threadId >>> 32));
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "TraceKey{" +
                 "threadId=" + threadId +
                 ", name='" + name + '\'' +
                 '}';
    }
}
