package com.spinn3r.artemis.util.misc;

import com.google.common.base.Supplier;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThrowablesTest {

    @Test
    public void testToString() throws Exception {

        String value = Throwables.toString( new NullPointerException( "" ) );

        //System.out.printf( value );

        assertTrue( value != null );

    }

    @Test(expected = MyCheckedException.class)
    public void testWithCheckedExceptionReturningBoolean() throws Exception {

        Throwables.withCheckedException(MyUncheckedException.class, MyCheckedException.class, () -> myBooleanUncheckedFunction());

    }


    private boolean myBooleanUncheckedFunction() {
        throw new MyUncheckedException();
    }

    public static class MyUncheckedException extends RuntimeException {


    }

    public static class MyCheckedException extends Exception {


    }


}