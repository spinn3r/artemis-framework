package com.spinn3r.artemis.byte_block_stream.writer;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.spinn3r.artemis.byte_block_stream.ByteBlock;
import com.spinn3r.artemis.byte_block_stream.Magic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.util.Map;

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

        ImmutableMap<String, String> headers = byteBlock.getHeaders();
        channel.write( intBuffer( headers.size() ) );

        for (Map.Entry<String, String> header : headers.entrySet()) {
            String key = header.getKey();
            String value = header.getValue();

            // key
            channel.write( intBuffer( key.length() ) );
            channel.write( stringBuffer( key ) );

            //value
            channel.write( intBuffer( value.length() ) );
            channel.write( stringBuffer( value ) );

        }

        channel.write( intBuffer( byteBlock.length() ) );
        channel.write( byteBlock.getByteBuffer() );

    }

    private ByteBuffer intBuffer( int value ) {

        ByteBuffer result = ByteBuffer.allocate( 4 );
        result.mark();
        result.putInt( value );
        result.reset();

        return result;

    }

    private ByteBuffer stringBuffer( String value ) {
        return ByteBuffer.wrap( value.getBytes( Charsets.UTF_8 ) );
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
