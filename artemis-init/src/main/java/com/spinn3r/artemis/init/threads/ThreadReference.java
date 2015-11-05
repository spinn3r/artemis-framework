package com.spinn3r.artemis.init.threads;

/**
 *
 */
public class ThreadReference implements Comparable<ThreadReference> {

    private final long identifier;

    private final String name;

    public ThreadReference(long identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public long getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof ThreadReference )) return false;

        ThreadReference that = (ThreadReference) o;

        if (identifier != that.identifier) return false;
        if (!name.equals( that.name )) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) ( identifier ^ ( identifier >>> 32 ) );
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public int compareTo(ThreadReference o) {
        return toString().compareTo( o.toString() );
    }

    @Override
    public String toString() {
        return String.format( "%025d : %s", identifier, name );
    }

}
