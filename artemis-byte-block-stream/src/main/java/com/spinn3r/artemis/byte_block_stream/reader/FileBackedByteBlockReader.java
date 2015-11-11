package com.spinn3r.artemis.byte_block_stream.reader;

import com.spinn3r.artemis.byte_block_stream.ByteBlock;
import com.spinn3r.artemis.byte_block_stream.Magic;
import com.spinn3r.artemis.util.misc.ByteBuffers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;

/**
 *
 */
public class FileBackedByteBlockReader implements ByteBlockReader {

    private final File file;

    private final FileInputStream fileInputStream;

    private final FileChannel fileChannel;

    private final ByteBuffer backing;

    public FileBackedByteBlockReader(File file) throws IOException {
        this.file = file;
        this.fileInputStream = new FileInputStream( file );
        this.fileChannel = fileInputStream.getChannel();
        this.backing = fileChannel.map( FileChannel.MapMode.READ_ONLY, 0, file.length() );

        // verify magic header

        byte[] magic = new byte[ Magic.HEADER.capacity() ];
        this.backing.get( magic );

        ByteBuffer magicByteBuffer = ByteBuffer.wrap( magic );
        if ( ! Magic.HEADER.equals( magicByteBuffer ) ) {
            throw new IOException( String.format( "File has wrong header: %s vs %s",
                                                  ByteBuffers.toHex(Magic.HEADER), ByteBuffers.toHex( magicByteBuffer ) ) );
        }

    }

    @Override
    public boolean hasNext() {
        return backing.position() < backing.limit();
    }

    @Override
    public ByteBlock next() {

        int length = backing.getInt();

        ByteBuffer slice = backing.slice();

        slice.limit( length );

        backing.position( backing.position() + length );

        return new ByteBlock( slice );

    }

    @Override
    public void close() throws IOException {

        if ( fileChannel != null )
            fileChannel.close();

        if ( fileInputStream != null )
            fileInputStream.close();

    }

}
