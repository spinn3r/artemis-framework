package com.spinn3r.artemis.byte_block_stream.writer;

import com.spinn3r.artemis.byte_block_stream.ByteBlock;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 */
public interface ByteBlockWriter extends Closeable {

    void write( ByteBlock byteBlock ) throws IOException;

}
