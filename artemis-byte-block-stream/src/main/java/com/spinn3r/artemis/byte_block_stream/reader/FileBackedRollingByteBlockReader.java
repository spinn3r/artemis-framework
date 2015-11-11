package com.spinn3r.artemis.byte_block_stream.reader;

import com.google.common.collect.Lists;
import com.spinn3r.artemis.byte_block_stream.ByteBlock;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 *
 */
public class FileBackedRollingByteBlockReader implements RollingByteBlockReader {

    private final File dir;

    private final String extension;

    private final Iterator<FileBackedByteBlockReader> byteBlockReaderIterator;

    private ByteBlockReader byteBlockReader = null;

    public FileBackedRollingByteBlockReader(File dir) throws IOException {
        this.dir = dir;
        this.extension = "bbs";

        if ( ! dir.exists() ) {
            throw new IOException( "Directory doesn't exist: " + dir.getPath() );
        }

        if ( ! dir.isDirectory() ) {
            throw new IOException( "Not a directory: " + dir.getPath() );
        }

        List<FileBackedByteBlockReader> blockFiles = Lists.newArrayList();

        File[] files = dir.listFiles();

        if ( files == null ) {
            throw new IOException( "No files found for dir: " + dir.getPath() );
        }

        Arrays.sort( files, (file0, file1) -> file0.getName().compareTo( file1.getName() ) );

        for (File file : files) {

            if ( file.getName().endsWith( extension ) ) {
                blockFiles.add( new FileBackedByteBlockReader( file ) );
            }

        }

        this.byteBlockReaderIterator = blockFiles.iterator();

        rollover();

    }

    @Override
    public boolean hasNext() throws IOException {

        if ( byteBlockReader.hasNext() ) {
            return true;
        }

        while( byteBlockReaderIterator.hasNext() ) {

            rollover();

            if ( byteBlockReader.hasNext() )
                return true;

        }

        return false;

    }

    @Override
    public ByteBlock next() throws IOException {
        return byteBlockReader.next();
    }

    @Override
    public void close() throws IOException {

        if ( byteBlockReader != null ) {
            byteBlockReader.close();
        }

    }

    private void rollover() throws IOException {

        if ( byteBlockReader != null ) {
            byteBlockReader.close();
        }

        if ( byteBlockReaderIterator.hasNext() ) {
            byteBlockReader = byteBlockReaderIterator.next();
        } else {
            byteBlockReader = new NullByteBlockReader();
        }

    }

}
