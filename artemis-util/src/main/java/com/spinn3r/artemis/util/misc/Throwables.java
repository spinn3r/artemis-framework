package com.spinn3r.artemis.util.misc;

import com.google.common.base.Charsets;
import com.google.common.base.Supplier;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

    public static <T extends Throwable> T createMultiException(List<T> exceptions) {

        T root = exceptions.remove( 0 );

        for (T exception : exceptions) {
            root.addSuppressed( exception );
        }

        return root;

    }

    public static <T, U extends RuntimeException, C extends Exception> T withCheckedException(Class<U> uncheckedExceptionClazz , Class<C> checkedExceptionClazz, Supplier<T> supplier) throws C {

        try {

            return supplier.get();

        } catch (RuntimeException e) {

            if ( uncheckedExceptionClazz.getClass().isInstance(e.getClass())) {

                try {

                    C checkedException;

                    try {

                        Constructor<C> declaredConstructor = checkedExceptionClazz.getDeclaredConstructor(String.class);

                        checkedException = declaredConstructor.newInstance(e.getMessage(), e);

                    } catch (NoSuchMethodException e1) {

                        // we cant init the exception with a message as there is
                        // no message constructor.
                        checkedException = checkedExceptionClazz.newInstance();

                    }

                    checkedException.initCause(e);
                    throw checkedException;

                } catch (InstantiationException|IllegalAccessException|InvocationTargetException e1) {
                    // this should not happen in production as it would be rare
                    // to pass in a
                    throw new RuntimeException("Could not instantiate checked exception: ", e1);
                }

            }

            throw e;

        }

    }

}
