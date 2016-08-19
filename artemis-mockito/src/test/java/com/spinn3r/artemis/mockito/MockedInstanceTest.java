package com.spinn3r.artemis.mockito;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 */
public class MockedInstanceTest {

    @Test
    public void testMockedInstance() throws Exception {

        MyPojo myPojo = new MyPojo();

        myPojo = spy(myPojo);

        assertEquals("hello", myPojo.myMethod());


    }

    @Test(expected = RuntimeException.class)
    public void testThrowingException() throws Exception {

        MyPojo myPojo = new MyPojo();

        myPojo = spy(myPojo);

        when(myPojo.myMethod()).thenThrow(new RuntimeException());
        myPojo.myMethod();

    }

    static class MyPojo {

        public String myMethod() {
            return "hello";
        }

    }

}
