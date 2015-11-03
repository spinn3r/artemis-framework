package com.spinn3r.artemis.util.io;

import com.google.common.collect.Lists;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public class Closeables {

    public static void close( Closeable... closeables ) throws IOException {
        close( Lists.newArrayList( closeables ) );
    }

    public static void close( Collection< ? extends Closeable> closeables ) throws IOException {

        List<IOException> exceptions = Lists.newArrayList();

        for (Closeable closeable : closeables) {

            try {
                closeable.close();
            } catch( IOException e ) {
                exceptions.add( e );
            }

        }

        rethrowWhenNecessary( exceptions );

    }

    public static <T extends Exception> void rethrowWhenNecessary( List<T> exceptions ) throws T {

        if ( exceptions.size() == 0 ) {
            // we're done...
            return;
        }

        T root = exceptions.remove( 0 );

        for (T exception : exceptions) {
            root.addSuppressed( exception );
        }

        throw root;

    }

}
