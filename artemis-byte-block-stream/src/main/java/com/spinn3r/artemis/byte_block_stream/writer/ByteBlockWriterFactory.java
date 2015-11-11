package com.spinn3r.artemis.byte_block_stream.writer;

import java.io.IOException;

/**
 *
 */
public interface ByteBlockWriterFactory {

    /**
     * Called when we are about to write a set of files to the directory.
     *
     * @throws IOException thrown if something fails during init
     */
    void init() throws IOException;

    ByteBlockWriter create() throws IOException;

}
