package com.spinn3r.artemis.util.io;

import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;

import static org.junit.Assert.*;

public class CloseablesTest {

    @Test
    public void test1() throws Exception {

        assertEquals( null, doClose( new WorkingCloseable() ) );
        assertNotNull( null, doClose( new FailingCloseable() ) );
        assertEquals( 1, doClose( new FailingCloseable(), new FailingCloseable() ).getSuppressed().length );

    }

    private static IOException doClose(Closeable... closeables) {

        try {

            Closeables.close( closeables );

        } catch ( IOException e ) {
            return e;
        }

        return null;

    }

    class FailingCloseable implements Closeable {

        @Override
        public void close() throws IOException {
            throw new IOException( "failed" );
        }

    }

    class WorkingCloseable implements Closeable {

        @Override
        public void close() throws IOException {


        }

    }

}

