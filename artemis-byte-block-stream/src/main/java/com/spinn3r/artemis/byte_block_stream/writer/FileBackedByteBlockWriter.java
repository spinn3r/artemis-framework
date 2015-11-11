package com.spinn3r.artemis.byte_block_stream.writer;

import com.spinn3r.artemis.byte_block_stream.ByteBlock;
import com.spinn3r.artemis.byte_block_stream.Magic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

/**
 *
 */
public class FileBackedByteBlockWriter implements ByteBlockWriter {

    private final File file;
    private final FileOutputStream fileOutputStream;
    private final WritableByteChannel channel;

    public FileBackedByteBlockWriter(File file) throws IOException {
        this.file = file;
        this.fileOutputStream = new FileOutputStream( file );
        this.channel = Channels.newChannel( fileOutputStream );

        channel.write( Magic.HEADER.slice() );

    }

    @Override
    public void write(ByteBlock byteBlock) throws IOException {

        ByteBuffer lengthBuff = ByteBuffer.allocate( 4 );
        lengthBuff.mark();
        lengthBuff.putInt( byteBlock.length() );
        lengthBuff.reset();

        channel.write( lengthBuff );
        channel.write( byteBlock.getByteBuffer() );

    }

    @Override
    public void close() throws IOException {

        if ( this.channel != null ) {
            this.channel.close();
        }

        if ( this.fileOutputStream != null ) {
            this.fileOutputStream.close();
        }

    }

}
