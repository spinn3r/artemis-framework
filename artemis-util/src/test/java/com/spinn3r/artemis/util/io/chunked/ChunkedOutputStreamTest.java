package com.spinn3r.artemis.util.io.chunked;

import com.spinn3r.artemis.util.misc.Base64;
import com.spinn3r.artemis.util.crypto.SHA256;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

public class ChunkedOutputStreamTest {

    public static String DATA = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Test
    public void test1() throws Exception {

        ChunkedOutputStream chunkedOutputStream = new ChunkedOutputStream( 100, 10 );

        assertEquals( 1, chunkedOutputStream.chunks.size() );
        chunkedOutputStream.write( new byte[ 100 ] );
        assertEquals( 1, chunkedOutputStream.chunks.size() );
        chunkedOutputStream.write( (byte) 0x00 );
        assertEquals( 2, chunkedOutputStream.chunks.size() );

        assertEquals( 100, chunkedOutputStream.chunks.get( 0 ).capacity() );
        assertEquals( 10, chunkedOutputStream.chunks.get( 1 ).capacity() );

    }

    @Test
    public void test2() throws Exception {

        ChunkedOutputStream chunkedOutputStream = new ChunkedOutputStream( 10, 10 );

        chunkedOutputStream.write( DATA.getBytes() );
        assertEquals( 7, chunkedOutputStream.chunks.size() );

        assertEquals( toString( chunkedOutputStream ), DATA );

        assertEquals( Base64.encode( SHA256.encode( DATA ) ),
                      Base64.encode( chunkedOutputStream.computeSHA256() ) );

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        chunkedOutputStream.transferTo( bos );

        assertEquals( toString( bos ), DATA );

    }

    private static String toString( ChunkedOutputStream chunkedOutputStream ) {
        return new String( toByteArray( chunkedOutputStream ) );
    }

    private static String toString( ByteArrayOutputStream outputStream ) {
        return new String( outputStream.toByteArray() );
    }

    private static byte[] toByteArray( ChunkedOutputStream chunkedOutputStream ) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        for( Chunk chunk : chunkedOutputStream.chunks ) {
            bos.write( chunk.byteArrayBacking.buf, 0, chunk.written() );
        }

        return bos.toByteArray();

    }

}