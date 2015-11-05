package com.spinn3r.artemis.init.threads;

import com.spinn3r.artemis.init.tracer.Tracer;

/**
 * The difference between two thread snapshots.
 */
public class ThreadDiff {

    private ThreadSnapshot missing;

    private ThreadSnapshot additional;

    public ThreadDiff(ThreadSnapshot missing, ThreadSnapshot additional) {
        this.missing = missing;
        this.additional = additional;
    }

    public ThreadSnapshot getMissing() {
        return missing;
    }

    public ThreadSnapshot getAdditional() {
        return additional;
    }

    public void report( Tracer tracer ) {

        if ( missing.size() > 0 ) {
            tracer.warn( "Found %,d missing threads:\n%s", missing.size(), missing.format() );
        }

        if ( additional.size() > 0 ) {
            tracer.warn( "Found %,d additional threads:\n%s", additional.size(), additional.format() );
        }

    }

    @Override
    public String toString() {
        return "ThreadDiff{" +
                 "missing=" + missing +
                 ", additional=" + additional +
                 '}';
    }

}
