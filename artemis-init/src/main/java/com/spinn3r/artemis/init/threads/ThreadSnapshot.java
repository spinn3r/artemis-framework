package com.spinn3r.artemis.init.threads;

import java.util.TreeSet;

/**
 *
 */
@SuppressWarnings( "serial" )
public class ThreadSnapshot extends TreeSet<ThreadReference> {

    public ThreadSnapshot copy() {

        ThreadSnapshot copy = new ThreadSnapshot();

        for (ThreadReference threadReference : this) {
            copy.add( threadReference );
        }

        return copy;

    }

    public String format() {

        StringBuilder buff = new StringBuilder();

        for (ThreadReference threadReference : this) {
            buff.append( "    " + threadReference.toString() + "\n" );
        }

        return buff.toString();

    }

}
