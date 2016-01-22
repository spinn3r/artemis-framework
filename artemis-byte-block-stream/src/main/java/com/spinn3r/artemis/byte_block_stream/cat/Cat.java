package com.spinn3r.artemis.byte_block_stream.cat;

import com.beust.jcommander.JCommander;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.spinn3r.artemis.byte_block_stream.ByteBlock;
import com.spinn3r.artemis.byte_block_stream.StandardHeaders;
import com.spinn3r.artemis.byte_block_stream.reader.ByteBlockReader;
import com.spinn3r.artemis.byte_block_stream.reader.DefaultByteBlockReader;
import com.spinn3r.artemis.util.misc.ByteBuffers;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Print a byte block to the screen...
 */
public class Cat {

    public void exec( CatArgs catArgs ) throws IOException {

        File file = new File( catArgs.getPath() );
        ByteBlockReader byteBlockReader = new DefaultByteBlockReader( file );

        while( byteBlockReader.hasNext() ) {

            ByteBlock byteBlock = byteBlockReader.next();

            System.out.printf( "======\n" );

            // print the headers
            ImmutableMap<String, String> headers = byteBlock.getHeaders();

            Set<String> headerNames = Sets.newTreeSet( headers.keySet() );

            for (String headerName : headerNames) {
                String headerValue = headers.get( headerName );
                System.out.printf( "%s: %s\n", headerName, headerValue );
            }

            System.out.printf( "== BEGIN RECORD DATA ==\n" );

            if ( isText( byteBlock ) ) {

                byte[] data = ByteBuffers.toByteArray( byteBlock.getByteBuffer() );
                String text = new String( data, headers.get( StandardHeaders.CONTENT_CHARSET ) );
                System.out.printf( "%s\n", text );

            } else {
                System.out.printf( "%s\n", ByteBuffers.toHex( byteBlock.getByteBuffer() ) );
            }

            System.out.printf( "== END RECORD DATA ==\n" );

        }

    }

    private boolean isText( ByteBlock byteBlock ) {

        ImmutableMap<String, String> headers = byteBlock.getHeaders();

        if ( ! headers.containsKey( StandardHeaders.CONTENT_TYPE ) ) {
            return false;
        }

        if ( ! headers.get( StandardHeaders.CONTENT_TYPE ).startsWith( "text/" ) ) {
            return false;
        }

        if ( ! headers.containsKey( StandardHeaders.CONTENT_CHARSET ) ) {
            return false;
        }

        return true;

    }

    public static void main(String[] args) throws IOException {

        CatArgs catArgs = new CatArgs();
        JCommander jc = new JCommander(catArgs);
        jc.parse( args );

        if ( catArgs.getHelp() ) {
            jc.usage();
            System.exit( 1 );
        }

        new Cat().exec( catArgs );

    }

}
