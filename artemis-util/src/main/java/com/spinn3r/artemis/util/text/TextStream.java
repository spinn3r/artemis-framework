package com.spinn3r.artemis.util.text;

/**
 *
 */
public class TextStream {

    private StringBuilder buff = new StringBuilder();

    public void printf( String format, Object... args ) {
        buff.append( String.format( format, args ) );
    }

    public void println( String data ) {
        buff.append( data );
        buff.append( "\n" );
    }

    @Override
    public String toString() {
        return buff.toString();
    }

}
