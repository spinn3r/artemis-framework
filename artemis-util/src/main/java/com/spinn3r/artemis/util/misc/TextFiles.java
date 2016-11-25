package com.spinn3r.artemis.util.misc;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import java.io.*;

import static com.google.common.base.Preconditions.*;

/**
 *
 */
public class TextFiles {

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

}
