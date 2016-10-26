package com.spinn3r.artemis.util.text;

import org.junit.Test;

import static com.spinn3r.artemis.util.text.TextFormatter.*;
import static org.junit.Assert.*;

public class TextFormatterTest {

    @Test
    public void testIndent() throws Exception {

        assertEquals( "    hello\n",
                      indent( "hello" ) );

        assertEquals( "    hello\n" +
                        "    world\n",
                      indent( "hello\nworld" ) );


        assertEquals( "    hello\n" +
                        "    world\n",
                      indent( "hello\nworld\n" ) );


        assertEquals( "    hello\n" +
                        "    world\n",
                      indent( "hello\nworld\n\n" ) );

        assertEquals( "    hello\n" +
                        "    world\n",
                      indent( "hello\nworld\n\n\n\n" ) );

        assertEquals( "    hello\n" +
                        "    \n" +
                        "    world\n",
                      indent( "hello\n\nworld\n\n\n\n" ) );

    }

    @Test
    public void testUnindent() throws Exception {

        assertEquals("hello",
                     unindent("   hello"));

        assertEquals("hello\nworld\n",
                     unindent("   hello\n   world\n"));


    }

}