package com.spinn3r.artemis.test;

import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @Deprecated use CapturedTerminalService
 */
public class BaseTestWithCapturedOutput {

    protected PrintStream stdout;
    protected PrintStream stderr;

    protected final ByteArrayOutputStream out = new ByteArrayOutputStream();
    protected final ByteArrayOutputStream err = new ByteArrayOutputStream();

    @Before
    public void setUp() throws Exception {

        stdout = System.out;
        stderr = System.err;

        System.setOut(new PrintStream( out ));
        System.setErr(new PrintStream( err ));
    }

    @After
    public void tearDown() throws Exception {

        System.setOut( stdout );
        System.setErr( stderr );

        System.out.printf( "===== OUT:\n" );

        System.out.println( out.toString() );

        System.out.printf( "===== ERR:\n" );

        System.out.println( err.toString() );

    }

}
