package com.spinn3r.artemis.byte_block_stream.writer;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.byte_block_stream.ByteBlock;
import com.spinn3r.artemis.byte_block_stream.reader.FileBackedRollingByteBlockReader;
import com.spinn3r.artemis.byte_block_stream.reader.RollingByteBlockReader;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class TestByteBlockStreams {

    @Test
    public void testWritingThenReading() throws Exception {

        String path = "/tmp/test-bbs";

        File dir = initPath( path );

        FileBackedByteBlockWriterFactory fileBackedByteBlockWriterFactory = new FileBackedByteBlockWriterFactory( dir );

        Map<String,String> headers = Maps.newHashMap();
        headers.put( "hello", "world" );

        int nrRecords = 400;
        try( RollingByteBlockWriter rollingByteBlockWriter = new RollingByteBlockWriter( fileBackedByteBlockWriterFactory, 1_000 ) ) {

            for (int i = 0; i < nrRecords; i++) {

                ByteBuffer buff = ByteBuffer.allocate( 100 );

                buff.mark();
                buff.putInt( i );
                buff.reset();

                ByteBlock byteBlock = new ByteBlock( ImmutableMap.copyOf( headers ), buff );
                rollingByteBlockWriter.write( byteBlock );

            }

        }

        try( RollingByteBlockReader rollingByteBlockReader = new FileBackedRollingByteBlockReader( dir ) ) {

            int found = 0;
            while( rollingByteBlockReader.hasNext() ) {

                ByteBlock byteBlock = rollingByteBlockReader.next();

                assertEquals( found, byteBlock.getByteBuffer().getInt() );
                assertEquals( headers, byteBlock.getHeaders() );

                ++found;
            }

            assertEquals( nrRecords, found );

        }


        Files.exists( Paths.get( "/tmp/test-bbs/00000.bbs"  ) );
        Files.exists( Paths.get( "/tmp/test-bbs/00035.bbs"  ) );

    }

    private File initPath(String path) throws IOException {
        File dir = new File( path );

        if ( dir.exists() ) {
            for(File file: dir.listFiles()) file.delete();
        } else {
            Files.createDirectory( dir.toPath() );
        }
        return dir;
    }

    @Test( expected = BlockSequenceFilesExistException.class )
    public void testWritingWithoutPurgingDirectory() throws Exception {

        File dir = new File( "/tmp/test-bbs-writing-without-purging" );

        if ( ! dir.exists() )
            Files.createDirectory( dir.toPath() );

        FileBackedByteBlockWriterFactory fileBackedByteBlockWriterFactory = new FileBackedByteBlockWriterFactory( dir );

        int nrRecords = 10;
        try( RollingByteBlockWriter rollingByteBlockWriter = new RollingByteBlockWriter( fileBackedByteBlockWriterFactory, 1_000 ) ) {

            for (int i = 0; i < nrRecords; i++) {

                ByteBuffer buff = ByteBuffer.allocate( 100 );
                ByteBlock byteBlock = new ByteBlock( buff );
                rollingByteBlockWriter.write( byteBlock );

            }

        }

        // now try it a second time...

        try( RollingByteBlockWriter rollingByteBlockWriter = new RollingByteBlockWriter( fileBackedByteBlockWriterFactory, 1_000 ) ) {

        }
    }

    @Test(expected = IOException.class )
    public void testReadNoDirectory() throws Exception {

        File dir = new File( "/tmp/byte-blocks/asdf" );
        RollingByteBlockReader rollingByteBlockReader = new FileBackedRollingByteBlockReader( dir );

    }

    @Test
    public void testReadEmptyDirectory() throws Exception {

        File dir = new File( "/tmp/byte-blocks/empty" );

        Files.deleteIfExists( dir.toPath() );

        Files.createDirectories( dir.toPath() );

        RollingByteBlockReader rollingByteBlockReader = new FileBackedRollingByteBlockReader( dir );

        int found = 0;
        while( rollingByteBlockReader.hasNext() ) {
            rollingByteBlockReader.next();
            ++found;
        }

        assertEquals( 0, found );

    }

}