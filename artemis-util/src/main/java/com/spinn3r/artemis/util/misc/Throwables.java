package com.spinn3r.artemis.util.misc;

import com.google.common.base.Charsets;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

/**
 *
 */
public class Throwables {

    public static String toString( Throwable t ) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream( bos );
        t.printStackTrace( out );

        return new String( bos.toByteArray(), Charsets.UTF_8 );

    }

    public static <T extends Throwable> T createMultiException(List<T> exceptions ) throws T {

        T root = exceptions.remove( 0 );

        for (T exception : exceptions) {
            root.addSuppressed( exception );
        }

        return root;

    }


}
