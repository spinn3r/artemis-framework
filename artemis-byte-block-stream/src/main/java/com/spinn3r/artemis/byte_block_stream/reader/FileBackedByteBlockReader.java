package com.spinn3r.artemis.byte_block_stream.reader;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.spinn3r.artemis.byte_block_stream.ByteBlock;
import com.spinn3r.artemis.byte_block_stream.Magic;
import com.spinn3r.artemis.util.misc.ByteBuffers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.Map;

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

        // now read the headers
        int nrHeaders = backing.getInt();

        Map<String,String> headers = Maps.newHashMap();

        for (int i = 0; i < nrHeaders; i++) {
            String headerName = readString();
            String headerValue = readString();
            headers.put( headerName, headerValue );
        }

        // now get the number of bytes of data to read.
        int length = backing.getInt();

        ByteBuffer slice = backing.slice();

        slice.limit( length );

        backing.position( backing.position() + length );

        return new ByteBlock( ImmutableMap.copyOf( headers ), slice );

    }

    private String readString() {
        int len = backing.getInt();
        byte[] data = new byte[ len ];
        backing.get(data);
        return new String( data, Charsets.UTF_8 );
    }

    @Override
    public void close() throws IOException {

        if ( fileChannel != null )
            fileChannel.close();

        if ( fileInputStream != null )
            fileInputStream.close();

    }

}
