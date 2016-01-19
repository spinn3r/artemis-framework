package com.spinn3r.artemis.byte_block_stream.writer;

import java.io.File;
import java.io.IOException;

/**
 * Implements a byte block writer that creates rolling chunks and stores them
 * in a directory.
 */
public class DefaultByteBlockWriter extends RollingByteBlockWriter {

    public DefaultByteBlockWriter( File dir ) throws IOException {
        super( new FileBackedByteBlockWriterFactory( dir ) );
    }

    public DefaultByteBlockWriter(File dir , int maxCapacity) throws IOException {
        super( new FileBackedByteBlockWriterFactory( dir ), maxCapacity );
    }

}
