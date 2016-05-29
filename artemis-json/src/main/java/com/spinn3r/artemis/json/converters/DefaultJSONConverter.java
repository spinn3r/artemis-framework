package com.spinn3r.artemis.json.converters;

import com.spinn3r.artemis.json.JSON;
import com.spinn3r.artemis.json.JSONConverter;
import com.spinn3r.artemis.util.text.UTF8Data;

import java.nio.ByteBuffer;

/**
 * Framework to allow us to convert various types of records to JSON types (Strings,
 * UTF-8 byte arrays, or UTF-8 ByteBuffers.
 *
 */
public class DefaultJSONConverter implements JSONConverter {

    private final Object object;

    public DefaultJSONConverter(Object object) {
        this.object = object;
    }

    @Override
    public String asString() {
        return JSON.toJSON(object);
    }

    @Override
    public byte[] asByteArray() {
        return JSON.toByteArray(object);
    }

    @Override
    public ByteBuffer asByteBuffer() {
        return JSON.toByteBuffer(object);
    }

    @Override
    public UTF8Data asUTF8Data() {
        return new UTF8Data(asByteBuffer());
    }
}
