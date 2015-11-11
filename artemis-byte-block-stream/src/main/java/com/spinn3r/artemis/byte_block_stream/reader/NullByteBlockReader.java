package com.spinn3r.artemis.byte_block_stream.reader;

import com.spinn3r.artemis.byte_block_stream.ByteBlock;

import java.io.IOException;
import java.util.Iterator;

/**
 *
 */
public class NullByteBlockReader implements ByteBlockReader {

    @Override
    public void close() throws IOException {

    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public ByteBlock next() {
        return null;
    }

}
