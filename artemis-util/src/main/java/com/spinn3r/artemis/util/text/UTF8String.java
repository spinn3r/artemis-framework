package com.spinn3r.artemis.util.text;

import java.nio.ByteBuffer;

/**
 * Represents a string encoded in UTF8.
 */
public class UTF8String {

    private final ByteBuffer data;

    public UTF8String(ByteBuffer data) {
        this.data = data;
    }

    public ByteBuffer getData() {
        return data;
    }

    @Override
    public String toString() {
        return new String( data.array(), data.position(), data.limit() );
    }

}
