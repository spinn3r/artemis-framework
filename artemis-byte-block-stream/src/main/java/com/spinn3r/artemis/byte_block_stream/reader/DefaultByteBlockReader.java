package com.spinn3r.artemis.byte_block_stream.reader;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class DefaultByteBlockReader extends FileBackedRollingByteBlockReader {

    public DefaultByteBlockReader(File dir) throws IOException {
        super( dir );
    }

}
