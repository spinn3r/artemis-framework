package com.spinn3r.artemis.util.io.chunked;

import com.google.common.collect.Lists;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * An output stream for maintaining temporary/intermediate data while it's
 * being written.  Data is allocated in slabs/chunks as extents so that we have
 * predictable memory allocation.  We also do away with synchronization as it's
 * just wasteful in our use case.
 */
public class ChunkedOutputStream extends OutputStream {

    protected int initialCapacity;

    protected int extentCapacity;

    protected final List<Chunk> chunks = Lists.newArrayList();

    protected Chunk current;

    protected boolean transfered = false;

    public ChunkedOutputStream(int initialCapacity, int extentCapacity) {

        this.initialCapacity = initialCapacity;
        this.extentCapacity = extentCapacity;

        expand( initialCapacity );

    }

    public void write(int b) throws IOException {
        assertNotTransfered();
        ensureCapacity( 1 );
        current.write( b );
    }

    public void write( byte b[] ) throws IOException {
        write( b, 0, b.length );
    }

    public void write(byte b[], int off, int len) throws IOException {
        assertNotTransfered();

        int remaining = len;

        while( remaining > 0 ) {

            // make sure we have at least the capacity for one byte for each round
            ensureCapacity( 1 );

            // the amount of data that we can fit into the current chunk.
            int window = current.capacity() - current.written();

            current.write( b, off, Math.min( remaining, window ) );
            remaining = remaining - window;
            off += window;

        }

    }

    /**
     *
     * @return The total number of bytes written.
     */
    public int written() {

        int result = 0;

        for (Chunk chunk : chunks) {
            result += chunk.written();
        }

        return result;

    }

    // ensure that we can handle the new capacity.
    private void ensureCapacity( int length ) {

        if ( current.capacity() < current.written() + length ) {
            expand( extentCapacity );
        }

    }

    private void expand( int newCapacity ) {
        current = new Chunk( newCapacity );
        chunks.add( current );
    }

    private void assertNotTransfered() {

        if ( transfered )
            throw new IllegalStateException( "Data already transfered to target." );

    }

    /**
     * Perform an 'efficient' zero copy of the data held here to the given output
     * stream.
     * @param target
     */
    public void transferTo( OutputStream target ) throws IOException {

        assertNotTransfered();

        int nrChunks = chunks.size();

        for (int i = 0; i < nrChunks; i++) {

            Chunk chunk = chunks.remove( 0 );
            target.write( chunk.byteArrayBacking.buf, 0, chunk.written() );

        }

    }

    public byte[] computeSHA256() {

        assertNotTransfered();

        Hasher hasher = Hashing.sha256().newHasher();

        for( Chunk chunk : chunks ) {
            hasher.putBytes( chunk.byteArrayBacking.buf, 0, chunk.written() );
        }

        return hasher.hash().asBytes();

    }


}

