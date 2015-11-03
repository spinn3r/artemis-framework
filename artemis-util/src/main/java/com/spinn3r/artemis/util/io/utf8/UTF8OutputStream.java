package com.spinn3r.artemis.util.io.utf8;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CoderResult;

/**
 *
 */
public class UTF8OutputStream extends OutputStream {

    private final OutputStream delegate;

    public UTF8OutputStream(OutputStream delegate) {
        this.delegate = delegate;
    }

    // borrowed from:
    //
    // http://grepcode.com/file_/repository.grepcode.com/java/root/jdk/openjdk/6-b14/sun/nio/cs/UTF_8.java/?v=source

    public void write( String src ) throws IOException {

        int sp = 0;
        int sl = src.length();

        //ASCII only loop optimization... prevents the if statements below
        //which slow down encoding.  This was in the original but I'm not sure it
        //actually will improve performance.

        char[] chars = src.toCharArray();

        while ( sp < sl && chars[sp] < '\u0080')
            delegate.write( (byte) chars[sp++] );

        while (sp < sl) {
            int c = chars[sp];
            if (c < 0x80) {
                delegate.write( (byte)c );
            } else if (c < 0x800) {
                delegate.write( (byte)(0xc0 | ((c >> 06))) );
                delegate.write( (byte)(0x80 | (c & 0x3f)) );
            } else {
                delegate.write( (byte)(0xe0 | ((c >> 12))) );
                delegate.write( (byte)(0x80 | ((c >> 06) & 0x3f)) );
                delegate.write( (byte)(0x80 | (c & 0x3f)) );
            }
            sp++;
        }

    }

    @Override
    public void write(int b) throws IOException {
        delegate.write( b );
    }

    @Override
    public void write(byte[] b) throws IOException {
        delegate.write( b );
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        delegate.write( b, off, len );
    }

    @Override
    public void flush() throws IOException {
        delegate.flush();
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }

}
