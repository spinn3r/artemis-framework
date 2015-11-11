package com.spinn3r.artemis.byte_block_stream.writer;

import java.io.IOException;

/**
 * Thrown if we're trying to work with a directory that already has block files.
 */
public class BlockSequenceFilesExistException extends IOException {

    public BlockSequenceFilesExistException() {
    }

    public BlockSequenceFilesExistException(String message) {
        super( message );
    }

    public BlockSequenceFilesExistException(String message, Throwable cause) {
        super( message, cause );
    }

    public BlockSequenceFilesExistException(Throwable cause) {
        super( cause );
    }

}
