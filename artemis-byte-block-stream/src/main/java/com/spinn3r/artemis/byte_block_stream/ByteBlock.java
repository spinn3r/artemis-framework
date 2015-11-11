package com.spinn3r.artemis.byte_block_stream;

import java.nio.ByteBuffer;

/**
 * Represents a chunk of data within a container.  We can combine containers
 * to build aggregate streams of byte blocks across files (similar to chunks in
 * a DFS).
 *
 * The underlying blocks can be anything.  JSON, Protocol Buffers, AVRO, etc.
 */
public class ByteBlock {

    private ByteBuffer byteBuffer;

    public ByteBlock( byte[] data )  {
        this( ByteBuffer.wrap( data ) );
    }

    public ByteBlock(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public int length() {
        return byteBuffer.limit() - byteBuffer.position();
    }

}
