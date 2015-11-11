package com.spinn3r.artemis.byte_block_stream.writer;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 *
 */
public class FileBackedByteBlockWriterFactory implements ByteBlockWriterFactory {

    private final File dir;

    private final String extension;

    private int nonce = 0;

    public FileBackedByteBlockWriterFactory(File dir) {
        this.dir = dir;
        this.extension = "bbs";
    }

    @Override
    public void init() throws IOException {

        if ( ! dir.exists() ) {
            throw new IOException( "Directory doesn't exist: " + dir.getPath() );
        }

        if ( ! dir.isDirectory() ) {
            throw new IOException( "Not a directory: " + dir.getPath() );
        }

        String[] blockFiles = dir.list( (dir, name) -> name.endsWith( "." + extension ) );

        if ( blockFiles == null ) {
            throw new IOException( "Could not work with directory: " + dir.getPath() );
        }

        if ( blockFiles.length > 0 ) {
            throw new BlockSequenceFilesExistException();
        }

    }

    @Override
    public ByteBlockWriter create() throws IOException {

        File file = new File( dir, String.format( "%05d.%s", nonce++, extension ) );
        return new FileBackedByteBlockWriter( file );

    }

}
