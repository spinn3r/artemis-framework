package com.spinn3r.artemis.byte_block_stream.cat;

import com.beust.jcommander.JCommander;
import com.spinn3r.artemis.byte_block_stream.ByteBlock;
import com.spinn3r.artemis.byte_block_stream.reader.ByteBlockReader;
import com.spinn3r.artemis.byte_block_stream.reader.DefaultByteBlockReader;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Print a byte block to the screen...
 */
public class Cat {

    public void exec( CatArgs catArgs ) throws IOException {

        File file = new File( catArgs.getPath() );
        ByteBlockReader byteBlockReader = new DefaultByteBlockReader( file );

        while( byteBlockReader.hasNext() ) {

            ByteBlock byteBlock = byteBlockReader.next();

            for (Map.Entry<String, String> entry : byteBlock.getHeaders().entrySet()) {
                System.out.printf( "%s: %s\n", entry.getKey(), entry.getValue() );
            }

            //if ( )

            // FIXME: if the file content type is text/ just print it after applying
            // the charset... otherwise we should Hext print it

        }

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
