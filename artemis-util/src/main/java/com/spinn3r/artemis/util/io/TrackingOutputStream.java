package com.spinn3r.artemis.util.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * OutputStream that implements tracking the number of bytes written to the
 * stream.
 */
public class TrackingOutputStream extends OutputStream {

    private final OutputStream delegate;

    private AtomicLong length = new AtomicLong( 0 );

    public TrackingOutputStream(OutputStream delegate) {
        this.delegate = delegate;
    }

    @Override
    public void write(int b) throws IOException {
        delegate.write( b );
        length.getAndAdd( 1 );
    }

    @Override
    public void write(byte[] b) throws IOException {
        delegate.write( b );
        length.getAndAdd( b.length );
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        delegate.write( b, off, len );
        length.getAndAdd( len );

    }

    @Override
    public void flush() throws IOException {
        delegate.flush();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

    public long length() {
        return length.get();
    }

}
