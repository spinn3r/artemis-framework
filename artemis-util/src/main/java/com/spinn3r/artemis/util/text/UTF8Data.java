package com.spinn3r.artemis.util.text;

import com.spinn3r.artemis.util.misc.ByteBuffers;

import java.nio.ByteBuffer;

/**
 * Represents raw/encoded UTF8 data.
 */
public class UTF8Data {

    private final ByteBuffer byteBuffer;

    public UTF8Data(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    @Override
    public String toString() {
        return ByteBuffers.toUTF8( byteBuffer );
    }

}
