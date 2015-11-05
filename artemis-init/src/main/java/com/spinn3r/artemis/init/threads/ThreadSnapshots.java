package com.spinn3r.artemis.init.threads;

/**
 *
 */
public class ThreadSnapshots {

    public static ThreadSnapshot create() {

        ThreadSnapshot snapshot = new ThreadSnapshot();

        for ( Thread thread : Thread.getAllStackTraces().keySet() ) {

            snapshot.add( new ThreadReference( thread.getId(), thread.getName() ) );

        }

        return snapshot;

    }

    public static ThreadDiff diff( ThreadSnapshot older, ThreadSnapshot newer ) {

        ThreadSnapshot missing = older.copy();
        missing.removeAll( newer );

        ThreadSnapshot additional = newer.copy();
        additional.removeAll( older );

        return new ThreadDiff( missing, additional );

    }

}
