package com.spinn3r.artemis.util.text;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrintBufferTest {

    PrintBuffer sut;

    @Before
    public void setUp() throws Exception {
        sut = new PrintBuffer();
    }

    @Test
    public void testBasicFunctionality() throws Exception {

        assertEquals("Hello world\n" +
                       "Hello world\n" +
                       "Hello world this is a variable: haha\n" +
                       "\n" +
                       "\n",
                     sut.println("Hello world")
                        .printf("Hello world\n")
                        .printf("Hello world this is a variable: %s\n", "haha")
                        .println()
                        .println()
                        .toString());

    }

}