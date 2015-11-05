package com.spinn3r.artemis.json;

import com.fasterxml.jackson.core.io.CharacterEscapes;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 *
 */
public class PrototypeFooEncoder {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final static byte BYTE_u = (byte) 'u';

    private final static byte BYTE_0 = (byte) '0';

    private static final byte BYTE_START_OBJECT = '{';
    private static final byte BYTE_END_OBJECT = '}';
    private static final byte BYTE_QUOTE = '\"';
    private static final byte BYTE_COLON = ':';
    private static final byte BYTE_COMMA = ',';
    private static final byte BYTE_BACKSLASH = (byte) '\\';

    // *** FOO FIELD NAMES AS UTF8

    private static final byte[] FIELD_NAME_FIRST_NAME = "first_name".getBytes( UTF_8 );
    private static final byte[] FIELD_NAME_LAST_NAME = "last_name".getBytes( UTF_8 );
    private static final byte[] FIELD_NAME_ADDRESS = "address".getBytes( UTF_8 );

    public final static int SURR1_FIRST = 0xD800;
    public final static int SURR1_LAST = 0xDBFF;
    public final static int SURR2_FIRST = 0xDC00;
    public final static int SURR2_LAST = 0xDFFF;

    private final byte[] buff;

    private final char[] text;

    // the index within the buffer
    private int bi = 0;

    // the text index
    private int ti = 0;

    public PrototypeFooEncoder(int bufferCapacity, int textBufferCapacity) {
        buff = new byte[ bufferCapacity ];
        text = new char[ textBufferCapacity ];
    }


    public ByteBuffer encode( Foo foo ) {

        buff[bi++] = BYTE_START_OBJECT;

        boolean hasFields = false;

        if ( foo.firstName != null ) {

            if ( hasFields ) buff[bi++] = BYTE_COMMA;

            buff[bi++] = BYTE_QUOTE;
            writeRaw( FIELD_NAME_FIRST_NAME );
            buff[bi++] = BYTE_QUOTE;
            buff[bi++] = BYTE_COLON;
            buff[bi++] = BYTE_QUOTE;

            int textLength = foo.firstName.length();
            foo.firstName.getChars( 0, textLength, text, 0 );
            writeRaw3( text, textLength );
            buff[bi++] = BYTE_QUOTE;
            hasFields = true;


        }

        if ( foo.lastName != null ) {

            if ( hasFields ) buff[bi++] = BYTE_COMMA;

            buff[bi++] = BYTE_QUOTE;
            writeRaw( FIELD_NAME_LAST_NAME );
            buff[bi++] = BYTE_QUOTE;
            buff[bi++] = BYTE_COLON;
            buff[bi++] = BYTE_QUOTE;

            int textLength = foo.lastName.length();
            foo.lastName.getChars( 0, textLength, text, 0 );
            writeRaw3( text, textLength );
            buff[bi++] = BYTE_QUOTE;
            hasFields = true;

        }


        if ( foo.address != null ) {

            if ( hasFields ) buff[bi++] = BYTE_COMMA;

            buff[bi++] = BYTE_QUOTE;
            writeRaw( FIELD_NAME_ADDRESS );
            buff[bi++] = BYTE_QUOTE;
            buff[bi++] = BYTE_COLON;
            buff[bi++] = BYTE_QUOTE;

            int textLength = foo.address.length();
            foo.address.getChars( 0, textLength, text, 0 );
            writeRaw3( text, textLength );
            buff[bi++] = BYTE_QUOTE;
            hasFields = true;

        }



        buff[bi++] = BYTE_END_OBJECT;

        return ByteBuffer.wrap( buff, 0, bi );

    }

    public void reset() {
        bi = 0;
        ti = 0;
    }

    private void writeRaw(byte[] data) {
        System.arraycopy( data, 0, buff, bi, data.length );
        bi += data.length;
    }

    private void writeRaw(char[] data, int length ) {

        for (int i = 0; i < length; i++) {

            int c = text[i];

            if (c < 0x80) {

                switch( c ) {

                    case '"':
                        buff[bi++] = '\'';
                        buff[bi++] = '"';
                        continue;

                    case '\\':
                        buff[bi++] = '\'';
                        buff[bi++] = '\'';
                        continue;

                    case '\n':
                        buff[bi++] = '\'';
                        buff[bi++] = 'n';
                        continue;

                    case '\b':
                        buff[bi++] = '\'';
                        buff[bi++] = 'b';
                        continue;

                    case '\f':
                        buff[bi++] = '\'';
                        buff[bi++] = 'f';
                        continue;

                    case '\r':
                        buff[bi++] = '\'';
                        buff[bi++] = 'r';
                        continue;

                    case '\t':
                        buff[bi++] = '\'';
                        buff[bi++] = 't';
                        continue;

                }

                buff[bi++] = (byte)c;

            } else if (c < 0x800) {
                buff[bi++] = (byte)(0xc0 | ((c >> 06)));
                buff[bi++] = (byte)(0x80 | (c & 0x3f));
            } else {
                buff[bi++] = (byte)(0xe0 | ((c >> 12))) ;
                buff[bi++] = (byte)(0x80 | ((c >> 06) & 0x3f)) ;
                buff[bi++] = (byte)(0x80 | (c & 0x3f)) ;
            }

        }

    }

    final static int[] _outputEscapes;

    static {
        int[] table = new int[128];
        // Control chars need generic escape sequence
        for (int i = 0; i < 32; ++i) {
            // 04-Mar-2011, tatu: Used to use "-(i + 1)", replaced with constant
            table[i] = CharacterEscapes.ESCAPE_STANDARD;
        }
        /* Others (and some within that range too) have explicit shorter
         * sequences
         */
        table['"'] = '"';
        table['\\'] = '\\';
        // Escaping of slash is optional, so let's not add it
        table[0x08] = 'b';
        table[0x09] = 't';
        table[0x0C] = 'f';
        table[0x0A] = 'n';
        table[0x0D] = 'r';
        _outputEscapes = table;
    }

    private final static char[] HC = "0123456789ABCDEF".toCharArray();

    private final static byte[] HB;

    private final static byte[] HEX_CHARS;

    static {
        int len = HC.length;
        HB = new byte[len];
        for (int i = 0; i < len; ++i) {
            HB[i] = (byte) HC[i];
        }

        HEX_CHARS = HB;

    }

    private void writeRaw2(final char[] data, int length ) {
        writeRaw2( data, 0, length );
    }

    private void writeRaw2(final char[] data, int offset, int length ) {

        // Fast+tight loop for ASCII-only, no-escaping-needed output
        final int[] escCodes = _outputEscapes;

        while (offset < length) {
            int ch = data[offset];
            // note: here we know that (ch > 0x7F) will cover case of escaping non-ASCII too:
            if (ch > 0x7F || escCodes[ch] != 0) {
                break;
            }
            buff[bi++] = (byte) ch;
            ++offset;
        }

        if (offset < length) {
            _writeStringSegment2(text, offset, length );
        }

    }

    private void writeRaw3(final char[] data, int data_length ) {

        int block_size = 30;

        int blocks = (data_length / block_size) + 1;

        int offset = 0;

        // FIXME: this actually isn't length but the last char...

        // ** do all but the last block.
        for (int block = 0; block < blocks - 1; block++) {
            int end = offset + block_size;

            //System.out.printf( "Working on block: %s with offset %s and length %s\n", block, offset, length );
            writeRaw2( data, offset, end );

            offset += block_size;
        }

        // ** now to the last block
        {
            //int block = blocks - 1;
            int length = offset + (data_length % block_size);
            //System.out.printf( "Working on last block with offset %s and length %s\n", offset, length );
            writeRaw2( data, offset, length );

        }

    }



    private final void _writeStringSegment2( final char[] data, int offset, final int end) {
        final int[] escCodes = _outputEscapes;

        while (offset < end) {
            int ch = data[offset++];
            if (ch <= 0x7F) {
                if (escCodes[ch] == 0) {
                    buff[bi++] = (byte) ch;
                    continue;
                }
                int escape = escCodes[ch];
                if (escape > 0) { // 2-char escape, fine
                    buff[bi++] = BYTE_BACKSLASH;
                    buff[bi++] = (byte) escape;
                } else {
                    // ctrl-char, 6-byte escape...
                    _writeGenericEscape(ch);
                }
                continue;
            }
            if (ch <= 0x7FF) { // fine, just needs 2 byte output
                buff[bi++] = (byte) (0xc0 | (ch >> 6));
                buff[bi++] = (byte) (0x80 | (ch & 0x3f));
            } else {
                _outputMultiByteChar(ch);
            }
        }
    }

    private void _writeGenericEscape(int charToEscape) {

        buff[bi++] = BYTE_BACKSLASH;
        buff[bi++] = BYTE_u;
        if (charToEscape > 0xFF) {
            int hi = (charToEscape >> 8) & 0xFF;
            buff[bi++] = HEX_CHARS[hi >> 4];
            buff[bi++] = HEX_CHARS[hi & 0xF];
            charToEscape &= 0xFF;
        } else {
            buff[bi++] = BYTE_0;
            buff[bi++] = BYTE_0;
        }
        // We know it's a control char, so only the last 2 chars are non-0
        buff[bi++] = HEX_CHARS[charToEscape >> 4];
        buff[bi++] = HEX_CHARS[charToEscape & 0xF];
    }

    private final void _outputMultiByteChar(int ch) {
        if (ch >= SURR1_FIRST && ch <= SURR2_LAST) { // yes, outside of BMP; add an escape
            buff[bi++] = BYTE_BACKSLASH;
            buff[bi++] = BYTE_u;

            buff[bi++] = HEX_CHARS[(ch >> 12) & 0xF];
            buff[bi++] = HEX_CHARS[(ch >> 8) & 0xF];
            buff[bi++] = HEX_CHARS[(ch >> 4) & 0xF];
            buff[bi++] = HEX_CHARS[ch & 0xF];
        } else {
            buff[bi++] = (byte) (0xe0 | (ch >> 12));
            buff[bi++] = (byte) (0x80 | ((ch >> 6) & 0x3f));
            buff[bi++] = (byte) (0x80 | (ch & 0x3f));
        }
    }

}
