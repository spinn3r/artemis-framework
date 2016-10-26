package com.spinn3r.artemis.util.misc;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;

import java.io.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 *
 */
public class Files {

    public static final String toUTF8( String path ) throws IOException {
        return toUTF8( new File( path ) );
    }

    /**
     * Convert the given file to UTF8 string.
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static final String toUTF8( File file ) throws IOException {

        try ( FileInputStream fis = new FileInputStream( file ) ) {

            return toUTF8( fis );

        }

    }

    public static final String toUTF8( InputStream inputStream ) throws IOException {
        checkNotNull(inputStream, "Input stream is null");
        return CharStreams.toString( new InputStreamReader( inputStream, Charsets.UTF_8 ) );
    }

    public static void mkdirs( String path ) throws IOException {
        mkdirs( new File( path ) );
    }

    public static void mkdirs(  File file  ) throws IOException {

        if ( file.exists() )
            return;

        if ( ! file.mkdirs() ) {
            throw new IOException( "Unable to create dir: " + file.getPath() );
        }

    }

    public static void renameTo( File source, File target ) throws IOException {

        if ( ! source.renameTo( target )) {

            throw new IOException( String.format( "Unable to move %s to %s",
                                                  source.getPath(),
                                                  target.getPath() ) );

        }

    }

    public static void writeTo( String path, String content ) throws IOException {

        try( FileOutputStream fileOutputStream = new FileOutputStream( path ); ) {
            fileOutputStream.write( content.getBytes( Charsets.UTF_8 ) );
        }

    }


}
