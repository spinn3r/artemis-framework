package com.spinn3r.artemis.util.text;

import org.junit.Test;

import static com.spinn3r.artemis.util.text.TextFormatter.indent;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class TextFormatterTest {

    @Test
    public void test1() throws Exception {

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
}