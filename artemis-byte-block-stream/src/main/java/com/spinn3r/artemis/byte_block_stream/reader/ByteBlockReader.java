package com.spinn3r.artemis.byte_block_stream.reader;

import com.spinn3r.artemis.byte_block_stream.ByteBlock;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;

/**
 *
 */
public interface ByteBlockReader extends Closeable {

    boolean hasNext() throws IOException;

    ByteBlock next() throws IOException;

}
