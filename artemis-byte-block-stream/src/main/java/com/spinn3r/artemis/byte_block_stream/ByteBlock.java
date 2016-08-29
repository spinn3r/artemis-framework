package com.spinn3r.artemis.byte_block_stream;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.nio.ByteBuffer;

/**
 * Represents a chunk of data within a container.  We can combine containers
 * to build aggregate streams of byte blocks across files (similar to chunks in
 * a DFS).
 *
 * The underlying blocks can be anything.  JSON, Protocol Buffers, AVRO, blob
 * of binary data or text data, etc.
 */
public class ByteBlock {

    private final ImmutableMap<String,String> headers;

    private final ByteBuffer byteBuffer;

    public ByteBlock( byte[] data ) {
        this( ImmutableMap.copyOf( Maps.newHashMap() ), data );
    }

    public ByteBlock( ImmutableMap<String,String> headers, byte[] data )  {
        this( headers, ByteBuffer.wrap( data ) );
    }

    public ByteBlock(ByteBuffer byteBuffer) {
        this( ImmutableMap.copyOf( Maps.newHashMap() ), byteBuffer );
    }

    public ByteBlock(ImmutableMap<String,String> headers, ByteBuffer byteBuffer) {
        this.headers = headers;
        this.byteBuffer = byteBuffer;
    }

    public ImmutableMap<String, String> getHeaders() {
        return headers;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public int length() {
        return byteBuffer.limit() - byteBuffer.position();
    }

}
