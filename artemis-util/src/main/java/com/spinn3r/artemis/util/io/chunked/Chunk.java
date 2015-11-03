package com.spinn3r.artemis.util.io.chunked;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 */
public class Chunk extends OutputStream {

    protected int written;

    protected int capacity;

    protected ByteArrayBacking byteArrayBacking;

    public Chunk(int capacity) {
        this.capacity = capacity;
        this.byteArrayBacking = new ByteArrayBacking( capacity );
    }

    public void write(int b) throws IOException {
        byteArrayBacking.write( b );
        ++written;
    }

    public void write( byte b[] ) throws IOException {
        byteArrayBacking.write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        byteArrayBacking.write( b, off, len );
        written += len;
    }

    public int written() {
        return written;
    }

    public int capacity() {
        return capacity;
    }

}
