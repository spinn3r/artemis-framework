package com.spinn3r.artemis.network;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class GZipDetector {

    /**
     * GZIP header magic number.
     */
    public final static int GZIP_MAGIC = 0x8b1f;

    public static boolean detect( InputStream is ) throws IOException {

        is.mark( 10 );

        // Check header magic
        if ( readUShort(is) != GZIP_MAGIC ) {
            return false;
        }

        is.reset();

        return true;

    }

    /*
     * Reads unsigned short in Intel byte order.
     */
    private static int readUShort(InputStream in) throws IOException {
        int b = readUByte(in);
        return ((int)readUByte(in) << 8) | b;
    }

    /*
     * Reads unsigned byte.
     */
    private static int readUByte(InputStream in) throws IOException {

        int b = in.read();

        if (b < 0 || b > 255) {

            // Report on this.in, not argument in; see read{Header, Trailer}.
            throw new NetworkException( in.getClass().getName() +
                                        ".read() returned value out of range -1..255: " + b);

        }

        return b;

    }

    public static void main( String[] args ) throws Exception {

        InputStream is = new FileInputStream( "test.gz" ) ;
        is = new BufferedInputStream( is );

        System.out.printf( "result: %s\n", detect( is ) );

    }

}
